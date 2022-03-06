package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name = "BlueLeftAuto")
public class BlueLeftAuto extends LinearOpMode {

    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;
    public DcMotor spinner;
    public DcMotor arm;
    public Servo boxServo;


    BNO055IMU imu;
    Orientation lastAngles = new Orientation();
    double globalAngle;


    private ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 537.6;
    static final double DRIVE_GEAR_REDUCTION = 1.23;
    static final double WHEEL_DIAMETER_INCHES = 4.724;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.14159265);
    static final double ARM_WHEEL_REDUCTION = 5.0;
    static final double ARM_RATIO = 1.0;
    static final double ARM_WHEEL_DIAMETER_INCHES = 1.9;
    static final double ARM_PER_INCH = (COUNTS_PER_MOTOR_REV * ARM_WHEEL_REDUCTION * ARM_RATIO) /
            (ARM_WHEEL_DIAMETER_INCHES * 3.14159265);


    static final double OPEN = 0.44;
    static final double MIDDLE = 0.55;
    static final double CLOSE = 1.0;


    @Override
    public void runOpMode() throws InterruptedException {

        //Initialize the IMU and its parameters.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);


        BarcodeUtil detector = new BarcodeUtil(hardwareMap, "webcam", telemetry);
        detector.init();

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        spinner = hardwareMap.dcMotor.get("spinner");
        arm = hardwareMap.dcMotor.get("arm");
        boxServo = hardwareMap.get(Servo.class, "boxServo");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        spinner.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //The IMU does not initialize instantly. This makes it so the driver can see when they can push Play without errors.
        telemetry.addData("Mode", "calibrating...");
        telemetry.update();
        while (!isStopRequested() && !imu.isGyroCalibrated()) {
            sleep(50);
            idle();
        }


        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spinner.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        spinner.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Path0", "Starting at %7d :%7d",
                frontLeft.getCurrentPosition(),
                frontRight.getCurrentPosition(),
                backLeft.getCurrentPosition(),
                backRight.getCurrentPosition());
        telemetry.update();

        BarcodePositionDetector.BarcodePosition bP = detector.getBarcodePosition();

        //Tells the driver it is ok to start.
        telemetry.addData("Mode", "waiting for start");
        telemetry.addData("imu calib status", imu.getCalibrationStatus().toString());
        telemetry.update();

        waitForStart();

        //actual code under
        if (bP == BarcodePositionDetector.BarcodePosition.LEFT) {

            armVisionEncoder(0.35, BarcodePositionDetector.BarcodePosition.LEFT, 2.0);
            armVisionEncoder(0.35, BarcodePositionDetector.BarcodePosition.LEFT, 2.0);
            armVisionEncoderInches(-.35, -1, 2.0);

            arm.setPower(0.015);

            armVisionEncoder(0.35, BarcodePositionDetector.BarcodePosition.LEFT, 2.0);
            armVisionEncoder(0.35, BarcodePositionDetector.BarcodePosition.LEFT, 2.0);
            armVisionEncoderInches(-.35, 2.2, 2.0);

            arm.setPower(0.015);

            encoderDrive(0.5, -35, -35, 2.0);
            rotate(-60, .5);
            encoderDrive(0.5, -14, -14, 2.0);
            encoderDrive(0.3, -1, -1, 2.0);

            boxServo.setPosition(CLOSE);

            sleep(1000);

            encoderDrive(0.5, 5, 5, 2);

            rotate(-45, 0.5);

            encoderDrive(0.5, 20, 20, 2);

            rotate(-55, 0.5);

            encoderDrive(0.5, 30, 30, 2);

        } else if (bP == BarcodePositionDetector.BarcodePosition.MIDDLE) {

            armVisionEncoder(0.35, BarcodePositionDetector.BarcodePosition.LEFT, 2.0);
            armVisionEncoder(0.35, BarcodePositionDetector.BarcodePosition.LEFT, 2.0);
            armVisionEncoderInches(-.35, 1.5, 2.0);
            arm.setPower(0.015);

            armVisionEncoder(0.35, BarcodePositionDetector.BarcodePosition.LEFT, 2.0);
            armVisionEncoder(0.35, BarcodePositionDetector.BarcodePosition.LEFT, 2.0);
            armVisionEncoderInches(-.35, 2.2, 2.0);

            arm.setPower(0.015);

            encoderDrive(0.5, -35, -35, 2.0);
            rotate(-60, .5);
            encoderDrive(0.5, -14, -14, 2.0);
            encoderDrive(0.3, -1, -1, 2.0);

            boxServo.setPosition(CLOSE);

            sleep(1000);

            encoderDrive(0.5, 5, 5, 2);

            rotate(-45, 0.5);

            encoderDrive(0.5, 20, 20, 2);

            rotate(-55, 0.5);

            encoderDrive(0.5, 30, 30, 2);
            /**
             armVisionEncoderInches(0.5, -3, 2);
             boxServo.setPosition(MIDDLE);

             arm.setPower(-0.015);

             encoderDrive(0.5, -6, -6, 2.0);
             rotate(-45, 5);

             armVisionEncoder(0.5, BarcodePositionDetector.BarcodePosition.MIDDLE, 2.0);
             boxServo.setPosition(CLOSE);
             rotate(135, .5);

             encoderDrive(0.5, 24, 24, 2.0);
             encoderDrive(0.2, 4, 4, 2.0);

             duckByTime(-.55, 5000);
             **/

        } else if (bP == BarcodePositionDetector.BarcodePosition.RIGHT) {

            armVisionEncoder(0.35, BarcodePositionDetector.BarcodePosition.LEFT, 2.0);
            armVisionEncoder(0.35, BarcodePositionDetector.BarcodePosition.LEFT, 2.0);
            armVisionEncoderInches(-.35, 2.2, 2.0);

            arm.setPower(0.015);

            encoderDrive(0.5, -35, -35, 2.0);
            rotate(-60, .5);
            encoderDrive(0.5, -14, -14, 2.0);
            encoderDrive(0.3, -1, -1, 2.0);

            boxServo.setPosition(CLOSE);

            sleep(1000);

            encoderDrive(0.5, 5, 5, 2);

            rotate(-45, 0.5);

            encoderDrive(0.5, 20, 20, 2);

            rotate(-55, 0.5);

            encoderDrive(0.5, 30, 30, 2);

        }

        telemetry.addData("Path", "Complete");
        telemetry.update();

    }

    //Method for driving with encoder
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = frontLeft.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newLeftTarget = backLeft.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTarget = frontRight.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            newRightTarget = backRight.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            frontLeft.setTargetPosition(newLeftTarget);
            frontRight.setTargetPosition(newRightTarget);
            backLeft.setTargetPosition(newLeftTarget);
            backRight.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            frontLeft.setPower(Math.abs(speed));
            frontRight.setPower(Math.abs(speed));
            backLeft.setPower(Math.abs(speed));
            backRight.setPower(Math.abs(speed));


            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        frontLeft.getCurrentPosition(),
                        frontRight.getCurrentPosition(),
                        backLeft.getCurrentPosition(),
                        backRight.getCurrentPosition());
                telemetry.update();
            }


            // Stop all motion;
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            // Turn off RUN_TO_POSITION
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }


    }

    //This method reads the IMU getting the angle. It automatically adjusts the angle so that it is between -180 and +180.
    public double getAngle() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }

    //The method turns the robot by a specific angle, -180 to +180.
    public void rotate(int degrees, double power) {
        double leftPower, rightPower;
        if (degrees > 0) {

            degrees -= 16;

        } else if (degrees < 0) {

            degrees += 16;

        }

        resetAngle();

        //if the degrees are less than 0, the robot will turn right
        if (degrees < 0) {
            leftPower = power;
            rightPower = -power;
        } else if (degrees > 0)//if greater than 0, turn left
        {
            leftPower = -power;
            rightPower = power;
        } else return;

        //sets power to motors with negative signs properly assigned to make the robot go in the correct direction
        frontLeft.setPower(rightPower);
        backLeft.setPower(rightPower);
        frontRight.setPower(leftPower);
        backRight.setPower(leftPower);

        //Repeatedly check the IMU until the getAngle() function returns the value specified.
        if (degrees < 0) {
            while (opModeIsActive() && getAngle() == 0) {
            }

            while (opModeIsActive() && getAngle() > degrees) {
            }
        } else
            while (opModeIsActive() && getAngle() < degrees) {
            }


        //stop the motors after the angle has been found.

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        //sleep for a bit to make sure the robot doesn't over shoot
        sleep(1000);

        resetAngle();
    }


    //this method resets the angle so that the robot's heading is now 0
    public void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        globalAngle = 0;
    }

    public void moveDuck(double power) {

        int target;

        if (opModeIsActive()) {

            target = spinner.getCurrentPosition() + (int) (5.25 * COUNTS_PER_INCH);

            spinner.setTargetPosition(target);

            spinner.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();

            spinner.setPower(Math.abs(power));

            spinner.setPower(0);

            spinner.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

    }

    public void duckByTime (double power, long time){

        spinner.setPower(power);

        sleep(time);

        spinner.setPower(0);
    }

    public void armVisionEncoder(double speed, BarcodePositionDetector.BarcodePosition bP, double timeoutS) {
        int newTarget;
        double inches = 0;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            //barcode check command for height
            if (bP == BarcodePositionDetector.BarcodePosition.LEFT) {
                inches = -10;
            } else if (bP == BarcodePositionDetector.BarcodePosition.MIDDLE) {
                inches = -5.0;

            } else if (bP == BarcodePositionDetector.BarcodePosition.RIGHT) {
                inches = -8.0;

            }


            // Determine new target position, and pass to motor controller
            newTarget = arm.getCurrentPosition() + (int) (inches * ARM_PER_INCH);

            arm.setTargetPosition(newTarget);

            // Turn On RUN_TO_POSITION
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            arm.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (arm.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to", arm);
                telemetry.addData("Path2", "Running at");
                telemetry.update();
            }


            // Stop all motion;
            arm.setPower(0);

            // Turn off RUN_TO_POSITION
            arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        }
    }

    public void armVisionEncoderInches(double speed, double inches, double timeoutS) {
        int newTarget;

        // Determine new target position, and pass to motor controller
        newTarget = arm.getCurrentPosition() + (int) (inches * ARM_PER_INCH);

        arm.setTargetPosition(newTarget);

        // Turn On RUN_TO_POSITION
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();
        arm.setPower(Math.abs(speed));

        while (opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (arm.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1", "Running to", arm);
            telemetry.addData("Path2", "Running at");
            telemetry.update();
        }


        // Stop all motion;
        arm.setPower(0);

        // Turn off RUN_TO_POSITION
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }

}