package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp2", group = "LinearOpMode")
@Disabled
public class TeleOp2 extends LinearOpMode{
        DrivingBot robot;
        double max_arm_pow = 0.3;

@Override
public void runOpMode(){
        robot=new DrivingBot();
        robot.init(hardwareMap);

        telemetry.addData("Status","Initialized");
        telemetry.update();


        waitForStart();

        while(opModeIsActive()){
            // Read inverse IMU heading, as the IMU heading is CW positive
            double heading = -robot.imu.getAngularOrientation().firstAngle;

            double rotX = gamepad1.left_stick_x * Math.cos(heading) - (-gamepad1.left_stick_y) * Math.sin(heading);
            double rotY = gamepad1.left_stick_x * Math.sin(heading) + (-gamepad1.left_stick_y) * Math.cos(heading);

            double max = Math.max(Math.abs(-gamepad1.left_stick_y) + Math.abs(gamepad1.left_stick_x) + Math.abs(gamepad1.right_stick_x), 1);
            double frontLeftPower = (rotY + rotX + gamepad1.right_stick_x) / max;
            double backLeftPower = (rotY - rotX + gamepad1.right_stick_x) / max;
            double frontRightPower = (rotY - rotX - gamepad1.right_stick_x) / max;
            double backRightPower = (rotY + rotX - gamepad1.right_stick_x) / max;

            robot.FL.setPower(frontLeftPower);
            robot.BL.setPower(backLeftPower);
            robot.FR.setPower(frontRightPower);
            robot.BR.setPower(backRightPower);

            moveArm(gamepad2.right_stick_y);
            telemetry.addData("left arm", robot.armLeft.getCurrentPosition());
            telemetry.addData("right arm", robot.armRight.getCurrentPosition());


            /*double angle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            double r = limit(Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y), 1);
            double turn = gamepad1.right_stick_x;
            double max = Math.max(Math.max(Math.max(Math.abs(gamepad1.left_stick_x), Math.abs(gamepad1.left_stick_y))
                            , Math.abs(gamepad1.right_stick_x)), 1);

            robot.FR.setPower(0.8 * ((r * Math.sin(angle) - turn) / max));
            robot.BR.setPower(0.8 * ((r * Math.cos(angle) - turn) / max));
            robot.FL.setPower(0.8 * ((r * Math.cos(angle) + turn) / max));
            robot.BL.setPower(0.8 * ((r * Math.sin(angle) + turn) / max));

            robot.claw.setPower(gamepad1.right_trigger);
            robot.claw.setPower(-gamepad1.left_trigger);*/



        }
    }

    public void moveArm(double power){
        robot.armLeft.setPower(max_arm_pow * power);
        robot.armRight.setPower(max_arm_pow * power);
    }
    
}
