package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "DriveTrainTestLinearOp")
public class MainTeleOp extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor spinner;

    private FreightArmSystem f = new FreightArmSystem(hardwareMap, "arm", "hand", 180.0, 0.0);

    private ElapsedTime runtime = new ElapsedTime();
    private Object FreightArm;


    @Override
    public void runOpMode() throws InterruptedException {

        //TODO: Hardware mapping when phones are configured;
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        spinner = hardwareMap.get(DcMotor.class, "spinner");


        //Set brake.
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);


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

                spinner.setPower(0.7);

            } else if (!gamepad1.b) {

                spinner.setPower(0);

            }

            if (gamepad1.x) {

                spinner.setPower(-0.7);

            } else if (!gamepad1.x) {

                spinner.setPower(0);

            }

            //reverse
            if (gamepad1.y) {
                if (frontLeft.getDirection() == DcMotor.Direction.REVERSE) {
                    frontLeft.setDirection(DcMotor.Direction.FORWARD);
                    backLeft.setDirection(DcMotor.Direction.FORWARD);
                    frontRight.setDirection(DcMotor.Direction.REVERSE);
                    backRight.setDirection(DcMotor.Direction.REVERSE);
                } else if (frontLeft.getDirection() == DcMotor.Direction.FORWARD) {
                    frontLeft.setDirection(DcMotor.Direction.REVERSE);
                    backLeft.setDirection(DcMotor.Direction.REVERSE);
                    frontRight.setDirection(DcMotor.Direction.FORWARD);
                    backRight.setDirection(DcMotor.Direction.FORWARD);
                }
            }

            //arm

            //arm up
            if (gamepad1.dpad_up) {

                f.setFreightArmPosition(FreightArmSystem.ArmPosition.UP, 0.7);

            } else if (gamepad1.dpad_down) {

                f.setFreightArmPosition(FreightArmSystem.ArmPosition.DOWN, 0.7);

            }

            //claw

            if (gamepad1.dpad_left) {

                f.setFreightHandPosition(FreightArmSystem.HandPosition.OPEN);

            } else if (gamepad1.dpad_right) {

                f.setFreightHandPosition(FreightArmSystem.HandPosition.CLOSED);

            }

        }

    }
}
