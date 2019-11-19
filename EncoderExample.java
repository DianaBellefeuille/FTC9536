/* Copyright (c) 2017 FIRST. All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.lang.annotation.Target;

/** * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list */

@Autonomous(name="EncoderTest")
public class EncoderExample extends LinearOpMode {

    /* Declare OpMode members. */
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
    static final int    COUNTS_PER_INCH        = (int) Math.round(COUNTS_PER_INCH_DOUBLE);

    static final double DRIVE_SPEED            = 0.6;
    static final double TURN_SPEED             = 0.5;

    @Override
    public void runOpMode() {

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("auto started", "Starting at %7d :%7d :%7d :%7d",
                          frontLeft.getCurrentPosition(),
                          frontRight.getCurrentPosition(),
                          rearLeft.getCurrentPosition(),
                          rearRight.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // Step through each leg of the path
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        // input speed, four ints for target inches and a timeout
        encoderDrive(DRIVE_SPEED, 48, 48, 48, 48, 5.0);  // path1: Forward 47 Inches with 5 Sec timeout
        encoderDrive(TURN_SPEED, 12, -12, 12, -12, 4.0);  // path2: Turn Right 12 Inches with 4 Sec timeout
        encoderDrive(DRIVE_SPEED, -24, -24, -24, -24, 4.0);  // path3: Reverse 24 Inches with 4 Sec timeout

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    /* Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running. */
    public void encoderDrive(double speed,
                             int rearLeftInches, int rearRightInches,
                             int frontLeftInches, int frontRightInches,
                             double timeoutS) {
        int newRearLeftTarget;
        int newRearRightTarget;
        int newFrontLeftTarget;
        int newFrontRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newRearLeftTarget   = rearLeft.getCurrentPosition() + (int)(rearLeftInches * COUNTS_PER_INCH);
            newRearRightTarget  = rearRight.getCurrentPosition() + (int)(rearRightInches * COUNTS_PER_INCH);
            newFrontLeftTarget  = frontLeft.getCurrentPosition() + (int)(frontLeftInches * COUNTS_PER_INCH);
            newFrontRightTarget = frontRight.getCurrentPosition() + (int)(frontRightInches * COUNTS_PER_INCH);
            rearLeft.setTargetPosition(newRearLeftTarget);
            rearRight.setTargetPosition(newRearRightTarget);
            frontLeft.setTargetPosition(newFrontLeftTarget);
            frontRight.setTargetPosition(newFrontRightTarget);

            // Turn On RUN_TO_POSITION
            rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            rearLeft.setPower(Math.abs(speed));
            rearRight.setPower(Math.abs(speed));
            frontLeft.setPower(Math.abs(speed));
            frontRight.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                   (runtime.seconds() < timeoutS) &&
                   (rearLeft.isBusy() && rearRight.isBusy()) &&
                   (frontLeft.isBusy() && frontRight.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d :%7d :%7d", 
                                  newRearLeftTarget,  newRearRightTarget, newFrontLeftTarget,  newFrontRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d :%7d :%7d",
                                            rearLeft.getCurrentPosition(),
                                            rearRight.getCurrentPosition(),
                                            frontLeft.getCurrentPosition(),
                                            frontRight.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            rearLeft.setPower(0.0);
            rearRight.setPower(0.0);
            frontLeft.setPower(0.0);
            frontRight.setPower(0.0);

            // Turn off RUN_TO_POSITION
            rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }
}
