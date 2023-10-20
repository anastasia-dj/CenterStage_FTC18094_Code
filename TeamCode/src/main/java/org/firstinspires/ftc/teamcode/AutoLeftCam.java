package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Left", group = "LinearOpMode")
public class AutoLeftCam extends LinearOpMode {
    DrivingBot robot;

    int capPosition;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new DrivingBot();
        robot.init(hardwareMap);
        telemetry.addData("arm", robot.armLeft.getCurrentPosition());

        //start camera
        robot.webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                robot.webcam.startStreaming(640, 360, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("camera", "failed");
                telemetry.update();
            }
        });




        // choose cap position during init
        while (!opModeIsActive()) {
            if (robot.pipeline.isCapLeft()) {
                capPosition = 2;
                telemetry.addData("Cone", "Orange");
            } else if (robot.pipeline.isCapCenter()) {
                capPosition = 1;
                telemetry.addData("Cone", "Green");
            } else {
                capPosition = 0;
                telemetry.addData("Cone", "Purple");
            }

            telemetry.update();
            if (isStopRequested())
                break;
        }




        robot.openClaw();

        telemetry.addData("Status", "Ready!");
        telemetry.update();


        waitForStart();
        robot.closeCameraAsync();

        while (opModeIsActive()) {
            switch (capPosition) {
                case 0: // Orange
                    telemetry.addData("Path", "Going Left");
                    telemetry.update();
                    goLeft();
                    break;
                case 1: // Green
                    goCenter();
                    telemetry.addData("Path", "Going Middle");
                    telemetry.update();
                    break;
                case 2: // Purple
                    goRight();
                    telemetry.addData("Path", "Going Right");
                    telemetry.update();
                    break;
            }

            break;

        }
        robot.stopMoving();

    }

    public void goLeft() { // ORANGE
        robot.closeClaw();
        sleep(2000);
        robot.moveForwardTo(0.4, 19);
        mediumArm();
        sleep(1000);
        //lowArm();
        robot.moveRightTo(0.3, 9.5);
        robot.moveForwardTo(0.2, 2.7);
        //robot.moveForwardTo(0.4, 10);
        sleep(1000);
        robot.openClaw();
        sleep(2000);
        robot.moveBackwardTo(0.2, 2);
        sleep(1000);
        robot.moveLeftTo(0.3, 14);
        restArm();
        sleep(600);
        robot.moveBackwardTo(0.2, 1);
        robot.moveLeftTo(0.4, 15);
        restArm();
        sleep(2000);
    }

    public void goCenter() { // GREEN
        robot.closeClaw();
        sleep(2000);
        robot.moveForwardTo(0.4, 19);
        mediumArm();
        sleep(1000);
        //lowArm();
        robot.moveRightTo(0.3, 9.5);
        robot.moveForwardTo(0.2, 2.6);
        //robot.moveForwardTo(0.4, 10);
        sleep(1000);
        robot.openClaw();
        sleep(2000);
        robot.moveBackwardTo(0.2, 2);
        sleep(1000);
        robot.moveLeftTo(0.3, 10);
        robot.moveBackwardTo(0.2, 2.5);
        restArm();
        sleep(2000);
    }

    public void goRight() { // PURPLE
        robot.closeClaw();
        sleep(2000);
        robot.moveForwardTo(0.4, 19);
        mediumArm();
        sleep(1000);
        //lowArm();
        robot.moveRightTo(0.3, 9.5);
        robot.moveForwardTo(0.2, 2.7);
        //robot.moveForwardTo(0.4, 10);
        sleep(1000);
        robot.openClaw();
        sleep(2000);
        robot.moveBackwardTo(0.2, 2);
        robot.moveRightTo(0.3, 9);
        restArm();
        sleep(2000);
    }


    public void lowArm() {
        moveElbow(900, 220);
        moveArm(300, 0);
    }

    public void mediumArm() {
        moveElbow(700, 365);
        sleep(300);
        moveArm(800, 265);
    }

    public void highArm() {
        moveElbow(800, 400);
        sleep(200);
        moveArm(700,400);
        moveElbow(700, 540);
    }

    public void restArm() {
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

}
