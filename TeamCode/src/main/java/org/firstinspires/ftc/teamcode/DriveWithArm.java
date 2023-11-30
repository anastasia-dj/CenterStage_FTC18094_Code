package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;


@TeleOp(name = "FOD (yes arm)")
public class DriveWithArm extends Hardware {
    private static double powerCurve(double power, boolean slowSpeed) {
        if (slowSpeed)
            return power / 8.0; // In slow speed move with 1/8 of the max power
        else
            return power / 3.0;
    }

    private static double armPowerCurve(double power) {
        final double calm = 2.8;
        double value = Math.pow(Math.abs(power), calm) * Math.signum(power);
        value = Math.min(value, 0.75); // max lifting power 0.75
        value = Math.max(value, -0.25); // limit down power to not more then 0.25 to avoid slamming the arm
        return value;
    }

    public void addMotorTelemetry() {
//        telemetry.addData(name + ".isBusy", busy);
//        telemetry.addData(name + ".position", currentPosition);
//        telemetry.addData(name + ".getTargetPosition", targetPosition);
        String text =
                " position:" + armLeft.getCurrentPosition()
//                + " target:" + armRight.getTargetPosition()
                        + " amp:" + armRight.getCurrent(CurrentUnit.AMPS) + ":" + armLeft.getCurrent(CurrentUnit.AMPS);
        RobotLog.v(text);
        telemetry.addData("arm", text);
    }

    public void initArmMotorSimple(DcMotorEx motor) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setPower(0.0);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void runOpMode() {
        initHardware(); // This initializes all the hardware

        initArmMotorSimple(armRight); // try directly controlling the arm motors with simple power
        initArmMotorSimple(armLeft); // try directly controlling the arm motors with simple power

        long previousTime = System.currentTimeMillis();
        boolean previousX = false;
        boolean previousY = false;
        boolean previousA = false;
        boolean antigravity = false; // activates arm motor control

        while (opModeIsActive()) {
            // This is the gamepad API: https://ftctechnh.github.io/ftc_app/doc/javadoc/com/qualcomm/robotcore/hardware/Gamepad.html
            boolean x = gamepad1.x; // x is true when the button is pressed and false when it is released.
            boolean y = gamepad1.y; // ditto
            boolean slowSpeed = gamepad1.left_bumper; // when pressed, slow down motion and arm

//            // Check if x was released
//            if (!x && previousX) {
//                // arm down position is at 0
//                moveArm(0.0, slowSpeed);
//            }
//            previousX = x;
//
//            // Check if y was released
//            if (!y && previousY) {
//                // The arm up angle in degrees relative to arm down
//                moveArm(40.0, slowSpeed);
//            }
//            previousY = y;

//            // When the motor thinks that it reached 0 position, power it off
//            if (armLeft.getTargetPosition() == 0 && armRight.getTargetPosition() == 0 && !armLeft.isBusy() && !armRight.isBusy()) {
//                armRight.setPower(0.0);
//                armLeft.setPower(0.0);
//            }

            // A presses
            boolean a = gamepad2.a;
            if (!a && previousA) {
                // arm down position is at 0
                antigravity = !antigravity; // toggle antigravity
                if (antigravity) {
                    initArmMotorSimple(armLeft);
                    initArmMotorSimple(armRight);
                }
            }
            previousA = a;

            // simple direct power to the arm motors
//            boolean antigravity = gamepad2.left_bumper; // when pressed, enable arm antigravity
            double armAngle = 0.0;
            double armPower = 0.0; // controls the arm power use up/down on the left joystick
            if (antigravity) {
                int currentPosition = armLeft.getCurrentPosition();
                armAngle = Math.toRadians((currentPosition - 20.0) / (80 - 20) * 90); // 20 is horizontal, 80 is vertical
                double force = Math.cos(armAngle) * 0.25; // Gravity requires 0.25 power to compensate

                armPower = armPowerCurve(-gamepad2.left_stick_y); // control with left stick up/down
                double upPower = gamepad2.left_trigger * 0.5 + force / 2;
                double downPower = gamepad2.right_trigger * 0.3 - force / 8;
                armPower += upPower - downPower;

                //                armPower += (gamepad1.dpad_up ? upPower : 0) + (gamepad1.dpad_down ? -downPower : 0); // or the dpad up/down (only on/off)
                armPower += force;
                RobotLog.v("antigravity pos:" + currentPosition + " angle:" + Math.toDegrees(armAngle) + " force:" + force);
            }
            armRight.setPower(armPower);
            armLeft.setPower(armPower);

//            // Having two PID loops trying to move both motors is making both sides fight each other.
//            // To solve this, we will just use the right arm PID and copy the current to the left
//            // motor
//            double armRightCurrent = armRight.getCurrent(CurrentUnit.AMPS);
//            armLeft.setPower(armRightCurrent/2.0);

            double leftStickY = powerCurve(-gamepad1.right_stick_y, slowSpeed); // input range is -1..+1
            double leftStickX = powerCurve(gamepad1.right_stick_x, slowSpeed); // ditto
            double rightStickX = powerCurve(gamepad1.left_stick_x, slowSpeed); // ditto

            // Read sensor data
            YawPitchRollAngles robotOrientation = imu.getRobotYawPitchRollAngles();
            double yaw = robotOrientation.getYaw(AngleUnit.RADIANS);

            double rotX = leftStickX * Math.cos(-yaw) - leftStickY * Math.sin(-yaw);
            double rotY = leftStickX * Math.sin(-yaw) + leftStickY * Math.cos(-yaw);

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

            telemetry.addData("yaw", yaw);
            addMotorTelemetry();
            if (antigravity) {
                telemetry.addData("antigravity", "on, angle:" + armAngle);
            }
            telemetry.update();
        }
    }
}
