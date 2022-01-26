package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name = "DriveTrainTestLinearOp")
public class MainTeleOp extends LinearOpMode {

    //Constants for movement.
    static final double COUNTS_PER_MOTOR_REV = 537.6;
    static final double ARM_GEAR_REDUCTION = 5.0;
    static final double WHEEL_DIAMETER_INCHES = 2.45;
    static final double ARM_RATIO = 0.4;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * ARM_GEAR_REDUCTION * ARM_RATIO) /
            (WHEEL_DIAMETER_INCHES * 3.14159265);
    static final double OPEN = 0.44;
    static final double MIDDLE = 0.55;
    static final double CLOSE = 1.0;

    static final double TOP = 8.0;
    static final double MOVE = 5.0;
    static final double GROUND = 0.0;


    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor spinner;
    private DcMotor arm;
    private Servo boxServo;

    private DcMotor intake;

    private BNO055IMU imu;
    private Orientation lastAngles = new Orientation();
    private double globalAngle;


    @Override
    public void runOpMode() throws InterruptedException {


        //Mapping.
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        spinner = hardwareMap.get(DcMotor.class, "spinner");
        arm = hardwareMap.get(DcMotor.class, "arm");
        intake = hardwareMap.get(DcMotor.class, "intake");
        boxServo = hardwareMap.get(Servo.class, "boxServo");

        //Set brake.
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Change direction.
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        arm.setDirection(DcMotor.Direction.REVERSE);

        // encoder use

        double minPower = -.7;
        double maxPower = .7;

        waitForStart();

        while (opModeIsActive()) {

            //compensate for hardware issue
            frontLeft.setPower(Range.clip(-gamepad1.right_stick_y, minPower, maxPower));
            backLeft.setPower(Range.clip(-gamepad1.right_stick_y, minPower, maxPower));

            frontRight.setPower(Range.clip(-gamepad1.left_stick_y, minPower, maxPower));
            backRight.setPower(Range.clip(-gamepad1.left_stick_y, minPower, maxPower));



            //spinner

            if (gamepad1.b) {

                spinner.setPower(1.0);

            } else if (!gamepad1.b) {

                spinner.setPower(0);

            }

            if (gamepad1.x) {

                spinner.setPower(-1.0);

            } else if (!gamepad1.x) {

                spinner.setPower(0);

            }
            //arm

            //arm up
            if (gamepad1.dpad_up) {

                arm.setPower(0.65);

            } else {

                arm.setPower(-0.035);

            }


            if (gamepad1.dpad_down) {

                arm.setPower(-0.65);

            }else{

                arm.setPower(0.035);

            }


            //arm stationary power use

            //arm.setPower(Range.clip(gamepad1.right_trigger, 0, 0.05));
            //arm.setPower(Range.clip(gamepad1.left_trigger,0,-0.05));


            if (gamepad1.left_bumper) {

                intake.setPower(0.6);

            }else {
                intake.setPower(0.0);
            }


            //code for intake

            if (gamepad1.right_bumper) {

                intake.setPower(-0.6);

            }else {
                intake.setPower(0.0);
            }


            if (gamepad1.y) {

                boxServo.setPosition(MIDDLE);

            }

            if(gamepad1.a) {

                boxServo.setPosition(CLOSE);
                sleep(1000);
                boxServo.setPosition(OPEN);

            }

        }
    }

        //This method reads the IMU getting the angle. It automatically adjusts the angle so that it is between -180 and +180.
        public double getAngle () {
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

        public void rotate ( int degrees, double power){
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
            intake.setPower(rightPower);

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

            intake.setPower(0);

            //sleep for a bit to make sure the robot doesn't over shoot
            sleep(1000);

            resetAngle();
        }


        //this method resets the angle so that the robot's heading is now 0
        public void resetAngle () {
            lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            globalAngle = 0;
        }
}
