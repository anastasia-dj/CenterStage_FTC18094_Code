package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import java.security.KeyStore;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "LinearOpMode")
public class TeleOp1 extends LinearOpMode {
    DrivingBot robot;
    double max_pow = 0.6;

    @Override
    public void runOpMode() {
        robot = new DrivingBot();
        robot.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        robot.claw.setPosition(0);

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) { // reset arm from init
            telemetry.addData("Status", "opMode");
            telemetry.addData("elbow", robot.elbow.getCurrentPosition());
            telemetry.addData("claw", robot.claw.getPosition());
            telemetry.addData("arm v", ((DcMotorEx) robot.elbow).getVelocity());
            telemetry.addData("left arm", robot.armLeft.getCurrentPosition());
            telemetry.addData("right arm", robot.armRight.getCurrentPosition());
            telemetry.addData("max power", max_pow);
            //telemetry.update();

            // ARM
            /*
                rest/intake: elbow = 0, arm = 0
                low goal: elbow = 190, arm = 0
                medium goal: elbow = 315, arm = 250
                high goal: elbow = 460, arm = 360
             */
            if (gamepad2.x) { // rest/intake
                if (robot.armLeft.getCurrentPosition() <= 260) { // from low arm position
                    moveArm(300,0);
                    moveElbow(230, 0);
                } else if((robot.armLeft.getCurrentPosition() >= 260) & (robot.armLeft.getCurrentPosition() <= 310)) { // from medium arm position
                    moveArm(400,200);
                    moveElbow(300, 300);
                    sleep(400);
                    moveElbow(250, 0);
                    moveArm(250, 0);
                } else { // from high arm position
                    moveArm(400,200);
                    moveElbow(300, 300);
                    sleep(600);
                    moveElbow(300, 0);
                    moveArm(250, 0);

                }
                robot.armLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                robot.armRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                robot.elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

            } else if (gamepad2.a) { // low goal
                moveElbow(900, 220);
                moveArm(300, 0);
            } else if (gamepad2.b) { // medium goal
                if (robot.elbow.getCurrentPosition() >= 150) {
                    moveArm(800, 265);
                    sleep(300);
                    moveElbow(700, 365);
                } else {
                    moveElbow(700, 365);
                    sleep(300);
                    moveArm(800, 265);
                }
            } else if (gamepad2.y) { // high goal
                if (robot.elbow.getCurrentPosition() >= 150) {
                    moveArm(700,365);
                    sleep(300);
                    moveElbow(700, 490);

                } else {
                    moveElbow(800, 410);
                    sleep(200);
                    moveArm(700,365);
                    moveElbow(700, 490);
                }
            }

            // ADJUST
            if (gamepad2.dpad_right) {
                moveArm(700, robot.armRight.getCurrentPosition() + 15);
            } else if (gamepad2.dpad_left) {
                moveArm(700, robot.armRight.getCurrentPosition() - 15);
            }

            if (gamepad2.dpad_up) {
                moveElbow(600, robot.elbow.getCurrentPosition() + 10);
            } else if (gamepad2.dpad_down) {
                moveElbow(700, robot.elbow.getCurrentPosition() - 10);
            }

            // CLAW
            if (gamepad2.left_bumper) { // close claw
                robot.claw.setPosition(0.5);
            } else if (gamepad2.right_bumper) { // open claw
                robot.claw.setPosition(0);
            }



            // TANK DRIVE
/*
            if (gamepad1.right_stick_y > 0.3 || gamepad1.dpad_down) {
                robot.FR.setPower(gamepad1.right_stick_y * -max_pow);
                robot.BR.setPower(gamepad1.right_stick_y * -max_pow);
            } else if (gamepad1.right_stick_y < -0.3 || gamepad1.dpad_up) {
                robot.FR.setPower(gamepad1.right_stick_y * -max_pow);
                robot.BR.setPower(gamepad1.right_stick_y * -max_pow);
            } else {
                robot.FR.setPower(0);
                robot.BR.setPower(0);
            }

            if (gamepad1.left_stick_y > 0.3 || gamepad1.dpad_down) {
                robot.FL.setPower(gamepad1.left_stick_y * -max_pow);
                robot.BL.setPower(gamepad1.left_stick_y * -max_pow);
            } else if (gamepad1.left_stick_y < -0.3 || gamepad1.dpad_up) {
                robot.FL.setPower(gamepad1.left_stick_y * -max_pow);
                robot.BL.setPower(gamepad1.left_stick_y * -max_pow);
            } else {
                robot.FL.setPower(0);
                robot.BL.setPower(0);
            }*/





            if ((gamepad1.right_stick_y < -0.3 || gamepad1.right_stick_y > 0.3) || (gamepad1.left_stick_y < -0.3 || gamepad1.left_stick_y > 0.3)) {
                robot.FR.setPower(gamepad1.right_stick_y * -max_pow);
                robot.BR.setPower(gamepad1.right_stick_y * -max_pow);
                robot.FL.setPower(gamepad1.left_stick_y * -max_pow);
                robot.BL.setPower(gamepad1.left_stick_y * -max_pow);
            } else if (gamepad1.right_bumper) {
                robot.FR.setPower(-0.5);
                robot.BL.setPower(-0.5);
                robot.FL.setPower(0.5);
                robot.BR.setPower(0.5);
            } else if (gamepad1.left_bumper) {
                robot.FL.setPower(-0.5);
                robot.BR.setPower(-0.5);
                robot.FR.setPower(0.5);
                robot.BL.setPower(0.5);
            } else {
                robot.setPower(0);
            }



/*
            if (gamepad1.y && max_pow <= 0.9) {
                max_pow = max_pow + 0.1;
            }

            if (gamepad1.a && max_pow >= 0.3) {
                max_pow = max_pow - 0.1;
            }

 */

            telemetry.update();
        }
    }



    public void moveArm(double velocity, int position) {
        robot.armLeft.setTargetPosition(position);
        robot.armRight.setTargetPosition(position);
        robot.armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ((DcMotorEx) robot.armLeft).setVelocity(velocity);
        ((DcMotorEx) robot.armRight).setVelocity(velocity);
    }

    public void moveElbow(double velocity, int position) {
        robot.elbow.setTargetPosition(position);
        robot.elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //robot.elbow.setPower(velocity);
        ((DcMotorEx) robot.elbow).setVelocity(velocity);
    }

    public double limit(double value, int max_num) {
        return Math.max(0, Math.min(value, max_num));
    }

    public void reset_encoders() {
        robot.armLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.armRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}



