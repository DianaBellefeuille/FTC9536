/* Copyright (c) 2019 FIRST. All rights reserved.
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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

/**
 * This 2019-2020 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Skystone game elements.
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
 
// this code is the same setup as the vision code last year but now it doesnt work 
@Autonomous(name = "Vuforia2018Test")

public class Vuforia2018Test extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    /* IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes. */
    private static final String VUFORIA_KEY =
        "AaywR47/////AAABmVQeYRS8DUY1kVxjwhXK/edZHvWX1tFtr2/6Z6Rxtma1STc4wTTqbujPiyAY9Qrx4sHmJRewNVlGukjVN0Vy3O5gglozGdlIo4Za8eFmcqbEBxx+A12HUP2dy5M974hDjDwXSTAKqqlRjBVlHePhCZexhXF97/qL7NBv+Sp9oHoA9spThTwV6ZMFnlZgJaBfVD2+ahVQ0SncnFQ4P+Q9zbG0uPV/NtsiokmLyFzk02jmp3szrWpRvy/uyaGDNfQBNgkV8Sx1GMkQ8dSCWJgqCX3RPFl2UfZcukZ2rQGzZacW/NDc4bvKxKvZRpFXLEHu30LdZmkBBBD4dSJMX5+DDrZtTUos+nR9T/1j4f52gr1K";

    /** {@link #vuforia} is the variable we will use to store our instance of the Vuforia localization engine. */
    private VuforiaLocalizer vuforia;

    /** {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object Detection engine. */
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();
        }

        /** Wait for the game to begin */
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since the last time that call
                    // was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
    
                        // step through the list of recognitions and display boundary info.
                        int i = 0;
                        for (Recognition recognition : updatedRecognitions) {
                            // telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            // telemetry.addData(String.format("left,top (%d)", i), "%.03f , %.03f",
                            //                   recognition.getLeft(), recognition.getTop());
                            // telemetry.addData(String.format("right,bottom (%d)", i), "%.03f , %.03f",
                            //                   recognition.getRight(), recognition.getBottom());
                            
                            if (updatedRecognitions.size() == 3) {
                                int skystoneX = -1;
                                int stone1X = -1;
                                int stone2X = -1;
                                
                                if (recognition.getLabel().equals(LABEL_SECOND_ELEMENT)) {
                                    skystoneX = (int) recognition.getTop();
                                } if (recognition.getLabel().equals(LABEL_FIRST_ELEMENT)) {
                                    stone1X = (int) recognition.getTop();
                                } if ((skystoneX != -1) && (stone1X != -1)) {
                                    stone2X = (int) recognition.getTop();
                                }

                                //   example from last year
                                //   if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                //     goldMineralX = (int) recognition.getLeft();
                                //   } else if (silverMineral1X == -1) {
                                //     silverMineral1X = (int) recognition.getLeft();
                                //   } else {
                                //     silverMineral2X = (int) recognition.getLeft();
                          
                                // print location of the stones
                                telemetry.addData("stone2 coordinate", stone2X);
                                telemetry.addData("stone1 coordinate", stone1X);
                                telemetry.addData("skystone coordinate", skystoneX);
                            
                                if (skystoneX != stone1X && stone1X != stone2X && stone2X != skystoneX) {
                                    if (skystoneX < stone1X && skystoneX < stone2X) {
                                        telemetry.addData("skystoneX", "Left");
                                    } else if (skystoneX > stone1X && skystoneX > stone2X) {
                                        telemetry.addData("skystoneX", "Right");
                                    } else {
                                        telemetry.addData("skystoneX", "Center");
                                    }
                                }

                                else {
                                    telemetry.addLine("could not recognize skystone position");
                                }

                            }
                        telemetry.update();
                        }
                    }
                }
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }

    //  * Initialize the Vuforia localization engine.
    private void initVuforia() {
        //  * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    //  * Initialize the TensorFlow Object Detection engine.
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }
}
