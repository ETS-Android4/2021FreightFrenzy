package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "New Barcode Test", group = "Test")

public class BarcodeInitTest extends LinearOpMode {

    @Override
    public void runOpMode( ) throws InterruptedException {

        BarcodeUtil detector = new BarcodeUtil( hardwareMap, "webcam", telemetry );
        detector.init();

        telemetry.addLine( "Image Detection init finished" );
        telemetry.update( );

        waitForStart( );

        telemetry.addLine( "Position: " + detector.getBarcodePosition( ) );

        while( !isStopRequested() );

    }
}