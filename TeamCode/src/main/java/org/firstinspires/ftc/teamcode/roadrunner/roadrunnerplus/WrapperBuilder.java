package org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.MarkerCallback;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder;

/**
 * Build the Roadrunner wrapper which improves the following:
 * Multiple unit types (which are just converted to inches)
 * Degrees instead of radians - meaning you won't forget to
 * convert to radians
 * For use ONLY by Team 7797 Victorian Voltage.
 */
public class WrapperBuilder {
    public TrajectorySequenceBuilder trajectorySequenceBuilder;
    private RoadrunnerUnit units;
    private static final double METRIC_CONVERSION_FACTOR = 0.3937008;

    /**
     * Mandatory constructor for WrapperBuilder. A drive and starting pose object is required!
     * @param rrWrapper the RoadrunnerWrapper object being passed
     */
    public WrapperBuilder(RoadrunnerWrapper rrWrapper) {
        trajectorySequenceBuilder = rrWrapper.getDrivetrain()
                .trajectorySequenceBuilder(rrWrapper.getStartPose());
        this.units = rrWrapper.getUnit();
    }

    /**
     * Move a distance forward
     * @param distance the distance to travel in, either in inches or the unit you specify
     * @return this object to use in a builder design pattern
     */
    public WrapperBuilder forward(double distance) {
        trajectorySequenceBuilder.forward(toInches(distance));
        return this;
    }

    /**
     * Move a distance back
     * @param distance the distance to travel in, either in inches or the unit you specify
     * @return this object to use in a builder design pattern
     */
    public WrapperBuilder back(double distance) {
        trajectorySequenceBuilder.back(toInches(distance));
        return this;
    }

    /**
     * Move a distance left
     * @param distance the distance to travel in, either in inches or the unit you specify
     * @return this object to use in a builder design pattern
     */
    public WrapperBuilder strafeLeft(double distance) {
        trajectorySequenceBuilder.strafeLeft(toInches(distance));
        return this;
    }

    /**
     * Move a distance right
     * @param distance the distance to travel in, either in inches or the unit you specify
     * @return this object to use in a builder design pattern
     */
    public WrapperBuilder strafeRight(double distance) {
        trajectorySequenceBuilder.strafeRight(toInches(distance));
        return this;
    }

    /**
     * Strafe/move in a line to a certain location.
     * @param x is the x coordinate to strafe to
     * @param y is the y coordinate to strafe to
     * @return this object to use in a builder design pattern
     */
    public WrapperBuilder lineTo(double x, double y) {
        trajectorySequenceBuilder.lineTo(new Vector2d(toInches(x), toInches(y)));
        return this;
    }

    /**
     * Move in a line to a coordinate and end at a certain location, the heading changes linearly
     * @param x the x coordinate of where to move to
     * @param y the y coordinate of where to move to
     * @param endHeading the end heading the robot should be at
     * @return this object to use in a builder design pattern
     */
    public WrapperBuilder lineToLinearHeading(double x, double y, double endHeading) {
        trajectorySequenceBuilder.lineToLinearHeading(new Pose2d(
                toInches(x), toInches(y), Math.toRadians(endHeading)));
        return this;
    }

    /**
     * Move in a line to a coordinate and end at a certain location, the heading changes according
     * to a spline function
     * @param x the x coordinate of where to move to
     * @param y the y coordinate of where to move to
     * @param endHeading the end heading the robot should be at
     * @return this object to use in a builder design pattern
     */
    public WrapperBuilder lineToSplineHeading(double x, double y, double endHeading) {
        trajectorySequenceBuilder.lineToSplineHeading(new Pose2d(
                toInches(x), toInches(y), Math.toRadians(endHeading)));
        return this;
    }


    /**
     * Spline to a given location
     * @param x the x coordinate of that location
     * @param y the y coordinate of that location
     * @param endHeading the end heading for the robot to be at
     * @return this object to use in a builder design pattern
     */
    public WrapperBuilder splineTo(double x, double y, double endHeading) {
        trajectorySequenceBuilder.splineTo(new Vector2d(toInches(x), toInches(y)),
                Math.toRadians(endHeading));
        return this;
    }

