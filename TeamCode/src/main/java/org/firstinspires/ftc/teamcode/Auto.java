package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Auto", group = "LinearOpMode") //Declares as autonomous file, SDK thing
public class Auto extends LinearOpMode {
    DrivingBot robot;

    int capPosition;
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() throws InterruptedException {
        robot = new DrivingBot();
        robot.init(hardwareMap);
        robot.moveRightTo(.1, 3);
        Thread.sleep(2000);
        robot.moveForwardTo(.1, 10);
    }
}
