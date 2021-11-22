package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name = "MainAutonomous")
public class MainAutonomous extends LinearOpMode {


    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    BNO055IMU imu;
    Orientation angles;

    @Override
    public void runOpMode() throws InterruptedException {

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        BNO055IMU.Parameters p = new BNO055IMU.Parameters();
        p.angleUnit = BNO055IMU.AngleUnit.DEGREES;

        //Hardware map (remember to set on phone)
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(p);

        waitForStart();


    }

    public void driveForward(double power, long time) {

        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);

        sleep(time);

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        sleep(500);

    }

    public void turnRight (int angle, double power) {

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        sleep(500);

        frontLeft.setPower(power);
        backLeft.setPower(power);
        frontRight.setPower(0);
        backRight.setPower(0);

    }
}