package org.firstinspires.ftc.teamcode;



import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;



@TeleOp(name = "Manual Control")

public class BlockTeleOpDrivetrain extends LinearOpMode {



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