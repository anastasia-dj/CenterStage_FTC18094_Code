package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

// This is a single line comment

/*
   This is a
   multi-line comment
*/

public class DrivingBot {
    // Public OpMode members
    public DcMotor FR;
    public DcMotor FL;
    public DcMotor BR;
    public DcMotor BL;
    //public DcMotor arm;
    //public DcMotor wrist;

    /*public DcMotor armLeft;
    public DcMotor armRight;
    public DcMotor elbow;
    //public DcMotor claw;*/
    //
    public BNO055IMU imu;
    //check whether we have orientation sensor for robot
    //public Servo claw;

   // public WebcamName camera; // CAMERA STUFF 1
   // OpenCvCamera webcam; // CAMERA STUFF 2

    static final double COUNTS_PER_MOTOR_REV = 560;
    static final double DRIVE_GEAR_REDUCTION = 1;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 3.543;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    //CapDeterminationPipeline pipeline; // CAMERA STUFF 3


    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    private boolean FRMoving = false;
    private boolean FLMoving = false;
    private boolean BRMoving = false;
    private boolean BLMoving = false;

    /* Constructor */
    public DrivingBot() {
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        FR = hwMap.dcMotor.get("FR");
        FL = hwMap.dcMotor.get("FL");
        BR = hwMap.dcMotor.get("BR");
        BL = hwMap.dcMotor.get("BL");
      //  arm = hwMap.dcMotor.get("arm");
      //  wrist = hwMap.dcMotor.get("claw");

        /*armLeft = hwMap.dcMotor.get("arm left");
        armRight = hwMap.dcMotor.get("arm right");
        elbow = hwMap.dcMotor.get("elbow");*/

        // set motors' direction
        FL.setDirection(DcMotor.Direction.FORWARD);
        FR.setDirection(DcMotor.Direction.REVERSE);
        BL.setDirection(DcMotor.Direction.FORWARD);
        BR.setDirection(DcMotor.Direction.REVERSE);

        //arm.setDirection(DcMotor.Direction.REVERSE);
        //wrist.setDirection(DcMotor.Direction.FORWARD);
        //Todo: Add correct motion directions for motors!!!!

        /*armRight.setDirection(DcMotor.Direction.FORWARD);
        armLeft.setDirection(DcMotor.Direction.REVERSE);
        elbow.setDirection(DcMotor.Direction.FORWARD);*/



        // set all motors to zero power
        FR.setPower(0);
        FL.setPower(0);
        BR.setPower(0);
        BL.setPower(0);
        //arm.setPower(0);
        //wrist.setPower(0);
        /*armLeft.setPower(0);
        armRight.setPower(0);
        elbow.setPower(0);*/

        //claw = hwMap.servo.get("claw");

        FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

       /// arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        /// wrist.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        /*armLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);*/

        /*armLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/








        // Define and initialize ALL installed servos.

        imu = hwMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);

        // CAMERA STUFF 4 (lines 119 - 126)
        // Set sensors
        /*camera = hwMap.get(WebcamName.class, "camera");

        // camera set-up
        int cameraMonitorViewId = hwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(camera, cameraMonitorViewId);
        pipeline = new CapDeterminationPipeline();
        webcam.setPipeline(pipeline);*/



    }

    //stop drive motors
    public void stopMoving() {
        FR.setPower(0);
        FL.setPower(0);
        BR.setPower(0);
        BL.setPower(0);
    }

    public void moveBackwardTo(double power, double inches) {
        int position = -(int) (inches * COUNTS_PER_INCH);
        setMotorPosition(FL, position);
        setMotorPosition(FR, position);
        setMotorPosition(BL, position);
        setMotorPosition(BR, position);
        startMovingToPosition(power);
    }

    public void moveForwardTo(double power, double inches) {
        int position = (int) (inches * COUNTS_PER_INCH);
        setMotorPosition(FL, position);
        setMotorPosition(FR, position);
        setMotorPosition(BL, position);
        setMotorPosition(BR, position);
        startMovingToPosition(power);
    }

