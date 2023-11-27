package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp(name = "FOD (no arm)")
public class DriveWithoutArm extends Hardware {
    private static double powerCurve(double power, boolean slowSpeed) {
        if (slowSpeed)
            return power / 8.0; // In slow speed move with 1/8 of the max power
        else
            return power / 2.2;
    }

    public void addMotorTelemetry(String name, DcMotor motor) {
        telemetry.addData(name + ".isBusy", motor.isBusy());
        telemetry.addData(name + ".getCurrentPosition", motor.getCurrentPosition());
    }

    @Override
    public void runOpMode() {
        initHardware(); // This initializes all the hardware

        long previousTime = System.currentTimeMillis();
        boolean previousX = false;
        boolean previousY = false;

        while (opModeIsActive()) {
            // This is the gamepad API: https://ftctechnh.github.io/ftc_app/doc/javadoc/com/qualcomm/robotcore/hardware/Gamepad.html
            boolean x = gamepad1.x; // x is true when the button is pressed and false when it is released.
            boolean y = gamepad1.y; // ditto
            boolean slowSpeed = gamepad1.left_bumper; // when pressed, slow down motion

            // Check if x was released
//            if (!x && previousX) {
//                // arm down position is at 0
//                moveArm(0.0, slowSpeed);
//            }
//            previousX = x;
//
//            // Check if y was released
//            if (!y && previousY) {
//                // The arm up angle in degrees relative to arm down
//                moveArm(80.0, slowSpeed);
//            }
//            previousY = y;

            double left_stick_y = powerCurve(-gamepad1.right_stick_y, slowSpeed); // input range is from 1 to -1
            double left_stick_x = powerCurve(gamepad1.right_stick_x, slowSpeed); // input range -1 - 1
            double rightStickX = powerCurve(gamepad1.left_stick_x, slowSpeed); // input range -1 - 1

            // Read sensor data
            YawPitchRollAngles robotOrientation = imu.getRobotYawPitchRollAngles();
            double yaw = robotOrientation.getYaw(AngleUnit.RADIANS);

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

            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);

            // Add the time it took to run the loop to the telemetry.
            long time = System.currentTimeMillis();
            telemetry.addData("time (ms)", time - previousTime);
            previousTime = time;

            telemetry.addData("y", y);
            telemetry.addData("previousY", previousY);
            telemetry.addData("X", x);
            telemetry.addData("previousX", previousX);

            telemetry.addData("yaw", yaw);
            //addMotorTelemetry("armLeft", armLeft);
            //addMotorTelemetry("armRight", armRight);
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
