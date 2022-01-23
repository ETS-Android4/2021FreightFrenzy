package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "DriveTrainTestLinearOp")
public class MainTeleOp extends LinearOpMode {

    //Constants for movement.
    static final double COUNTS_PER_MOTOR_REV = 537.6;
    static final double ARM_GEAR_REDUCTION = 5.0;
    static final double WHEEL_DIAMETER_INCHES = 2.45;
    static final double ARM_RATIO = 1.0;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * ARM_GEAR_REDUCTION * ARM_RATIO) /
            (WHEEL_DIAMETER_INCHES * 3.14159265);
    static final double OPEN = 0.5;
    static final double MIDDLE = 0.75;
    static final double CLOSE = 1;

    static final double TOP = 8.0;
    static final double MID = 5.0;
    static final double LOW = 2.0;
    static final double BOX = 4;


    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor spinner;
    private DcMotor arm;
    private Servo boxServo;

    private DcMotor intake;

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
        //frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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

                int newTarget;
                double timeoutS = 2.0;
                newTarget = arm.getCurrentPosition() + (int) (TOP * COUNTS_PER_INCH);

                arm.setTargetPosition(newTarget);

                // Turn On RUN_TO_POSITION
                arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                // reset the timeout time and start motion.
                runtime.reset();
                arm.setPower(Math.abs(0.7));

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

            if (gamepad1.dpad_left) {
                int newTarget;
                double timeoutS = 2.0;
                newTarget = arm.getCurrentPosition() + (int) (MID * COUNTS_PER_INCH);

                arm.setTargetPosition(newTarget);

                // Turn On RUN_TO_POSITION
                arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                // reset the timeout time and start motion.
                runtime.reset();
                arm.setPower(Math.abs(0.7));

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

            if (gamepad1.dpad_right) {

                int newTarget;

                newTarget = arm.getCurrentPosition() + (int) (LOW * COUNTS_PER_INCH);

                arm.setTargetPosition(newTarget);

                // Turn On RUN_TO_POSITION

                arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                runtime.reset();
                arm.setPower(Math.abs(0.7));

                // Stop all motion;
                arm.setPower(0);

                // Turn off RUN_TO_POSITION
                arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            if (gamepad1.dpad_down) {

                if (gamepad1.dpad_right) {

                    int newTarget;

                    newTarget = arm.getCurrentPosition() + (int) (LOW * COUNTS_PER_INCH);

                    arm.setTargetPosition(newTarget);

                    // Turn On RUN_TO_POSITION

                    arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    runtime.reset();
                    arm.setPower(Math.abs(-0.7));

                    // Stop all motion;
                    arm.setPower(0);

                    // Turn off RUN_TO_POSITION
                    arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                }


            }

            if (gamepad1.left_bumper) {

                intake.setPower(0.2);
                sleep(5000);


            }


            //code for intake

            if (gamepad1.right_bumper) {

                intake.setPower(0.65);

            } else {

                intake.setPower(0);
            }

            if (gamepad1.left_bumper) {

                intake.setDirection(DcMotor.Direction.REVERSE);
                intake.setPower(0.65);
                intake.setDirection(DcMotor.Direction.FORWARD);

            } else {

                intake.setPower(0);

            }

            if (gamepad1.y) {

                sleep(1000);

                boxServo.setPosition(MIDDLE);

            }

            if (gamepad1.a) {

                boxServo.setPosition(CLOSE);

                sleep(1000);

                boxServo.setPosition(OPEN);
            }

        }
    }
}