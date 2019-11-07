package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

// this autonomous is for when the robot is facing the skybridge
// objective is to strafe away from the wall and park under the skybridge
@Autonomous(name="StrafePark")

public class StrafePark extends LinearOpMode{
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor rearLeft;
    private DcMotor rearRight;

    // our 2019-2020 robot uses 4 inch mechanum wheels with 20:1 neverest orbital motors geared to be 10:1
    static final double COUNTS_PER_MOTOR_REV   = 537.6 ; // neverest orbital 20 output shaft
    static final double DRIVE_GEAR_REDUCTION   = 2.0 ; // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES  = 4.0 ;  // diameter for circumference
    static final double COUNTS_PER_INCH_DOUBLE = (((COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION)/
                                                   (WHEEL_DIAMETER_INCHES * 3.1415))/4.8);
    static final int COUNTS_PER_INCH           = (int) Math.round(COUNTS_PER_INCH_DOUBLE);

    @Override
    public void runOpMode() {
        
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        rearLeft = hardwareMap.dcMotor.get("rearLeft");
        rearLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight = hardwareMap.dcMotor.get("frontRight");
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        rearRight = hardwareMap.dcMotor.get("rearRight");
        rearRight.setDirection(DcMotorSimple.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        frontLeft.setMode( DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(  DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode( DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            // telemetry.addData("Status", "Running", "Run Time" + runtime.toString());
            telemetry.update();
            idle();

            // robot is placed on the tile next to the skybridge
            // go forward

            telemetry.addData("frontLeft encoder",  frontLeft.getCurrentPosition());
            telemetry.addData("frontRight encoder", frontRight.getCurrentPosition());
            telemetry.addData("rearLeft encoder",   rearLeft.getCurrentPosition());
            telemetry.addData("rearRight encoder",  rearRight.getCurrentPosition());
            telemetry.update();

            // strafe to avoid hitting alliance partner,
            // estimate of how many encoders ticks to use, need to figure out 
            // what an inch strafed is to encoder ticks
            while (frontLeft.getCurrentPosition() < (COUNTS_PER_INCH * 40) && 
                   frontRight.getCurrentPosition() < (COUNTS_PER_INCH * 40)) {

                frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                rearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                rearRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                
                telemetry.addData("first motion: strafe left", frontRight.getCurrentPosition());
                telemetry.addData("target position", COUNTS_PER_INCH * 20);
                telemetry.update();
                
                frontLeft.setPower(-0.5);
                frontRight.setPower(0.5);
                rearLeft.setPower(0.5);
                rearRight.setPower(-0.5);
 
                if (isStopRequested()) {
                    frontLeft.setPower(0.0);
                    frontRight.setPower(0.0);
                    rearLeft.setPower(0.0);
                    rearRight.setPower(0.0);

                    stop();
                    break;
                }
            // end of strafe code
            }
            
            frontLeft.setPower(0.0);
            frontRight.setPower(0.0);
            rearLeft.setPower(0.0);
            rearRight.setPower(0.0);


            // go forward to be under skybridge
            while ( // (frontLeft.getCurrentPosition() > (COUNTS_PER_INCH * 20) && 
                    (frontRight.getCurrentPosition() > (COUNTS_PER_INCH * 40)) &&
                //   (frontLeft.getCurrentPosition() < (COUNTS_PER_INCH * 40) && 
                    (frontRight.getCurrentPosition() < (COUNTS_PER_INCH * 40))) {
                
                frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                rearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                rearRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                
                telemetry.update();
                telemetry.addData("second motion: drive forward", frontRight.getCurrentPosition());
                
                frontLeft.setPower(0.5);
                frontRight.setPower(0.5);
                rearLeft.setPower(0.5);
                rearRight.setPower(0.5);
 
                if (isStopRequested()) {
                    frontLeft.setPower(0.0);
                    frontRight.setPower(0.0);
                    rearLeft.setPower(0.0);
                    rearRight.setPower(0.0);

                    stop();
                    break;
                }
 
            }

            // park under skybridge
            
            frontLeft.setPower(0.0);
            frontRight.setPower(0.0);
            rearLeft.setPower(0.0);
            rearRight.setPower(0.0);

            stop();
        }
        
    }
}
