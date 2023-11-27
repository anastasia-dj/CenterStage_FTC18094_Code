package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Manual Control CLICK THIS ONE CLICK THIS ONE CLICK THIS ONE")
public class BlockTeleOpDrivetrain extends Hardware {

    /**
     * This function is executed when this OpMode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {
        initHardware();
        waitForStart();
        float Y;
        float X;
        float RX;

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