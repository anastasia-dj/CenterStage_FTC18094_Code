package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auto Red Far", group = "LinearOpMode")
//Declares as autonomous file, SDK thing
public class AutoRedFar extends Hardware {
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        stopMoving();

        // in milliseconds
        telemetry.addLine("Sleeping for a second");
        sleep(1000);
        waitForStart();


        moveX(-.4);
        sleep(273);

        telemetry.addLine("Moving Left");


        time = 22000;


        moveY(.35);
        sleep(4000);


//        for (int i = 1; i < 22000 / 10000; i++) {
//            moveY(.35);
//            sleep(4000);
//            moveX(-.3);
//           sleep(273);
//        }

//        for (int i = 1; i < 22000 / 6000; i++) {
//            moveY(.3);
//            sleep(4000);
//            moveX(-.3);
//            sleep(273);
//        }
    }
}