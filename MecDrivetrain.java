package org.firstinspires.ftc.teamcode;

import java.lang.annotation.Target;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="MecDrivetrain")

// this file is to serve as boilerplate code for mechanum drivetrains
public class MecDrivetrain extends LinearOpMode{
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor FrontLeftMotor;
    private DcMotor RearLeftMotor;
    private DcMotor FrontRightMotor;
    private DcMotor RearRightMotor;
    double power = 1.0;

    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        FrontLeftMotor = hardwareMap.dcMotor.get("FrontLeftMotor");
        FrontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        RearLeftMotor = hardwareMap.dcMotor.get("RearLeftMotor");
        RearLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        FrontRightMotor = hardwareMap.dcMotor.get("FrontRightMotor");
        FrontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        RearRightMotor = hardwareMap.dcMotor.get("RearRightMotor");
        RearRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        FrontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RearLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RearRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            //telemetry.addData("Status", "Running", "Run Time" + runtime.toString());
            telemetry.update();
            idle();

            // change to make 30% power
            if (gamepad1.left_trigger > 0.5){
                FrontRightMotor.setPower((0.3*((-(Math.hypot(gamepad1.left_stick_y, gamepad1.right_stick_x)) * (Math.cos(Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4))) - (gamepad1.left_stick_x))));
                RearRightMotor.setPower((0.3*((-(Math.hypot(gamepad1.left_stick_y, gamepad1.right_stick_x)) * (Math.sin(Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4))) - (gamepad1.left_stick_x))));
                FrontLeftMotor.setPower((0.3*(((Math.hypot(gamepad1.left_stick_y, gamepad1.right_stick_x)) * (Math.sin(Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4))) - (gamepad1.left_stick_x))));
                RearLeftMotor.setPower((0.3*(((Math.hypot(gamepad1.left_stick_y, gamepad1.right_stick_x)) * (Math.cos(Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4))) - (gamepad1.left_stick_x))));
            }

            // full power nomally
            else{
                FrontRightMotor.setPower(((-(Math.hypot(gamepad1.left_stick_y, gamepad1.right_stick_x)) * (Math.cos(Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4))) - (gamepad1.left_stick_x)));
                RearRightMotor.setPower(((-(Math.hypot(gamepad1.left_stick_y, gamepad1.right_stick_x)) * (Math.sin(Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4))) - (gamepad1.left_stick_x)));
                FrontLeftMotor.setPower((((Math.hypot(gamepad1.left_stick_y, gamepad1.right_stick_x)) * (Math.sin(Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4))) - (gamepad1.left_stick_x)));
                RearLeftMotor.setPower((((Math.hypot(gamepad1.left_stick_y, gamepad1.right_stick_x)) * (Math.cos(Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4))) - (gamepad1.left_stick_x)));
            }

        //telemetry.addData(">", "Done");
        //telemetry.addData("Target Power", tgtPower);
        //telemetry.addData("Motor Power", motorTest.getPower());
        //telemetry.addData("Status", "Running");
        //telemetry.update();
        power = 0.0;
        }
    }
}
