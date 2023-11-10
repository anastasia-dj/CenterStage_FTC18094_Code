package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Auto Red Far", group = "LinearOpMode") //Declares as autonomous file, SDK thing
public class AutoRedFar extends LinearOpMode {

    private static DcMotor BL;

    private static DcMotor FL;

    private static DcMotor FR;

    private static DcMotor BR;


    // these two are unused
    int capPosition;
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() throws InterruptedException {

        FR = hardwareMap.get(DcMotor.class, "frontRight");
        FL = hardwareMap.get(DcMotor.class, "frontLeft");
        BR = hardwareMap.get(DcMotor.class, "backRight");
        BL = hardwareMap.get(DcMotor.class, "backLeft");


        // For future generations: make sure your direction's specifically for YOUR motors
        BL.setDirection(DcMotor.Direction.REVERSE);
        stopMoving();


        // in milliseconds
        telemetry.addLine("Sleeping for a second");
        sleep(1000);
        waitForStart();

        // the sleep amount is how far it goes
        // for moving X, there's a minimum threshold for power in order to move
        // we had an 11 inches in one second with .3 power
        moveX(-.3);
        sleep(273);

        telemetry.addLine("Moving Left");
        // we had 3.5 inches in one second with .1 power
        // close needs to move about 39 inches
        // 29 inches * (1second/3.5 inches) = 29/3.5 seconds = 8 3/7 seconds = 8.285714 seconds = 8285 ms
        // added 1.5 seconds for a buffer zone
        moveY(.1);
        sleep(16660);


    }


    public static void stopMoving(){
        FR.setPower(0);
        FL.setPower(0);
        BR.setPower(0);
        BL.setPower(0);
    }


    // We can have the go backwards be negative power
    // forward -> use positive power
    // backward -> use negative power
    public static void moveY(double power){
        FR.setPower(power);
        FL.setPower(power);
        BR.setPower(power);
        BL.setPower(power);
    }

    // its the same thing as moveY. Positive power is going to be right and negative is left
    // right -> postive power
    // left -> negative power
    public static void moveX(double power){
        FR.setPower(-power);
        FL.setPower(power);
        BR.setPower(power);
        BL.setPower(-power);
    }


}