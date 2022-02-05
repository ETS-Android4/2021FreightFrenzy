package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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

                spinner.setPower(-0.95);

            } else if (!gamepad1.x) {

                spinner.setPower(0);

            }
            //arm

            //arm up
            if (gamepad1.dpad_up) {

                arm.setPower(0.65);

            } else {

                arm.setPower(-0.025);

            }


            if (gamepad1.dpad_down) {

                arm.setPower(-0.65);

            } else {

                arm.setPower(0.025);

            }


            //arm stationary power use

            //arm.setPower(Range.clip(gamepad1.right_trigger, 0, 0.05));
            //arm.setPower(Range.clip(gamepad1.left_trigger,0,-0.05));


            if (gamepad1.left_bumper) {

                intake.setPower(0.6);

            } else {
                intake.setPower(0.0);
            }


            //code for intake

            if (gamepad1.right_bumper) {

                intake.setPower(-0.6);

            } else {
                intake.setPower(0.0);
            }


            if (gamepad1.y) {

                boxServo.setPosition(MIDDLE);

            }

            if (gamepad1.a) {

                boxServo.setPosition(CLOSE);
                sleep(1000);
                boxServo.setPosition(OPEN);

            }

            /**
            if (gamepad1.dpad_left) {


            }

            if (gamepad1.dpad_right) {


            }
             **/

        }
    }

    public void armEncoder(double speed, double inches, double timeoutS) {
        int newTarget;
        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            // Determine new target position, and pass to motor controller
            newTarget = arm.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
            arm.setTargetPosition(newTarget);
            // Turn On RUN_TO_POSITION
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // reset the timeout time and start motion.
            runtime.reset();
            arm.setPower(Math.abs(speed));

            // Stop all motion;
            arm.setPower(0);
            // Turn off RUN_TO_POSITION
            arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

}