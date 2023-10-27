
//This whole file is camera stuff, and we don't have a camera yet. I highly doubt having this code will interfere with
//anything but just thought to comment it out :)

/*package org.firstinspires.ftc.teamcode;
//Literally just testing a camera
//Kinda unused
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name = "CameraTest", group = "LinearOpMode")
//@Disabled
public class TestCam extends LinearOpMode {
    // Declare OpMode members.
    DrivingBot robot;
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {
        robot = new DrivingBot();
        robot.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

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

        telemetry.addData("Status", "camera started!");

        while (!opModeIsActive()) {
            telemetry.addData("avg", robot.pipeline.findAvg());
            telemetry.update();

            if (isStopRequested()) {
                break;
            }
        }
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        robot.closeCameraAsync();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.addData("avg", robot.pipeline.findAvg());
            telemetry.update();

        }

        // robot.webcam.closeCameraDevice();
    }
}


 */