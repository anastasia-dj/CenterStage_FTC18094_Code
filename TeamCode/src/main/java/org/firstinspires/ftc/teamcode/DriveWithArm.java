package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;


@TeleOp(name = "Field Oriented Driving")
public class DriveWithArm extends Hardware {
    public static double powerCurve(double power, boolean slowSpeed) {
        if (slowSpeed)
            return power / 8.0; // In slow speed move with 1/8 of the max power <- to make it slower, make denominator bigger/xfwri
        else
            return power / 3.0; // regular power
    }
    public static double armPowerCurve(double power) {
        final double clam = 2.8;
        double value = Math.pow(Math.abs(power), clam) * Math.signum(power);
        value = Math.min(value, 0.95); // max lifting power 0.75
        value = Math.max(value, -0.35); // limit down power to not more then 0.25 to avoid slamming the arm
        RobotLog.v("value: " + value);
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
        //RobotLog.v(text);
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
        initHardware(); // This initializes all the hardware <- Check Hardware file for more details

        initArmMotorSimple(armRight); // try directly controlling the arm motors with simple power
        initArmMotorSimple(armLeft); // try directly controlling the arm motors with simple power

        long previousTime = System.currentTimeMillis();
        boolean previousG1X = false;
        boolean previousG1Y = false;
        boolean previousG2A = false;
        boolean previousX = false;
        boolean antigravity = false;
        boolean emergencyDrive = false; // activates arm motor control & gravity compensation
        double wristAngle = 0.0; // moves to 0.0 when running
        //PIDFCoefficients pid = new PIDFCoefficients(15.0, 0.005, 0.002, 0.0, MotorControlAlgorithm.LegacyPID);
        //wrist.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pid);
        //telemetry.addData("Wrist PID set to ", pid);
        double clawAngle = 0.0; // moves to 0.0 when running

        while (opModeIsActive()) {
            // This is the gamepad API: https://ftctechnh.github.io/ftc_app/doc/javadoc/com/qualcomm/robotcore/hardware/Gamepad.html
            boolean y = gamepad1.y; // ditto
            boolean slowSpeed = gamepad1.left_bumper; // when pressed, slow down motion

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
            if (!a & previousG2A) {
                antigravity = !antigravity; // toggle antigravity
                if (antigravity) {
                    initArmMotorSimple(armLeft);
                    initArmMotorSimple(armRight);
                    // turn on the motors
                }
            }
            previousG2A = a;

            // X presses
            boolean x = gamepad1.x;
            if (!x & previousX) {
                emergencyDrive = !emergencyDrive; // toggle emergency drive
            }
            previousX = x;




            // simple direct power to the arm motors
//            boolean antigravity = gamepad2.left_bumper; // when pressed, enable arm antigravity
            double armAngle = 0.0;
            double armPower = 0.0; // controls the arm power use up/down on the left joystick
            if (antigravity) {
                int currentPosition = armLeft.getCurrentPosition();
                armAngle = Math.toRadians((currentPosition - 20.0) / (80 - 20) * 90); // 20 is horizontal, 80 is vertical

                double force = Math.cos(armAngle) * 0.15;
                if (gamepad2.dpad_up) force = Math.cos(armAngle) * 0.37;

                // Gravity requires 0.15 (used to be 0.25) power to compensate

                armPower = armPowerCurve(-gamepad2.left_stick_y); // control with left stick up/down
                double upPower = gamepad2.left_trigger * 0.5 + force / 2;
                double downPower = gamepad2.right_trigger * 0.3 - force / 8;
                armPower += upPower - downPower;
                armPower += force;
                RobotLog.v("armPower: " + armPower);

            }
            armRight.setPower(armPower);
            armLeft.setPower(armPower);

            // having two PID loops trying to move both motors is making both sides fight each other
            // (future note, one of the encoders was messed up lol <- that motor is marked)
            // to solve this we will just use the right arm PID and copy the current to the left
            // motor

//            double armRightCurrent = armRight.getCurrent(CurrentUnit.AMPS);
//            armLeft.setPower(armRightCurrent/2.0);
            if (emergencyDrive){
                // Y stick is reversed
                float rightStickY = -gamepad1.right_stick_y;
                float rightStickX = gamepad1.right_stick_x;
                float RX = gamepad1.left_stick_x;
                RobotLog.v(String.valueOf(rightStickY));
                RobotLog.v(String.valueOf(x));
                RobotLog.v(String.valueOf(RX));

                //makes power smaller or bigger (slow & fast function on the emergency drive)
                double dividedBy = 2;
                if (gamepad1.right_bumper){
                    dividedBy = 0.5;
                } else if (gamepad1.left_bumper){
                    dividedBy = 4;
                }
                frontLeft.setPower((rightStickY + rightStickX + RX) / dividedBy);
                backLeft.setPower(((rightStickY - rightStickX) + RX) / dividedBy);
                frontRight.setPower(((rightStickY - rightStickX) - RX) / dividedBy);
                backRight.setPower(((rightStickY + rightStickX) - RX) / dividedBy);
                telemetry.addData("yaw", "not applicable");
                //addMotorTelemetry();
            } else {

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
                if (gamepad1.right_bumper) {
                    denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rightStickX), 1) / 2;
                }


                double frontLeftPower = (rotY + rotX + rightStickX) / denominator;
                double backLeftPower = (rotY - rotX + rightStickX) / denominator;
                double frontRightPower = (rotY - rotX - rightStickX) / denominator;
                double backRightPower = (rotY + rotX - rightStickX) / denominator;

                frontLeft.setPower(frontLeftPower);
                backLeft.setPower(backLeftPower);
                frontRight.setPower(frontRightPower);
                backRight.setPower(backRightPower);

                telemetry.addData("yaw", yaw);
                //addMotorTelemetry();
            }

            // update the wrist and claw angles
            wristAngle = clamp(wristAngle + gamepad2.right_stick_y, 0.0, 195.0);
            clawAngle = clamp(clawAngle + gamepad2.right_stick_x / 70, 0.0, 1.0);

            if (gamepad2.x) wristAngle = 195.0;
            if (gamepad2.y) wristAngle = 60.0;
            if (gamepad2.b) wristAngle = 0.0;
            if (gamepad2.left_bumper) clawAngle = 0.50;
            if (gamepad2.right_bumper) clawAngle = 0;

            if (gamepad1.a){
                launcher.setPosition(1);
                telemetry.addData("launcher to position", launcher.getPosition());

            }
            if (gamepad1.y){
                launcher.setPosition(0);
                telemetry.addData("launcher to position", launcher.getPosition());
            }


                //PIDFCoefficients pid = new PIDFCoefficients(35.0, 0.005, 0.002, 0.0, MotorControlAlgorithm.LegacyPID);
            //wrist.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pid);
            //RobotLog.v("Wrist PID set to " + pid);


            moveWrist(wristAngle);
            moveClaw(clawAngle);

            // add the time it took to run the loop to the telemetry
            //long time = System.currentTimeMillis();
            //telemetry.addData("time (ms)", time - previousTime);
            //previousTime = time;

            // adds to the telemetry which mode the robot is in
            if (emergencyDrive) {
                telemetry.addData("mode", "Emergency Driving");
            } else {
                telemetry.addData("mode", "Field Oriented Driving");
            }

            telemetry.addData("wristAngle", wristAngle);
            telemetry.addData("clawAngle", clawAngle);

            // shows antigravity on telemetry
            if (antigravity) {
                telemetry.addData("antigravity", "on");
            }
            telemetry.update();
        }
    }
}