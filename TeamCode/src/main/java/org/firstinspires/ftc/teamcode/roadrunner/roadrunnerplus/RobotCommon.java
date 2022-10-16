package org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus;

import android.annotation.SuppressLint;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.RevIMU;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.pipelines.AprilTagDetectionPipeline;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;


abstract public class RobotCommon extends LinearOpMode {
    /**
     * Roadrunner wrapped object - only change the units here if something else is being used
     */
    protected RoadrunnerWrapper pathing;

    /**
     * Name object for the webcam
     */
    private WebcamName webcameraName;

    /**
     * The actual camera object to apply a pipeline to
     */
    private OpenCvCamera camera;

    /**
     * Pipeline object
     */
    // Add pipeline - pipeline should be private (only used here)
    private AprilTagDetectionPipeline pipeline;

    // CAMERA INFORMATION
    // Units are in pixels
    private static final double fx = 578.272;
    private static final double fy = 578.272;
    private static final double cx = 402.145;
    private static final double cy = 221.506;

    // UNITS ARE METERS
    private static final double tagsize = 0.166;

    private static final float DECIMATION_HIGH = 3;
    private static final float DECIMATION_LOW = 2;
    private static final float THRESHOLD_HIGH_DECIMATION_RANGE_METERS = 1.0f;
    private static final int THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION = 4;
    private int numFramesWithoutDetection = 0;

    protected int detectedNumber = 1;

    protected MecanumDrive teleopDrivebase;
    protected RevIMU imu;
    protected GamepadEx game_pad1;
    protected GamepadEx game_pad2;

    protected boolean FIELD_CENTRIC = false;

    /**
     * Method to execute the init phase. Add vision code here
     */
    @SuppressLint("DefaultLocale")
    protected void initialize() {
        pipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);
        telemetry.setMsTransmissionInterval(50);

        ArrayList<AprilTagDetection> detections = pipeline.getDetectionsUpdate();

        initHardware();
        initCamera();
        pathing.build();

        // Add your custom initialization loop here
        do {
            if(detections != null) {
                telemetry.addData("FPS", camera.getFps());
                telemetry.addData("Overhead ms", camera.getOverheadTimeMs());
                telemetry.addData("Pipeline ms", camera.getPipelineTimeMs());

                if (detections.size() == 0) {
                    numFramesWithoutDetection++;

                    if (numFramesWithoutDetection >= THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION) {
                        pipeline.setDecimation(DECIMATION_LOW);
                    }
                } else {
                    numFramesWithoutDetection = 0;

                    if (detections.get(0).pose.z < THRESHOLD_HIGH_DECIMATION_RANGE_METERS) {
                        pipeline.setDecimation(DECIMATION_HIGH);
                    }

                    for (AprilTagDetection detection : detections) {
                        detectedNumber = detection.id;
                        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
                    }
                }
                telemetry.update();
            }

        }
        while(!opModeIsActive());
    }

    /**
     * Method to execute after init phase. Should probably not need to be changed
     */
    protected void run() {
        pathing.follow();

        // Add any other runtime code here - you probably won't though

    }

    /**
     * Used for teleop to drive in robot-centric or field-centric mode (default is robot-centric)
     */
    protected void updateDrivetrain(){
        if (!FIELD_CENTRIC) {
            teleopDrivebase.driveRobotCentric(
                    game_pad1.getLeftX(),
                    game_pad1.getLeftY(),
                    game_pad1.getRightX(),
                    false
            );
        } else {
            teleopDrivebase.driveFieldCentric(
                    game_pad1.getLeftX(),
                    game_pad1.getLeftY(),
                    game_pad1.getRightX(),
                    imu.getRotation2d().getDegrees(),   // gyro value passed in here must be in degrees
                    false
            );
        }
    }


    /**
     * Method to init non-drivetrain hardware before entering the init phase.
     */
    private void initHardware() {
        // constructor takes in frontLeft, frontRight, backLeft, backRight motors
        // IN THAT ORDER
        teleopDrivebase = new MecanumDrive(
                new Motor(hardwareMap, "FL", Motor.GoBILDA.RPM_312),
                new Motor(hardwareMap, "FR", Motor.GoBILDA.RPM_312),
                new Motor(hardwareMap, "BL", Motor.GoBILDA.RPM_312),
                new Motor(hardwareMap, "BR", Motor.GoBILDA.RPM_312)
        );

        imu = new RevIMU(hardwareMap);
        imu.init();

        // the extended gamepad object
        game_pad1 = new GamepadEx(this.gamepad1);
        game_pad2 = new GamepadEx(this.gamepad2);

    }

    /**
     * Initialize the camera and set its properties.
     * Postcondition: camera has been properly assigned
     */
    private void initCamera(){
        // Build the camera objects - most likely you won't need to change this, but if you've renamed your webcam then you will!
//        WebcamName name = hardwareMap.get(WebcamName.class, "Webcam 1");
        webcameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraMonitorViewId = hardwareMap.appContext.getResources()
                .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());


        // Create a new camera object in openCV
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcameraName, cameraMonitorViewId);

        camera.setPipeline(pipeline);

        // Start the camera stream or throws an error
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                telemetry.update();
                camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode) {
                throw new RuntimeException("Error in camera initialization! Error code "+errorCode);
            }
        });
    }

    /**
     * Method to set the starting position of the robot.
     * Moved to the end because this method should NOT be changed.
     * @param x the starting x position in the units specified
     * @param y the starting y position in the units specified
     * @param heading the starting heading - will convert to radians
     *                <strong>LATER, IN RoadrunnerWrapper!!!!</strong>
     */
    protected void setStartPose(double x, double y, double heading) {
        pathing.setStartPose(x, y, heading);
    }
}
