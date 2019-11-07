package org.firstinspires.ftc.teamcode;

import java.lang.annotation.Target;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Old2019Mec")

// this class is for the 2019-2020 FTC 9536 robot

// this class is a relic of when the robot had a ground intake and v1 of the 
// transfer mechanism
public class Old2019Mec extends LinearOpMode{
    
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor frontLeft;
    private DcMotor rearLeft;
    private DcMotor frontRight;
    private DcMotor rearRight;
    private DcMotor outtakeMotor;
    private DcMotor arm;

    // private DcMotor leftIntake;
    // private DcMotor rightIntake;
    // private CRServo leftGround;
    // private CRServo rightGround;

    private CRServo leftCarriage;
    private Servo   rightCarriage;
    
    static final double MAX_POS = 1.0;
    // double power = 1.0;

    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        // create drivetrain motors
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        rearLeft = hardwareMap.dcMotor.get("rearLeft");
        rearLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight = hardwareMap.dcMotor.get("frontRight");
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        rearRight = hardwareMap.dcMotor.get("rearRight");
        rearRight.setDirection(DcMotorSimple.Direction.FORWARD);

        // create operator motors
        arm = hardwareMap.dcMotor.get("arm");
        arm.setDirection(DcMotorSimple.Direction.REVERSE);
        outtakeMotor = hardwareMap.dcMotor.get("outtakeMotor");
        outtakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        // leftIntake = hardwareMap.dcMotor.get("leftIntake");
        // leftIntake.setDirection(DcMotorSimple.Direction.FORWARD);
        // rightIntake = hardwareMap.dcMotor.get("rightIntake");
        // rightIntake.setDirection(DcMotorSimple.Direction.FORWARD);

        // create servos
        // leftGround    = hardwareMap.get(CRServo.class, "leftGround");
        // rightGround   = hardwareMap.get(CRServo.class, "rightGround");
        // leftCarriage  = hardwareMap.get(CRServo.class, "leftCarriage");
        // rightCarriage = hardwareMap.get(Servo.class,   "rightCarriage");

        // set all motors to brake mode
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        outtakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // leftIntake.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE);
        // rightIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            //telemetry.addData("Status", "Running", "Run Time" + runtime.toString());
            telemetry.update();
            idle();

            /**            MOTOR CONTROLS             **/

            // change to make drivetrain 30% power
            if (gamepad1.left_trigger > 0.5){
                frontRight.setPower((0.3*((-(Math.hypot(gamepad1.left_stick_y, gamepad1.right_stick_x)) * (Math.cos(Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4))) - (gamepad1.left_stick_x))));
                rearRight.setPower((0.3*((-( Math.hypot(gamepad1.left_stick_y, gamepad1.right_stick_x)) * (Math.sin(Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4))) - (gamepad1.left_stick_x))));
                frontLeft.setPower((0.3*(((  Math.hypot(gamepad1.left_stick_y, gamepad1.right_stick_x)) * (Math.sin(Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4))) - (gamepad1.left_stick_x))));
                rearLeft.setPower((0.3*(((   Math.hypot(gamepad1.left_stick_y, gamepad1.right_stick_x)) * (Math.cos(Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4))) - (gamepad1.left_stick_x))));
            }

            // full power nomally
            frontRight.setPower(((-(Math.hypot(gamepad1.left_stick_y, gamepad1.right_stick_x)) * (Math.cos(Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4))) - (gamepad1.left_stick_x)));
            rearRight.setPower(((-( Math.hypot(gamepad1.left_stick_y, gamepad1.right_stick_x)) * (Math.sin(Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4))) - (gamepad1.left_stick_x)));
            frontLeft.setPower((((  Math.hypot(gamepad1.left_stick_y, gamepad1.right_stick_x)) * (Math.sin(Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4))) - (gamepad1.left_stick_x)));
            rearLeft.setPower((((   Math.hypot(gamepad1.left_stick_y, gamepad1.right_stick_x)) * (Math.cos(Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4))) - (gamepad1.left_stick_x)));

            // intake wheels are driven by gamepad 2's triggers
            // if (gamepad2.left_trigger > 0.1) {
            //     leftIntake.setPower(1.0);
            //     rightIntake.setPower(1.0);
            // }

            // if (gamepad2.right_trigger > 0.1) {
            //     leftIntake.setPower(-1.0);
            //     rightIntake.setPower(-1.0);
            // }

            outtakeMotor.setPower(gamepad2.right_stick_y);
            arm.setPower(0.9 * gamepad2.left_stick_y);

            /**           SERVO CONTROLS            **/

            // keep servos still when button is not called, right servo automatically has negative power for some reason 
            // leftGround.setPower(0.0);
            // rightGround.setPower(0.05);

            // button for opening the ground intake
            // if (gamepad2.a) {
            //     leftGround.setPower(1.0);
            //     rightGround.setPower(1.0);
            // } 

            // button for closing the ground intake
            // if (gamepad2.b) {
            //     leftGround.setPower(-1.0);
            //     rightGround.setPower(-1.0);
            // }

            // move the servo to grib the stone
            if (gamepad2.y) {
                rightCarriage.setPosition(0.4);
            }
            if (gamepad2.x) {
                rightCarriage.setPosition(-0.1);
            }

            // same control as arm
            if (gamepad2.left_stick_y > 0.1 || gamepad2.left_stick_y < -0.1) {
                leftCarriage.setPower(0.5 * gamepad2.left_stick_y);
            } else {
                leftCarriage.setPower(-0.8);
            }
            
            // control the main transfer servo with the bumper buttons on gamepad 2
            // if (gamepad2.left_bumper) {
            //     leftCarriage.setPower(-0.2);
            // }
            
            // if (gamepad2.right_bumper) {
            // leftCarriage.setPower(-0.8);
            // }

            // keep motors still
            // leftIntake.setPower(0.0);
            // rightIntake.setPower(0.0);

            // telemetry.addData("leftGround Power", leftGround.getPower());
            // telemetry.addData("rightGround Power", rightGround.getPower());
            telemetry.addData("leftCarriage Power", leftCarriage.getPower());
            telemetry.addData("rightCarriage Position", rightCarriage.getPosition());
            // telemetry.addData("gamepad2 left trigger", gamepad2.left_trigger());
            // telemetry.addData("gamepad2 right trigger", gamepad2.right_trigger());
            //telemetry.addData(">", "Done");
            //telemetry.addData("Status", "Running");
            //telemetry.update();
            // power = 0.0;
        }
    }
}
