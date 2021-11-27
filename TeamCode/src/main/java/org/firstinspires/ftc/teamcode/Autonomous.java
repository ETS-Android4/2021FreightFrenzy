package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Autonomous {

    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;
    DcMotor spinner;
    BNO055IMU imu;

    Orientation lastAngles = new Orientation();
    double globalAngle;

    public Autonomous(HardwareMap hardwareMap, String a, String b, String c, String d, String s, BNO055IMU imu) {

        frontLeft = hardwareMap.dcMotor.get(a);
        frontRight = hardwareMap.dcMotor.get(b);
        backLeft = hardwareMap.dcMotor.get(c);
        backRight = hardwareMap.dcMotor.get(d);
        spinner = hardwareMap.dcMotor.get(s);

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        spinner.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public void initializeIMU(HardwareMap hardwareMap, BNO055IMU imu) {

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        this.imu = hardwareMap.get(BNO055IMU.class, "imu");
        this.imu.initialize(parameters);

    }

    public void stopResetEncoders() {

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spinner.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }

    public void runUsingEncoders() {


        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        spinner.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void encoderDrive(LinearOpMode l, Telemetry telemetry, ElapsedTime runtime, double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {


        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (l.opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = frontLeft.getCurrentPosition() + (int) (leftInches * AutonomousConstants.COUNTS_PER_INCH);
            newLeftTarget = backLeft.getCurrentPosition() + (int) (leftInches * AutonomousConstants.COUNTS_PER_INCH);
            newRightTarget = frontRight.getCurrentPosition() + (int) (rightInches * AutonomousConstants.COUNTS_PER_INCH);
            newRightTarget = backRight.getCurrentPosition() + (int) (rightInches * AutonomousConstants.COUNTS_PER_INCH);
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


            while (l.opModeIsActive() &&
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
    public void rotate(LinearOpMode l, int degrees, double power) {

        double leftPower, rightPower;
        degrees += 23;

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
            while (l.opModeIsActive() && getAngle() == 0) {
            }

            while (l.opModeIsActive() && getAngle() > degrees) {
            }
        } else
            while (l.opModeIsActive() && getAngle() < degrees) {
            }


        //stop the motors after the angle has been found.

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        //sleep for a bit to make sure the robot doesn't over shoot
        l.sleep(1000);

        resetAngle();
    }


    //this method resets the angle so that the robot's heading is now 0
    public void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        globalAngle = 0;
    }

}

