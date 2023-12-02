package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auto Red Close Partner", group = "LinearOpMode")
//Declares as autonomous file, SDK thing
public class AutoRedClosePartner extends Hardware {
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        stopMoving();

        // in milliseconds
        telemetry.addLine("Sleeping for a second");
        sleep(1000);
        waitForStart();


        moveX(-.3);
        sleep(273);

        telemetry.addLine("Moving Left");


        moveY(.5);
        sleep(1250);


        moveX(-.3);
        sleep(1650);
    }
}