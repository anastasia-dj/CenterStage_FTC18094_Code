package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;



@TeleOp(name = "Manual Control CLICK THIS ONE CLICK THIS ONE CLICK THIS ONE")

public class BlockTeleOpDrivetrain extends LinearOpMode {



    BNO055IMU imu;

    BNO055IMU.Parameters parameters = null;

    private DcMotor backLeft;

    private DcMotor frontLeft;

    private DcMotor frontRight;

    private DcMotor backRight;



    /**

     * This function is executed when this OpMode is selected from the Driver Station.

     */

    @Override

    public void runOpMode() {

        float Y;

        float X;

        float RX;



        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        telemetry.addLine("test");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");

        frontRight = hardwareMap.get(DcMotor.class, "frontRight");

        backRight = hardwareMap.get(DcMotor.class, "backRight");



        // Put initialization blocks here.

        backLeft.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        if (opModeIsActive()) {

            // Put run blocks here.

            while (opModeIsActive()) {

                // Y stick is reversed

                Y = -gamepad1.right_stick_y;

                X = gamepad1.right_stick_x;

                RX = -gamepad1.left_stick_x;

                // there's a divide by two in these in order to make the power smaller
                // if you want to make the power smaller, then make the '2' higher
                // if you want it stronger make it lower or remove the division by two
                frontLeft.setPower((Y + X + RX) / 2);

                backLeft.setPower(((Y - X) + RX) / 2);

                frontRight.setPower(((Y - X) - RX) / 2);

                backRight.setPower(((Y + X) - RX) / 2);

                telemetry.update();

            }

        }

    }

}