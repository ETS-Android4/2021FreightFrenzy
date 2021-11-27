package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class FreightArmSystem {

    static final double COUNTS_PER_MOTOR_REV = 1120;
    static final double DRIVE_GEAR_REDUCTION = 1.23;

    private DcMotor freightArm;
    private Servo freightHand;

    private double openClawPosition;
    private double closedClawPosition;

    public enum ArmPosition {
        UP,
        DOWN
    }

    public enum HandPosition {
        OPEN,
        CLOSED
    }

    private ArmPosition currentArmPosition;

    private HandPosition currentHandPosition;

    public FreightArmSystem(HardwareMap hardwareMap, String armName, String handName, String liftButton, String lowerButton, double openP, double closeP) {
        freightArm = hardwareMap.dcMotor.get(armName);
        freightHand = hardwareMap.servo.get(handName);
        this.openClawPosition = openP;
        this.closedClawPosition = closeP;

        freightArm.setDirection(DcMotor.Direction.REVERSE);

    }

    public void setFreightArmPower(double power) {
        freightArm.setPower(power);
    }

    public void setFreightArmPosition(ArmPosition position, double power) {

        switch (position) {
            case UP: {
                if (currentArmPosition == ArmPosition.UP)
                    break;

                liftToPosition(ArmPosition.UP, power);
                currentArmPosition = ArmPosition.UP;
                break;
            }
            case DOWN: {
                if (currentArmPosition == ArmPosition.DOWN)
                    break;

                liftToPosition(ArmPosition.DOWN, power);
                currentArmPosition = ArmPosition.DOWN;
                break;
            }
        }
    }

    public void setFreightHandPosition(HandPosition position) {
        switch(position) {
            case OPEN:
                freightHand.setPosition(openClawPosition);
                currentHandPosition = HandPosition.OPEN;
                break;

            case CLOSED:
                freightHand.setPosition(closedClawPosition);
                currentHandPosition = HandPosition.CLOSED;
                break;
        }

    }

    private void liftToPosition(ArmPosition position, double power) {
        long time = System.currentTimeMillis() + 1000;
        if(position == ArmPosition.UP) {
            freightArm.setPower(power);
        }
        else {
            freightArm.setPower(-power);
        }
        freightArm.setPower(0);
    }

    public double getHandPosition() {
        return freightHand.getPosition();
    }

    public double getArmPower() {
        return freightArm.getPower();
    }

    public HandPosition getCurrentHandPosition() {
        return currentHandPosition;
    }

    public ArmPosition getCurrentArmPosition() {
        return currentArmPosition;
    }

}
