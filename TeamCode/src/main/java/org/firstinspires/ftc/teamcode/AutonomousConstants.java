package org.firstinspires.ftc.teamcode;

public class AutonomousConstants {

    static final double COUNTS_PER_MOTOR_REV = 537.6;
    static final double DRIVE_GEAR_REDUCTION = 1.23;
    static final double WHEEL_DIAMETER_INCHES = 4.724;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.14159265);
    static final double DRIVE_SPEED = 0.70;
    static final double TURN_SPEED = 0.15;

    static final double TICKS_PER_SPINNER_REV = 1120;
    static final double SPIN_GEAR_REDUCTION = 1;

}
