package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "DriveTrainTestLinearOp")
public class MainTeleOp extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor spinner;

    static final double COUNTS_PER_MOTOR_REV = 1120;
    static final double DRIVE_GEAR_REDUCTION = .5;
    static final double WHEEL_DIAMETER_INCHES = 5.75;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);


    private ElapsedTime runtime = new ElapsedTime();


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

            //spinner movement
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

        }

    }


}
