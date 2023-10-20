package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Auto", group = "LinearOpMode")
@Disabled
public class Auto extends LinearOpMode {
    DrivingBot robot;

    int capPosition;
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() throws InterruptedException {
        robot = new DrivingBot();
        robot.init(hardwareMap);
        telemetry.addData("arm", robot.armLeft.getCurrentPosition());
/*
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


        //choose cap position during init
        while (!opModeIsActive()) {
            if (robot.pipeline.isCapLeft()) {
                capPosition = 0;
                telemetry.addData("cap", 0);
            } else if (robot.pipeline.isCapCenter()) {
                capPosition = 1;
                telemetry.addData("cap", 1);
            } else {
                capPosition = 2;
                telemetry.addData("cap", 2);
            }

            telemetry.update();
            if (isStopRequested())
                break;
        }
*/
        telemetry.addData("Status", "Ready!");
        telemetry.update();


        waitForStart();

        while (opModeIsActive()) {

            moveElbow(0.6, 0);
            moveArm(0.5, -83);
            sleep(500);
            moveElbow(0.5, 150);
            sleep(700);
            moveArm(0.5, 70);
            moveElbow(0.7, 700);
            sleep(700);
            moveElbow(0.7, 1400);
            moveArm(0.3, 100);
            moveElbow(0.4, 1150);
            sleep(5000);
            robot.moveForwardTo(0.5, 22);
            sleep(3000);
            break;
        }
        robot.stopMoving();

    }

    public void goLeft() {
        robot.moveForwardTo(0.4, 3);
        robot.moveLeftTo(0.4, 20);
        robot.moveForwardTo(0.4, 20);
        moveElbow(0.3, 0);
        moveArm(0.3, 100);
        sleep(2000);
        moveElbow(0.3, 460);
        sleep(3000);
        moveArm(0.3, 18);
            }

    public void goCenter() {
        robot.moveForwardTo(0.4, 25);
        moveElbow(0.3, 0);
        moveArm(0.3, 100);
        sleep(2000);
        moveElbow(0.3, 460);
        sleep(3000);
        moveArm(0.3, 18);
    }

    public void goRight() {
        robot.moveForwardTo(0.4, 3);
        robot.moveRightTo(0.4, 20);
        robot.moveForwardTo(0.4, 20);
        moveElbow(0.3, 0);
        moveArm(0.3, 100);
        sleep(2000);
        moveElbow(0.3, 460);
        sleep(3000);
        moveArm(0.3, 18);
    }


    public void moveArm(double power, int position) {
        robot.armLeft.setTargetPosition(position);
        robot.armRight.setTargetPosition(position);
        robot.armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.armLeft.setPower(power * 0.3);
        robot.armRight.setPower(power * 0.3);
    }

    public void moveElbow(double power, int position) {
        robot.elbow.setTargetPosition(position);
        robot.elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.elbow.setPower(power * 0.3);
    }
}
