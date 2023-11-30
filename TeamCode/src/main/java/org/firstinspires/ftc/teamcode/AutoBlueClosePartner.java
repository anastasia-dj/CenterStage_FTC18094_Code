package org.firstinspires.ftc.teamcode;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Auto Blue Close Partner", group = "LinearOpMode")
//Declares as autonomous file, SDK thing
public class AutoBlueClosePartner extends Hardware {
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        stopMoving();

        // in milliseconds
        telemetry.addLine("Sleeping for a second");
        sleep(1000);
        waitForStart();

        // the sleep amount is how far it goes
        // for moving X, there's a minimum threshold for power in order to move
        // we had an 11 inches in one second with .3 power
        moveX(.3);
        sleep(500);

        telemetry.addLine("Moving Right");
        // we had 3.5 inches in one second with .1 power
        // close needs to move about 39 inches
        // 29 inches * (1second/3.5 inches) = 29/3.5 seconds = 8 3/7 seconds = 8.285714 seconds = 8285 ms
        // added 1.5 seconds for a buffer zone
        moveY(.5);
        sleep(1250);

        moveX(0.3);
        sleep(1650);
    }
}
