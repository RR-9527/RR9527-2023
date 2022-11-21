package org.firstinspires.ftc.teamcode.opmodes.RogueBaseAuto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.components.arm.Arm;
import org.firstinspires.ftc.teamcode.components.claw.Claw;
import org.firstinspires.ftc.teamcode.components.distancesensor.ShortRangeSensor;
import org.firstinspires.ftc.teamcode.components.intake.Intake;
import org.firstinspires.ftc.teamcode.components.lift.Lift;
import org.firstinspires.ftc.teamcode.components.voltagescaler.VoltageScaler;
import org.firstinspires.ftc.teamcode.components.wrist.Wrist;
import org.firstinspires.ftc.teamcode.pipelines.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.pipelines.BasePoleDetector;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.drive.StandardTrackingWheelLocalizer;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

public abstract class RougeBaseAuto extends LinearOpMode {
    protected SampleMecanumDrive drive;

    protected Claw claw;
    protected Intake intake;
    protected Arm arm;
    protected Wrist wrist;
    protected Lift lift;
    protected VoltageScaler voltageScaler;
    protected ShortRangeSensor frontSensor;


    //************
    // Camera code
    //************
    private OpenCvCamera camera;
    private AprilTagDetectionPipeline aprilTagDetectionPipeline;
    private BasePoleDetector poleDetector;

    private int numFramesWithoutDetection = 0;


    // Lens intrinsics
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


    protected void initHardware() {
        telemetry.setMsTransmissionInterval(50);

        drive = new SampleMecanumDrive(hardwareMap);

        voltageScaler = new VoltageScaler(hardwareMap);
        claw = new Claw(hardwareMap);
        intake = new Intake(hardwareMap);
        arm = new Arm(hardwareMap);
        wrist = new Wrist(hardwareMap);
        lift = new Lift(hardwareMap, voltageScaler);
        frontSensor = new ShortRangeSensor(hardwareMap, "FRONT_SENSOR"); // TODO: Make sure this is what the name is!

        //***************************
        // Set up camera and pipeline
        //***************************
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {                               // RESOLUTION
                camera.startStreaming(1280, 960, OpenCvCameraRotation.UPSIDE_DOWN);
            }

            @Override
            public void onError(int errorCode) {
//                throw new RuntimeException("Error opening camera! Error code " + errorCode);
            }
        });

        poleDetector = new BasePoleDetector(telemetry);
    }

    public int waitForStartWithVision() {
        int lastIntID = -1;
        while (!opModeIsActive()) {
            ArrayList<AprilTagDetection> detections = aprilTagDetectionPipeline.getDetectionsUpdate();

            if (detections != null) {
                telemetry.addData("FPS", camera.getFps());
                telemetry.addData("Overhead ms", camera.getOverheadTimeMs());
                telemetry.addData("Pipeline ms", camera.getPipelineTimeMs());

                if (detections.size() == 0) {
                    numFramesWithoutDetection++;

                    if (numFramesWithoutDetection >= THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION) {
                        aprilTagDetectionPipeline.setDecimation(DECIMATION_LOW);
                    }
                } else {
                    numFramesWithoutDetection = 0;

                    if (detections.get(0).pose.z < THRESHOLD_HIGH_DECIMATION_RANGE_METERS) {
                        aprilTagDetectionPipeline.setDecimation(DECIMATION_HIGH);
                    }

                    for (AprilTagDetection detection : detections) {
                        lastIntID = detection.id;
                        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
                    }
                }

                telemetry.update();
            }
        }
        return lastIntID;
    }

    public void setPoleDetectorAsPipeline(){
        camera.setPipeline(poleDetector);
    }

    public double[] getPolePosition(){
        Pose2d currentPose = drive.getLocalizer().getPoseEstimate();
        double[] pos = poleDetector.getRepositionCoord(
            toCentimeters(currentPose.getX()),
            toCentimeters(currentPose.getY()),
            currentPose.getHeading(),
            frontSensor.getDistance()
            );

        // Do not move more than 15 cm in any one direction!
        if(Math.max(pos[0], pos[1]) > 15){
            telemetry.addData("Uh oh! Tried to return a pole position greater than 15 cm away!",
                Math.max(pos[0], pos[1]));
            return new double[]{0, 0};
        }
        return pos;
    }

    private double toCentimeters(double inches){
        return inches*2.54;
    }
}
