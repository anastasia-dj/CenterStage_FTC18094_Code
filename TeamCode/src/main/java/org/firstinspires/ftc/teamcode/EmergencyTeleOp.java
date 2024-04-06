package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;



@TeleOp(name = "Emergency Manual Control")
public class EmergencyTeleOp extends Hardware {

    /**
     * This function is executed when this OpMode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {
        initHardware();
        initArmMotorSimple(armRight); // try directly controlling the arm motors with simple power
        initArmMotorSimple(armLeft); // try directly controlling the arm motors with simple power

        //private static double armPowerCurve(double power) {

        //}






        long previousTime = System.currentTimeMillis();
        boolean previousG1X = false;
        boolean previousG1Y = false;
        boolean previousG2A = false;
        boolean antigravity = false; // activates arm motor control & gravity compensation
        double wristAngle = 0.0; // moves to 0.0 when running
        //PIDFCoefficients pid = new PIDFCoefficients(15.0, 0.005, 0.002, 0.0, MotorControlAlgorithm.LegacyPID);
        //wrist.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pid);
        //telemetry.addData("Wrist PID set to ", pid);
        double clawAngle = 0.0; // moves to 0.0 when running

        waitForStart();
        float y;
        float x;
        float rx;

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                // This is the gamepad API: https://ftctechnh.github.io/ftc_app/doc/javadoc/com/qualcomm/robotcore/hardware/Gamepad.html
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
                    // arm down position is at 0
                    antigravity = !antigravity; // toggle antigravity
                    if (antigravity) {
                        initArmMotorSimple(armLeft);
                        initArmMotorSimple(armRight);
                    }
                }
                previousG2A = a;

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
                wristAngle = clamp(wristAngle + gamepad2.right_stick_y, 0.0, 195.0);
                clawAngle = clamp(clawAngle + gamepad2.right_stick_x / 70, 0.0, 1.0);

                if (gamepad2.x) wristAngle = 195.0;
                if (gamepad2.y) wristAngle = 60.0;
                if (gamepad2.b) wristAngle = 0.0;
                if (gamepad2.left_bumper) clawAngle = 0.50;
                if (gamepad2.right_bumper) clawAngle = 0;

                //PIDFCoefficients pid = new PIDFCoefficients(35.0, 0.005, 0.002, 0.0, MotorControlAlgorithm.LegacyPID);
                //wrist.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pid);
                //RobotLog.v("Wrist PID set to " + pid);


                moveWrist(wristAngle);
                moveClaw(clawAngle);


                // having two PID loops trying to move both motors is making both sides fight each other
                // (future note, one of the encoders was messed up lol <- that motor is marked)
                // to solve this we will just use the right arm PID and copy the current to the left
                // motor

//            double armRightCurrent = armRight.getCurrent(CurrentUnit.AMPS);
//            armLeft.setPower(armRightCurrent/2.0);

                // Y stick is reversed/
                y = -gamepad1.right_stick_y;
                x = gamepad1.right_stick_x;
                rx = gamepad1.left_stick_x;
                RobotLog.v(String.valueOf(y));
                RobotLog.v(String.valueOf(x));
                RobotLog.v(String.valueOf(rx));

                // there's a divide by two in these in order to make the power smaller
                // if you want to make the power smaller, then make the '2' higher
                // if you want it stronger make it lower or remove the division by two
                double dividedBy = 2;
                if (gamepad1.right_bumper){
                    dividedBy = 0.5;
                } else if (gamepad1.left_bumper){
                    dividedBy = 4;
                }

                frontLeft.setPower((y + x + rx) / dividedBy);
                backLeft.setPower(((y - x) + rx) / dividedBy);
                frontRight.setPower(((y - x) - rx) / dividedBy);
                backRight.setPower(((y + x) - rx) / dividedBy);

                if (gamepad1.a){
                    launcher.setPosition(1);
                    telemetry.addData("launcher to position", launcher.getPosition());
                // making it do a full rotation
                }
                if (gamepad1.y){
                    launcher.setPosition(0);
                    telemetry.addData("launcher to position", launcher.getPosition());
                }



            }
        }
    }
}