    public void spinLeft(double power, double inches) {
        int position = inchesToPosition(inches);
        setMotorPosition(FL, -position);
        setMotorPosition(FR, position);
        setMotorPosition(BL, -position);
        setMotorPosition(BR, position);
        startMovingToPosition(power);
    }

    public void spinRight(double power, double inches) {
        int position = inchesToPosition(inches);
        setMotorPosition(FL, position);
        setMotorPosition(FR, -position);
        setMotorPosition(BL, position);
        setMotorPosition(BR, -position);
        startMovingToPosition(power);
    }

    public void moveRightTo(double power, double inches) {
        int position = inchesToPosition(inches);
        setMotorPosition(FL, position);
        setMotorPosition(FR, -position);
        setMotorPosition(BL, -position);
        setMotorPosition(BR, position);
        startMovingToPosition(power);
    }

    public void moveLeftTo(double power, double inches) {
        int position = inchesToPosition(inches);
        setMotorPosition(FL, -position);
        setMotorPosition(FR, position);
        setMotorPosition(BL, position);
        setMotorPosition(BR, -position);
        startMovingToPosition(power);
    }

    private void startMovingToPosition(double pow) {
        setMotorsMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(pow);
        FLMoving = true;
        FRMoving = true;
        BLMoving = true;
        BRMoving = true;
        while (isRobotMoving()) { }
        setPower(0);
    }

    private void setMotorPosition(DcMotor motor, int position) {
        motor.setTargetPosition(motor.getCurrentPosition() + position);
    }

    public void setMotorsMode(DcMotor.RunMode mode) {
        FR.setMode(mode);
        FL.setMode(mode);
        BR.setMode(mode);
        BL.setMode(mode);
    }


    /*      public void closeClaw() {
        claw.setPosition(0.4);
    }

    public void openClaw() {
        claw.setPosition(0);
    }*/

    //When attactching servo, make sure to reset it so that it starts at 0
    //Setsposition just moves it from position
    //1 == 360
    //Motors you can start position and set it to zero, not completely set like how servo is

    public boolean isRobotMoving() {
        if (FLMoving)
            FLMoving = !isAtTargetPosition(FL.getCurrentPosition(), FL.getTargetPosition());
        if (FRMoving)
            FRMoving = !isAtTargetPosition(FR.getCurrentPosition(), FR.getTargetPosition());
        if (BLMoving)
            BLMoving = !isAtTargetPosition(BL.getCurrentPosition(), BL.getTargetPosition());
        if (BRMoving)
            BRMoving = !isAtTargetPosition(BR.getCurrentPosition(), BR.getTargetPosition());

        return FLMoving || FRMoving || BLMoving || BRMoving;
    }

    private boolean isAtTargetPosition(int current, int target) {
        return Math.abs(Math.abs(current) - Math.abs(target)) < 1.2;
    }

    private int inchesToPosition(double inches) {
        return (int) (inches * COUNTS_PER_INCH);
    }

    public void setPower(double power) {
        FL.setPower(power);
        BL.setPower(power);
        FR.setPower(power);
        BR.setPower(power);
    }


   /* public void moveArm(double velocity, int position) {
        arm.setTargetPosition(position);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ((DcMotorEx) arm).setVelocity(velocity);
    }

    public void moveWrist(double velocity, int position) {
        wrist.setTargetPosition(position);
        wrist.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //robot.elbow.setPower(velocity);
        ((DcMotorEx) wrist).setVelocity(velocity);
    }

    // CAMERA STUFF 5 (lines 278 - 285)
    /*public void closeCameraAsync() {
        webcam.closeCameraDeviceAsync(new OpenCvCamera.AsyncCameraCloseListener() {
            //when the camera opens, make sure some parts are opened and initialized
            //general camera thing
            @Override
            public void onClose() {

            }
        });
    }*/


}
