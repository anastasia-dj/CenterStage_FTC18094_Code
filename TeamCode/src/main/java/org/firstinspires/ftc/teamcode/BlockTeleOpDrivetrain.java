package org.firstinspires.ftc.teamcode;
//todo: why won't this file work? good back up if imu dies

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;


@TeleOp(name = "Manual Control")
public class BlockTeleOpDrivetrain extends Hardware {

    /**
     * This function is executed when this OpMode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {
        initHardware();

        waitForStart();
        float y;
        float x;
        float rx;

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                // Y stick is reversed
                y = -gamepad1.right_stick_y;
                x = gamepad1.right_stick_x;
                rx = -gamepad1.left_stick_x;
                RobotLog.v(String.valueOf(y));
                RobotLog.v(String.valueOf(x));
                RobotLog.v(String.valueOf(rx));

                // there's a divide by two in these in order to make the power smaller
                // if you want to make the power smaller, then make the '2' higher
                // if you want it stronger make it lower or remove the division by two
                frontLeft.setPower((y + x + rx) / 2);
                backLeft.setPower(((y - x) + rx) / 2);
                frontRight.setPower(((y - x) - rx) / 2);
                backRight.setPower(((y + x) - rx) / 2);


            }
        }
    }
}