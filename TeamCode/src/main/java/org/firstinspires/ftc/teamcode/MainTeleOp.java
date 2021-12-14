package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "DriveTrainTestLinearOp")
public class MainTeleOp extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor spinner;
    private DcMotor arm;
    private Servo hand;

    private Servo horizontal;

    private DcMotor dSlideR;

    @Override
    public void runOpMode() throws InterruptedException {

        //TODO: Hardware mapping when phones are configured;
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        spinner = hardwareMap.get(DcMotor.class, "spinner");
        arm = hardwareMap.get(DcMotor.class, "arm");
        hand = hardwareMap.get(Servo.class, "hand");

        dSlideR = hardwareMap.get(DcMotor.class, "dSlideR");

        horizontal = hardwareMap.get(Servo.class, "horizontal");

        //Set brake.
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        arm.setDirection(DcMotor.Direction.REVERSE);

        // encoder use
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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

            if (gamepad2.dpad_up) {

                drawerSlideUp();

            } else if (gamepad2.dpad_down) {

                drawerSliderDown();

            }

            //Horizontal Extension

            //expand
            if (gamepad2.b) {

                horizontal.setPosition(0.17);
                //compress
            } else if (gamepad2.x) {

                horizontal.setPosition(0.40);

            }

        }

    }

    public void drawerSlideUp() {

        dSlideR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        dSlideR.setTargetPosition(200);
        dSlideR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        sleep(1000);

        dSlideR.setPower(0.3);

        while (opModeIsActive() && dSlideR.isBusy()) {

            telemetry.addData("drawer slide right", dSlideR.getCurrentPosition() + " busy =" + dSlideR.isBusy());
            telemetry.update();
            idle();

        }

        dSlideR.setPower(0);

        resetStartTime();

    }

    public void drawerSliderDown() {

        dSlideR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        dSlideR.setTargetPosition(-200);
        dSlideR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        sleep(1000);

        dSlideR.setPower(0.3);

        while (opModeIsActive() && dSlideR.isBusy()) {

            telemetry.addData("drawer slide right", dSlideR.getCurrentPosition() + " busy =" + dSlideR.isBusy());
            telemetry.update();
            idle();

        }

        dSlideR.setPower(0);

        resetStartTime();

    }


}