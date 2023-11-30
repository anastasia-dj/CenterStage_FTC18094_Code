package org.firstinspires.ftc.teamcode;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Auto Blue Far", group = "LinearOpMode")
//Declares as autonomous file, SDK thing
public class AutoBlueFar extends Hardware {
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        stopMoving();

        // in milliseconds
        telemetry.addLine("Sleeping for a second");
        sleep(1000);
        waitForStart();


        moveX(.3);
        sleep(400);

        telemetry.addLine("Moving Left");


        moveY(.35);
        sleep(4000);
    }
}