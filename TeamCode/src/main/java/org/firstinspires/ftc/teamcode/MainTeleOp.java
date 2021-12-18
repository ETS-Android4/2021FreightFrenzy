package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "DriveTrainTestLinearOp")
public class MainTeleOp extends LinearOpMode {

    static final double COUNTS_PER_MOTOR_REV = 537.6;
    static final double PULLEY_GEAR_REDUCTION = 0.6;
    static final double WHEEL_DIAMETER_INCHES = 2.45;
    static final double PULLEY_WHEEL_VERTICAL_RATIO = 7.111;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * PULLEY_GEAR_REDUCTION * PULLEY_WHEEL_VERTICAL_RATIO) /
            (WHEEL_DIAMETER_INCHES * 3.14159265);

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor spinner;
    private DcMotor arm;
    private Servo hand;

    private Servo horizontal;

    private DcMotor dSlideR;
    private DcMotor dSlideL;

   private DcMotor intake;

    @Override
    public void runOpMode() throws InterruptedException {

        //TODO: Hardware mapping when phones are configured;
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        spinner = hardwareMap.get(DcMotor.class, "spinner");

        //hand = hardwareMap.get(Servo.class, "hand");

        dSlideR = hardwareMap.get(DcMotor.class, "dSlideR");
        dSlideL = hardwareMap.get(DcMotor.class, "dSlideL");

        horizontal = hardwareMap.get(Servo.class, "horizontal");

        intake = hardwareMap.get(DcMotor.class, "intake");

        //Set brake.
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        //arm.setDirection(DcMotor.Direction.REVERSE);

        // encoder use
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        dSlideL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        dSlideR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        dSlideL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        dSlideR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


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

            /**
            //arm

            //arm up
            if (gamepad1.dpad_up) {

                arm.setPower(-0.55);

            } else if (!gamepad1.dpad_up) {

                arm.setPower(0);

            }

            if (gamepad1.dpad_down) {

                arm.setPower(0.55);

            } else if (!gamepad1.dpad_down) {

                arm.setPower(0);

            }

            //claw

            if (gamepad1.left_bumper) {

                hand.setPosition(0.25);

            } else if (gamepad1.right_bumper) {

                hand.setPosition(.45);

            }

             **/

            //flip flop

            if (gamepad1.y) {

                frontLeft.setDirection(DcMotor.Direction.FORWARD);
                backLeft.setDirection(DcMotor.Direction.FORWARD);
                backRight.setDirection(DcMotor.Direction.REVERSE);
                frontRight.setDirection(DcMotor.Direction.REVERSE);

                frontLeft.setPower(Range.clip(-gamepad1.left_stick_y, minPower, maxPower));
                backLeft.setPower(Range.clip(-gamepad1.left_stick_y, minPower, maxPower));

                frontRight.setPower(Range.clip(-gamepad1.right_stick_y, minPower, maxPower));
                backRight.setPower(Range.clip(-gamepad1.right_stick_y, minPower, maxPower));


            } else if (gamepad1.a) {

                frontLeft.setDirection(DcMotor.Direction.REVERSE);
                backLeft.setDirection(DcMotor.Direction.REVERSE);
                frontRight.setDirection(DcMotor.Direction.FORWARD);
                backRight.setDirection(DcMotor.Direction.FORWARD);

                frontLeft.setPower(Range.clip(-gamepad1.right_stick_y, minPower, maxPower));
                backLeft.setPower(Range.clip(-gamepad1.right_stick_y, minPower, maxPower));

                frontRight.setPower(Range.clip(-gamepad1.left_stick_y, minPower, maxPower));
                backRight.setPower(Range.clip(-gamepad1.left_stick_y, minPower, maxPower));


            }

            //Horizontal Extension

            //expand
            if (gamepad2.b) {

                horizontal.setPosition(0.27);
                telemetry.addLine( "Position: " + horizontal.getPosition());


            }

            //compress
            if (gamepad2.x) {

                horizontal.setPosition(0.12);
                telemetry.addLine( "Position: " + horizontal.getPosition());

            }

            //Vertical Extension

            //Up


            if (gamepad2.dpad_up) {

                dSliderEncoder(0.2, 3.5, 5.0);

            }

            //Down

            if(gamepad2.dpad_down) {
                dSlideL.setDirection(DcMotor.Direction.REVERSE);
                dSlideR.setDirection(DcMotor.Direction.REVERSE);

                dSliderEncoder(0.2, 3.5, 5.0);

                dSlideL.setDirection(DcMotor.Direction.FORWARD);
                dSlideR.setDirection(DcMotor.Direction.FORWARD);

            }

            //code for intake

            if (gamepad2.right_bumper) {

                intake.setPower(0.4);

            } else {

                intake.setPower(0);
            }

            if (gamepad2.left_bumper) {

                intake.setDirection(DcMotor.Direction.REVERSE);
                intake.setPower(0.4);
                intake.setDirection(DcMotor.Direction.FORWARD);

            } else {

                intake.setPower(0);

            }
        }

    }



    public void dSliderEncoder(double speed, double inches, double timeoutS) {
        int lNewTarget, rNewTarget;
        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            lNewTarget = dSlideL.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
            rNewTarget = dSlideR.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);

            dSlideL.setTargetPosition(lNewTarget);
            dSlideR.setTargetPosition(rNewTarget);

            // Turn On RUN_TO_POSITION
            dSlideL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            dSlideR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            dSlideL.setPower(Math.abs(speed));
            dSlideR.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (dSlideR.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to", lNewTarget, rNewTarget);
                telemetry.addData("Path2", "Running at");
                telemetry.update();
            }


            // Stop all motion;
            dSlideL.setPower(0);
            dSlideR.setPower(0);

            // Turn off RUN_TO_POSITION
            dSlideL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            dSlideR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }

    }


}