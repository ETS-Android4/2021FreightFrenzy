import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Motor Test")
public class MotorTest extends LinearOpMode {

    private DcMotor dcmotor;

    @Override
    public void runOpMode() throws InterruptedException {

        dcmotor = hardwareMap.get(DcMotor.class, "dcmotor");

        waitForStart();

        dcmotor.setPower(1);
        sleep(1000);

    }
}