    /**
     * Spline to a given location but stay at the original heading the entire way through
     * @param x the x coordinate of that location
     * @param y the y coordinate of that location
     * @param endTangent the end heading for the robot to be at
     * @return this object to use in a builder design pattern
     */

    public WrapperBuilder splineToConstantHeading(double x, double y, double endTangent) {
        trajectorySequenceBuilder.splineToConstantHeading(new Vector2d(toInches(x), toInches(y)),
                Math.toRadians(endTangent));
        return this;
    }

    /**
     * Spline to a given location but end at some other heading than what you started at, according
     * to a linear function
     * @param x the x coordinate of that location
     * @param y the y coordinate of that location
     * @param endHeading the end heading for the robot to be at
     * @param endTangent the angle for the end of the spline to be at (not the robot angle like
     *                   endHeading!)
     * @return this object to use in a builder design pattern
     */
    public WrapperBuilder splineToLinearHeading(double x, double y, double endHeading, double endTangent) {
        trajectorySequenceBuilder.splineToLinearHeading(new Pose2d(toInches(x), toInches(y),
                        Math.toRadians(endHeading)),
                Math.toRadians(endTangent));
        return this;
    }

    /**
     * Spline to a given location but end at some other heading than what you started at, according
     * to a spline function
     * @param x the x coordinate of that location
     * @param y the y coordinate of that location
     * @param endHeading the end heading for the robot to be at
     * @param endTangent the angle for the end of the spline to be at (not the robot angle like
     *                   endHeading!)
     * @return this object to use in a builder design pattern
     */
    public WrapperBuilder splineToSplineHeading(double x, double y, double endHeading, double endTangent) {
        trajectorySequenceBuilder.splineToSplineHeading(new Pose2d(toInches(x), toInches(y),
                        Math.toRadians(endHeading)),
                Math.toRadians(endTangent));
        return this;
    }

    /**
     * Turn for a certain angle
     * @param angle angle to turn at
     * @return this object to use in a builder design pattern
     */
    public WrapperBuilder turn(double angle) {
        trajectorySequenceBuilder.turn(Math.toRadians(angle));
        return this;
    }

    public WrapperBuilder setReversed(boolean reversed) {
        trajectorySequenceBuilder.setReversed(reversed);
        return this;
    }

    /**
     * Add a temporal marker for a given time into the whole path sequence
     * @param time the time to execute the temporal marker
     * @param callback the temporal marker
     * @return this object to use in a builder design pattern
     */
    public WrapperBuilder addTemporalMarker(double time, MarkerCallback callback) {
        trajectorySequenceBuilder.addTemporalMarker(0.0, time, callback);
        return this;
    }

    /**
     * Add a temporal marker for a given time into the <STRONG>CURRENT PAT</STRONG>
     * @param offset the time to execute the temporal marker
     * @param callback the temporal marker
     * @return this object to use in a builder design pattern
     */
    public WrapperBuilder UNSTABLE_addTemporalMarkerOffset(double offset, MarkerCallback callback) {
        trajectorySequenceBuilder.UNSTABLE_addTemporalMarkerOffset(
                trajectorySequenceBuilder.currentDuration + offset, callback);
        return this;
    }


    /**
     * Build this object
     * @return this object to be used in a builder design pattern
     */
    public WrapperBuilder build() {
        trajectorySequenceBuilder.build();
        return this;
    }

    /**
     * Convert a distance to inches based on the set unit
     * @param distance the input distances
     * @return the distance in inches
     */
    private double toInches(double distance) {
        if(units == RoadrunnerUnit.IN)
            return distance;
        if(units == RoadrunnerUnit.FT)
            return distance*12;
        if(units == RoadrunnerUnit.MM)
            return distance*METRIC_CONVERSION_FACTOR/10;
        if(units == RoadrunnerUnit.CM)
            return distance*METRIC_CONVERSION_FACTOR;
        if(units == RoadrunnerUnit.M)
            return distance*METRIC_CONVERSION_FACTOR*100;
        if(units == RoadrunnerUnit.KM)
            return distance*METRIC_CONVERSION_FACTOR*1000;
        throw new RuntimeException("Incorrect distance unit specified into WrapperBuilder.toInches(...)!");
    }
}
