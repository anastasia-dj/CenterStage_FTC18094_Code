package org.firstinspires.ftc.teamcode;
//Initialization is useful for camera
//init not for movement??/
//setting camera u can reuse
//colors u'll have to look more into ftc machine learning
//sensors different (duh)
//look into sensors
//driving bot file
//where u initialize everything ^^^^^ (might have to change a few names, servos, etc etc etc) <- template stays the same
//functions at end of file that are like general move forward close claw
//Framework for teleop and auto are basically the same
    //Teleop: keep drivetrain
    //Auto: will have to be changed
//Only drive train should really be the same
//Try not to reuse arm (IT WAS A MESSSSSS)
//Scissor lift will probs have motor going clockwise or counterclockwise
    //Elbow??? move at the same time????? how will this work??/
    //Elbow + scissor lift at same time = angle
//Claw will be the same

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;
//Commented both import statements out because android studio was mad at me for not using it

import java.security.KeyStore;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "LinearOpMode")
@SuppressWarnings("unused")
public class TeleOp1 extends LinearOpMode {
    DrivingBot robot;
    double max_pow = 0.6;
    //Sets max_pow

    @Override
    public void runOpMode() {
        robot = new DrivingBot();
        robot.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
        //Tells monitor "we're startingggggg"
        telemetry.update();
        //Updates the monitor so it shows

        //Todo: check this with the actual robot
        robot.claw.setPosition(0.3);
        //Move claw to lowest position??

        waitForStart();
        //Wait for start to be clicked on montior


        while (opModeIsActive() && !isStopRequested()) { // reset arm from init
            telemetry.addData("Status", "opMode");
            telemetry.addData("claw", robot.claw.getPosition());
            telemetry.addData("arm v", ((DcMotorEx) robot.wrist).getVelocity());
            telemetry.addData("left arm", robot.arm.getCurrentPosition());
            telemetry.addData("max power", max_pow);
            telemetry.update();

            // ARM
            /*
                rest/intake: elbow = 0, arm = 0
                low goal: elbow = 190, arm = 0
                medium goal: elbow = 315, arm = 250
                high goal: elbow = 460, arm = 360
             */
            /*if (gamepad2.x) { // if x is pressed... rest/intake
                if (robot.armLeft.getCurrentPosition() <= 260) { // move to rest from low arm position
                    moveArm(300,0);
                    moveElbow(230, 0);
                } else if((robot.armLeft.getCurrentPosition() >= 260) & (robot.armLeft.getCurrentPosition() <= 310)) { // move to rest from medium arm position
                    moveArm(400,200);
                    moveElbow(300, 300);
                    sleep(400);
                    moveElbow(250, 0);
                    moveArm(250, 0);
                } else { // move to rest from high arm position
                    //low & medium r already defined so this is the last one to be defined
                    moveArm(400,200);
                    moveElbow(300, 300);
                    sleep(600);
                    moveElbow(300, 0);
                    moveArm(250, 0);

                }

             */
             /*   robot.armLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT); //When motor is off, arm can freely move
                robot.armRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT); // for this too
                robot.elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT); // and this

            } else if (gamepad2.a) { // if a is pressed... move to low goal
                moveElbow(900, 220);
                moveArm(300, 0);
            } else if (gamepad2.b) { // move to medium goal
                if (robot.elbow.getCurrentPosition() >= 150) { //if current position is under 150
                    moveArm(800, 265);
                    sleep(300);
                    moveElbow(700, 365);
                } else { //if current position is *above* 150, so its pretty high
                    moveElbow(700, 365);
                    sleep(300);
                    moveArm(800, 265);
                }
            } else if (gamepad2.y) { // high goal
                if (robot.elbow.getCurrentPosition() >= 150) { // if current position is under 150
                    moveArm(700,365);
                    sleep(300);
                    moveElbow(700, 490);
                } else { // if current position is above 150
                    moveElbow(800, 410);
                    sleep(200);
                    moveArm(700,365);
                    moveElbow(700, 490);
                }
            }
            */
            // ADJUST



            /*if (gamepad2.dpad_right) { //if u press the right arrow (dpad), move arm right
                moveArm(700, robot.armRight.getCurrentPosition() + 15);
            } else if (gamepad2.dpad_left) { // if u press the left arrow (dpad), move arm left
                moveArm(700, robot.armRight.getCurrentPosition() - 15);
            }

            if (gamepad2.dpad_up) { // if u press the up arrow (dpad), move arm up
                moveElbow(600, robot.elbow.getCurrentPosition() + 10);
            } else if (gamepad2.dpad_down) { // if u press the down arrow (dpad), move arm down
                moveElbow(700, robot.elbow.getCurrentPosition() - 10);
            }

            // CLAW: we might want to edit this part?? HW: look into claw
            if (gamepad2.left_bumper) { // if left bumper is pressed close claw
                robot.claw.setPosition(0.5);
            } else if (gamepad2.right_bumper) { // if right bumper is pressed open claw
                robot.claw.setPosition(0);
            } */




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


            // MAIN DRIVE TRAIN HEREEEEEEE!!!!!

            if ((gamepad1.right_stick_y < -0.3 || gamepad1.right_stick_y > 0.3) || (gamepad1.left_stick_y < -0.3 || gamepad1.left_stick_y > 0.3)) {
                robot.FR.setPower(gamepad1.right_stick_y * -max_pow);
                robot.BR.setPower(gamepad1.right_stick_y * -max_pow);
                //if right stick is pulled down, the Front and Back wheels spin backwards
                //if right stick is pulled up, the Front and Back wheels spin forwards

                robot.FL.setPower(gamepad1.left_stick_y * -max_pow);
                robot.BL.setPower(gamepad1.left_stick_y * -max_pow);
                //if left stick is pulled down, the Front and Back wheels spin backwards
                //if left stick is pulled up, the Front and Back wheels spin forwards

            } else if (gamepad1.right_bumper) {
                robot.FR.setPower(-0.5);
                robot.BL.setPower(-0.5);
                robot.FL.setPower(0.5);
                robot.BR.setPower(0.5);
                //if right bumper is pressed, turn clockwise

            } else if (gamepad1.left_bumper) {
                robot.FL.setPower(-0.5);
                robot.BR.setPower(-0.5);
                robot.FR.setPower(0.5);
                robot.BL.setPower(0.5);
                //if left bumper is pressed, turn anticlockwise

            } else {
                robot.setPower(0);
                //if nothing is pressed, don't move
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
        robot.arm.setTargetPosition(position); //hey arm, "position" is where you want to go
        robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION); //CHAAAARRRGEEE ARMM!!!! (go to the set target position)
        ((DcMotorEx) robot.arm).setVelocity(velocity); //sets the velocity of the arm
    }

    public void moveWrist(double velocity, int position) {
        robot.wrist.setTargetPosition(position); //hey elbow, "position" is where you want to go
        robot.wrist.setMode(DcMotor.RunMode.RUN_TO_POSITION); //CHAAARRRGGEEE ELBOWW!!! (go to the set target position)
        //robot.elbow.setPower(velocity);
        ((DcMotorEx) robot.wrist).setVelocity(velocity); //sets the velocity of the elbow
    }



    @SuppressWarnings("unused")
    public double limit(double value, int max_num) {
        return Math.max(0, Math.min(value, max_num));
    }
    //It takes two numbers and finds the smaller of the two (Math.min)
    //if the num is neg, return 0 (else return the number)
    //probably experiment, test out calculate motor movement (used limit as var, didn't end up using it)
    //probably dont need this
    //@SuppressWarnings("unused")

    public void reset_encoders() {
        robot.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //remove power from the motor
        robot.wrist.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //remove power from the motor
    }
}



