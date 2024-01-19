package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auto Blue Close", group = "LinearOpMode")
//Declares as autonomous file, SDK thing
public class AutoBlueClose extends Hardware {
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        stopMoving();

        // in milliseconds
        telemetry.addLine("Sleeping for a second");
        sleep(1000);
        waitForStart();

        //Move to the side to go around the pole
        moveX(.3);
        sleep(500);

        telemetry.addLine("Moving Right");

        //Go straight/
        moveY(.5);
        sleep(1250);
    }
}
