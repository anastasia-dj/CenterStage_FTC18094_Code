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


        moveX(.3);
        sleep(500);

        telemetry.addLine("Moving Right");


        moveY(.5);
        sleep(1250);

        moveX(0.3);
        sleep(1650);
    }
}
