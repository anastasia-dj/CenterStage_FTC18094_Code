package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp(name = "FOD (yes arm)")
public class DriveWithArm extends LinearOpMode {
    BHI260IMU imu;
    private DcMotorEx BL;
    private DcMotorEx FL;
    private DcMotorEx FR;
    private DcMotorEx BR;
    private DcMotorEx armLeft;
    private DcMotorEx armRight;

    private static double powerCurve(double power, boolean slowSpeed) {
        if (slowSpeed)
            return power / 10.0; // In slow speed move with 1/10 of the max power
        else
            return power / 2.2;
    }

    public static void initArmMotor(@NonNull DcMotorEx motor) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setPower(0.75);
        motor.setVelocity(ARM_NORMAL_SPEED, AngleUnit.DEGREES);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    final static double ENCODER_COUNT_PER_ROTATION = 288.0;
    final static double ARM_NORMAL_SPEED = 10.0; // degrees per second
    final static double ARM_SLOW_SPEED = 3.0; // degrees per second

    public void moveArm(double degree, boolean slowSpeed) {
        int position = (int) Math.round(degree / 360.0 * ENCODER_COUNT_PER_ROTATION);
        armLeft.setVelocity(slowSpeed ? ARM_SLOW_SPEED : ARM_NORMAL_SPEED, AngleUnit.DEGREES);
        armRight.setVelocity(slowSpeed ? ARM_SLOW_SPEED : ARM_NORMAL_SPEED, AngleUnit.DEGREES);
        armLeft.setTargetPosition(position);
        armRight.setTargetPosition(position);
        armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void addMotorTelemetry(String name, DcMotor motor) {
        telemetry.addData(name + ".isBusy", motor.isBusy());
        telemetry.addData(name + ".getCurrentPosition", motor.getCurrentPosition());
    }

    @Override
    public void runOpMode() {
        // Initialize motors
        // https://ftctechnh.github.io/ftc_app/doc/javadoc/com/qualcomm/robotcore/hardware/DcMotor.html
        // https://ftctechnh.github.io/ftc_app/doc/javadoc/com/qualcomm/robotcore/hardware/DcMotorEx.html
        FL = (DcMotorEx) hardwareMap.dcMotor.get("FL");
        FR = (DcMotorEx) hardwareMap.dcMotor.get("FR");
        BL = (DcMotorEx) hardwareMap.dcMotor.get("BL");
        BR = (DcMotorEx) hardwareMap.dcMotor.get("BR");
        armRight = (DcMotorEx) hardwareMap.dcMotor.get("ArmRight");
        armLeft = (DcMotorEx) hardwareMap.dcMotor.get("ArmLeft");

        // Initialize BHI260AP sensor
        imu = hardwareMap.get(BHI260IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));

        imu.initialize(parameters);

        BL.setDirection(DcMotor.Direction.REVERSE);

        armLeft.setDirection(DcMotor.Direction.REVERSE);
        initArmMotor(armLeft);
        initArmMotor(armRight);

        waitForStart();

        long previousTime = System.currentTimeMillis();
        boolean previousX = false;
        boolean previousY = false;

        while (opModeIsActive()) {
            // This is the gamepad API: https://ftctechnh.github.io/ftc_app/doc/javadoc/com/qualcomm/robotcore/hardware/Gamepad.html
            boolean x = gamepad1.x; // x is true when the button is pressed and false when it is released.
            boolean y = gamepad1.y; // ditto
            boolean slowSpeed = gamepad1.left_bumper; // when pressed, slow down motion and arm

            // Check if x was released
            if (!x && previousX) {
                // arm down position is at 0
                moveArm(0.0, slowSpeed);
            }
            previousX = x;

            // Check if y was released
            if (!y && previousY) {
                // The arm up angle in degrees relative to arm down
                moveArm(80.0, slowSpeed);
            }
            previousY = y;

            double left_stick_y = powerCurve(-gamepad1.left_stick_y, slowSpeed); // input range is from 1 to -1
            double left_stick_x = powerCurve(gamepad1.left_stick_x, slowSpeed); // input range -1 - 1
            double rightStickX = powerCurve(gamepad1.right_stick_x, slowSpeed); // input range -1 - 1

            // Read sensor data
            YawPitchRollAngles robotOrientation = imu.getRobotYawPitchRollAngles();
            double yaw = robotOrientation.getYaw(AngleUnit.RADIANS);

            // angles = imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
            // double botHeading = angles.firstAngle;

            double rotX = left_stick_x * Math.cos(-yaw) - left_stick_y * Math.sin(-yaw);
            double rotY = left_stick_x * Math.sin(-yaw) + left_stick_y * Math.cos(-yaw);

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]

            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rightStickX), 1);
            double frontLeftPower = (rotY + rotX + rightStickX) / denominator;
            double backLeftPower = (rotY - rotX + rightStickX) / denominator;
            double frontRightPower = (rotY - rotX - rightStickX) / denominator;
            double backRightPower = (rotY + rotX - rightStickX) / denominator;

            FL.setPower(frontLeftPower);
            BL.setPower(backLeftPower);
            FR.setPower(frontRightPower);
            BR.setPower(backRightPower);

            // Add the time it took to run the loop to the telemetry.
            long time = System.currentTimeMillis();
            telemetry.addData("time (ms)", time - previousTime);
            previousTime = time;

            telemetry.addData("y", y);
            telemetry.addData("previousY", previousY);
            telemetry.addData("X", x);
            telemetry.addData("previousX", previousX);

            telemetry.addData("yaw", yaw);
            addMotorTelemetry("armLeft", armLeft);
            addMotorTelemetry("armRight", armRight);
            telemetry.addData("slowSpeed", slowSpeed); // remove once you validate the slow speed

            //     telemetry.addData("Left Stick X", left_stick_x);
            //     telemetry.addData("Left Stick Y", left_stick_y);
            //     telemetry.addData("Right Stick X", right_stick_x);
            //     telemetry.addData("angles", angles);
            //     telemetry.addData("frontLeftPower", frontLeftPower);
            //     telemetry.addData("backLeftPower", backLeftPower);
            //     telemetry.addData("frontRightPower", frontRightPower);
            //     telemetry.addData("backRightPower", backRightPower);
            telemetry.update();
        }
    }
}
