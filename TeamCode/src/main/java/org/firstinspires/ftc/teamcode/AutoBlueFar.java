package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auto Blue Far", group = "LinearOpMode")
//Declares as autonomous file, SDK thing/
public class AutoBlueFar extends Hardware {
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        stopMoving();

        // in milliseconds
        telemetry.addLine("Sleeping for a second");
        sleep(1000);
        waitForStart();


        //Move to the side to go around the pole
        moveX(.4);
        sleep(273);

        telemetry.addLine("Moving Right");

        //Go straight
        moveY(.35);
        sleep(4000);
    }
}