package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Auto", group = "LinearOpMode") //Declares as autonomous file, SDK thing
public class Auto extends LinearOpMode {
    
    private DcMotor BL;

    private DcMotor FL;

    private DcMotor FR;

    private DcMotor BR;


    // these two are unused
    int capPosition;
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() throws InterruptedException {
            FR = hardwareMap.get(DcMotor.class, "frontRight");
            FL = hardwareMap.get(DcMotor.class, "frontLeft");
            BR = hardwareMap.get(DcMotor.class, "backRight");
            BL = hardwareMap.get(DcMotor.class, "backLeft");

            backLeft.setDirection(DcMotor.Direction.REVERSE);
            stopMoving();


            // in milliseconds
            telemetry.addLine("Sleeping for a second");
            sleep(1000);

            telemetry.addLine("Moving Right");
            moveX(.1);
            sleep(500);

            telemetry.addLine("Moving Left")
            moveY(.1);
            sleep(500);




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

    public static void sleep(long ms){
        Thread.sleep(ms);
    }
    
}
