package org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

/**
 * The MAIN wrapper object for RoadRunner to improve interfacing with it.
 */
public class RoadrunnerWrapper {
    /**
     * Drivetrain object containing drivetrain parameters and motor objects
     */
    private SampleMecanumDrive drivetrain;

    /**
     * The starting pose of the robot
     */
    private Pose2d startPose;

    /**
     * The unit type to use in for Roadrunner <strong>ALL DISTANCES/POSITIONS</strong>
     */
    private RoadrunnerUnit unit;

    /**
     * The wrapper object for PathSequence
     */
    public SequenceWrapper sequenceWrapper;

    /**
     * The trajectory sequence object to follow and build
     */
    private TrajectorySequence trajectorySequence;

    /**
     * Conversion factor to convert metric values to inches. <strong>SHOULD NEVER CHANGE!</strong>
     */
    private static final double METRIC_CONVERSION_FACTOR = 0.3937008;


    /**
     * Basic constructor in which only the hardwareMap is passed. Note that it <strong>MUST</strong>
     * be passed. Pose is set to 0, 0, 0, and the unit type is assigned to IN (default)
     * @param hardwareMap the hardware map to pass to this object
     */
    public RoadrunnerWrapper(HardwareMap hardwareMap) {
        drivetrain = new SampleMecanumDrive(hardwareMap);
        unit = RoadrunnerUnit.IN;
        startPose = new Pose2d(0, 0, 0);
    }

    /**
     * Constructor which takes a custom unit but is still set to 0, 0, 0 for staring position.
     * @param hardwareMap the passed hardware map
     * @param unit the unit to work with
     */
    public RoadrunnerWrapper(HardwareMap hardwareMap, RoadrunnerUnit unit) {
        drivetrain = new SampleMecanumDrive(hardwareMap);
        this.unit = unit;
        startPose = new Pose2d(0, 0, 0);
    }

    /**
     * Constructor which takes a hardware map as well as position and uses the default unit of inches
     * @param hardwareMap the passed hardware map
     * @param x the starting x value
     * @param y the starting y value
     * @param heading the starting robot heading
     */
    public RoadrunnerWrapper(HardwareMap hardwareMap, double x, double y, double heading) {
        drivetrain = new SampleMecanumDrive(hardwareMap);
        unit = RoadrunnerUnit.IN;
        startPose = new Pose2d(toInches(x), toInches( y), Math.toRadians(heading));
        drivetrain.setPoseEstimate(startPose);
    }

    /**
     * Constructor which takes a hardware map as well as position and uses a custom passed unit
     * @param hardwareMap the passed hardware map
     * @param x the starting x value
     * @param y the starting y value
     * @param heading the starting robot heading
     * @param unit the unit to use for <strong>ALL DISTANCES/POSITIONS</strong>
     */
    public RoadrunnerWrapper(HardwareMap hardwareMap, double x, double y, double heading, RoadrunnerUnit unit) {
        drivetrain = new SampleMecanumDrive(hardwareMap);
        this.unit = unit;
        startPose = new Pose2d(toInches(x), toInches(y), Math.toRadians(heading));
        drivetrain.setPoseEstimate(startPose);
    }

    /**
     * Set the starting pose externally from a constructor
     * @param x the x starting position
     * @param y the y starting position
     * @param heading the starting heading
     */
    public void setStartPose(double x, double y, double heading) {
        startPose = new Pose2d(toInches(x), toInches(y), Math.toRadians(heading));
    }

    /**
     * Build trajectorySequence
     */
    public void build() {
        trajectorySequence = sequenceWrapper.trajectorySequenceBuilder.build();
    }

    /**
     * Follow the trajectorySequence object
     */
    public void follow() {
        drivetrain.followTrajectorySequence(trajectorySequence);
    }

    /**
     * Get the drivetrain object
     * @return the drivetrain object
     */
    public SampleMecanumDrive getDrivetrain() {
        return drivetrain;
    }

    /**
     * Get the starting pose of the object
     * @return the starting pose
     */
    public Pose2d getStartPose() {
        return startPose;
    }

    /**
     * Get the unit used in this object
     * @return the unit used
     */
    public RoadrunnerUnit getUnit() {
        return unit;
    }

    /**
     * Get the value in inches of a distance given the set unit
     * @param distance the input distance
     * @return the equivalent value in inches
     */
    private double toInches(double distance) {
        if(unit == RoadrunnerUnit.IN)
            return distance;
        if(unit == RoadrunnerUnit.FT)
            return distance*12;
        if(unit == RoadrunnerUnit.MM)
            return distance*METRIC_CONVERSION_FACTOR/10;
        if(unit == RoadrunnerUnit.CM)
            return distance*METRIC_CONVERSION_FACTOR;
        if(unit == RoadrunnerUnit.M)
            return distance*METRIC_CONVERSION_FACTOR*100;
        if(unit == RoadrunnerUnit.KM)
            return distance*METRIC_CONVERSION_FACTOR*1000;
        throw new RuntimeException("Incorrect distance unit specified into WrapperBuilder.toInches(...)!");
    }
}
