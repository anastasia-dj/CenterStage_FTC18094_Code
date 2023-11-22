package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;


@TeleOp(name = "FOD (yes arm)")
public class DriveWithArm extends LinearOpMode {


    BHI260IMU imu;


    private DcMotor BL;
    private DcMotor FL;
    private DcMotor FR;
    private DcMotor BR;


    private DcMotor ArmLeft;


    private DcMotor ArmRight;


    private static double powerCurve(double power) {
        return power / 2.2;
    }


    public void moveArm(double velocity, int degree) {
        ArmLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ArmRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ((DcMotorEx) ArmLeft).setVelocity(velocity);
        ((DcMotorEx) ArmRight).setVelocity(velocity);
        ArmLeft.setTargetPosition(degree);
        ArmRight.setTargetPosition(degree);
    }


    @Override
    public void runOpMode() {
        // Initialize motors
        FL = hardwareMap.dcMotor.get("FL");
        FR = hardwareMap.dcMotor.get("FR");
        BL = hardwareMap.dcMotor.get("BL");
        BR = hardwareMap.dcMotor.get("BR");
        ArmRight = hardwareMap.dcMotor.get("ArmRight");
        ArmLeft = hardwareMap.dcMotor.get("ArmLeft");


        // Initialize BHI260AP sensor
        imu = hardwareMap.get(BHI260IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));


        imu.initialize(parameters);
        Orientation angles;


        BL.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();


        long previousTime = System.currentTimeMillis();
        while (opModeIsActive()) {
            double left_stick_y = powerCurve(-gamepad1.left_stick_y); // input range is from 1 to -1
            double left_stick_x = powerCurve(gamepad1.left_stick_x); // input range -1 - 1
            double right_stick_x = powerCurve(gamepad1.right_stick_x); // input range -1 - 1


            // Read sensor data
            YawPitchRollAngles robotOrientation = imu.getRobotYawPitchRollAngles();
            double yaw = robotOrientation.getYaw(AngleUnit.RADIANS);
            double botHeading = yaw;


            // angles = imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
            // double botHeading = angles.firstAngle;


            double rotX = left_stick_x * Math.cos(-botHeading) - left_stick_y * Math.sin(-botHeading);
            double rotY = left_stick_x * Math.sin(-botHeading) + left_stick_y * Math.cos(-botHeading);


            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]


            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(right_stick_x), 1);


            double frontLeftPower = (rotY + rotX + right_stick_x) / denominator;
            double backLeftPower = (rotY - rotX + right_stick_x) / denominator;
            double frontRightPower = (rotY - rotX - right_stick_x) / denominator;
            double backRightPower = (rotY + rotX - right_stick_x) / denominator;



/*


           telemetry.addData("Left Stick X", left_stick_x);
           telemetry.addData("Left Stick Y", left_stick_y);
           telemetry.addData("Right Stick X", right_stick_x);
           telemetry.addData("angles", angles);
           telemetry.addData("botheading", botHeading);
           telemetry.addData("frontLeftPower", frontLeftPower);
           telemetry.addData("backLeftPower", backLeftPower);
           telemetry.addData("frontRightPower", frontRightPower);
           telemetry.addData("backRightPower", backRightPower);
           telemetry.update();
*/

            FL.setPower(frontLeftPower);
            BL.setPower(backLeftPower);
            FR.setPower(frontRightPower);
            BR.setPower(backRightPower);

            // Add the time it took to run the loop to the telemetry.
            long time = System.currentTimeMillis();


            telemetry.addData("time (ms)", time - previousTime);
            previousTime = time;


            telemetry.addData("yaw", yaw);
            telemetry.update();
        }
    }
}













