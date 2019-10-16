package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

// this autonomous requires the robot to be facing the skybridge
// objective is to park
// make sure name is the same as the class and file name
@Autonomous(name="AutoPark")

// create class
public class AutoPark extends LinearOpMode{

    // create time
    private ElapsedTime runtime = new ElapsedTime();

    // create motors
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor rearLeft;
    private DcMotor rearRight;

    // calculate how many encoder ticks are per inch for the robot
    // neverest orbital 20 output shaft, found in motor documentation
    static final double COUNTS_PER_MOTOR_REV   = 537.6 ; 
    // robot is geared down 10, so in combination with the 20:1 gearbox on the robot the gearing is 2
    static final double DRIVE_GEAR_REDUCTION   = 2.0 ; // This is < 1.0 if geared UP
    // our current mecahnum wheels have a diameter of 4 inches
    static final double WHEEL_DIAMETER_INCHES  = 4.0 ;  // For figuring circumference
    // equation for calculating based off inputted constants
    static final double COUNTS_PER_INCH_DOUBLE = (((COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION)
                                                  /(WHEEL_DIAMETER_INCHES * 3.1415))/4.8);
    // motor can only move a whole number of encoder ticks, so round to nearest integer
    static final int COUNTS_PER_INCH           = (int) Math.round(COUNTS_PER_INCH_DOUBLE);

    // code to run when the opmode starts
    @Override
    public void runOpMode() {
        
        // set motors to the ones that are in the configuration file on the phone
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        rearLeft = hardwareMap.dcMotor.get("rearLeft");
        rearLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight = hardwareMap.dcMotor.get("frontRight");
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        rearRight = hardwareMap.dcMotor.get("rearRight");
        rearRight.setDirection(DcMotorSimple.Direction.FORWARD);

        // set to brake mode so robot won't be pushed
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        // reset drive encoders to zero so any previous driving doesn't interfer
        frontLeft.setMode( DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(  DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode( DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // wait for the start button to be pressed on the driver station
        waitForStart();
        // restart time
        runtime.reset();

        // code to run while robot is moving
        while (opModeIsActive()) {

            // telemetry.addData("Status", "Running", "Run Time" + runtime.toString());
            telemetry.update();
            idle();

            // robot is placed on the tile next to the skybridge
            // go forward
            // display current encoder position on the driver station
            telemetry.addData("frontLeft encoder",  frontLeft.getCurrentPosition());
            telemetry.addData("frontRight encoder", frontRight.getCurrentPosition());
            telemetry.addData("rearLeft encoder",   rearLeft.getCurrentPosition());
            telemetry.addData("rearRight encoder",  rearRight.getCurrentPosition());

            // first motion
            // drive the robot forward while the distance moved is less than 24 inches
            while (frontLeft.getCurrentPosition() < (COUNTS_PER_INCH * 24)) {
                
                frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                rearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                rearRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                
                telemetry.addData("wheels should be moving", frontLeft.getCurrentPosition());
                
                // speed up if neccesary
                frontLeft.setPower(0.5);
                frontRight.setPower(0.5);
                rearLeft.setPower(0.5);
                rearRight.setPower(0.5);
 
                // if the stop button is pressed while the robot is driving, the robot will stop moving
                if (isStopRequested()) {
                    frontLeft.setPower(0.0);
                    frontRight.setPower(0.0);
                    rearLeft.setPower(0.0);
                    rearRight.setPower(0.0);

                    stop();
                    break;
                }
            // end of robot driving 
            }

            // park under skybridge
            frontLeft.setPower(0.0);
            frontRight.setPower(0.0);
            rearLeft.setPower(0.0);
            rearRight.setPower(0.0);

            stop();
        }
    // end of active opmode code
    }
}
