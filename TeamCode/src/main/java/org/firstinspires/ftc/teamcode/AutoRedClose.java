package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auto Red Close", group = "LinearOpMode")
//Declares as autonomous file, SDK thing
public class AutoRedClose extends Hardware {
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        stopMoving();

        // in milliseconds
        telemetry.addLine("Sleeping for a second");
        sleep(1000);
        waitForStart();


        moveX(-.3);
        sleep(500);

        telemetry.addLine("Moving Left");


        moveY(.5);
        sleep(1250);
    }
}