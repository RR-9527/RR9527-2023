package org.firstinspires.ftc.teamcode.roadrunner.drive;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.roadrunner.util.Encoder;
import org.firstinspires.ftc.teamcode.util.ReverseDeadwheels;

import java.util.Arrays;
import java.util.List;

/*
 * Sample tracking wheel localizer implementation assuming the standard configuration:
 *
 *    /--------------\
 *    |     ____     |
 *    |     ----     |
 *    | ||        || |
 *    | ||        || |
 *    |              |
 *    |              |
 *    \--------------/
 *
 */
@Config
public class StandardTrackingWheelLocalizer extends ThreeTrackingWheelLocalizer {
    public static double X_MULTIPLIER = ((72 / 71.05925) + (72 / 71.31266) + (72 / 71.26539)) / 3;
    public static double Y_MULTIPLIER = ((72 / 70.62737) + (72 / 70.54758) + (72 / 70.00001)) / 3;

    public static double TICKS_PER_REV = 8192;
    public static double WHEEL_RADIUS = .688975; // in
    public static double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

    public static double LATERAL_DISTANCE = 12.37001; // in; distance between the left and right wheels
    public static double FORWARD_OFFSET = ((15.25 / 2) - 3); // in; offset of the lateral wheel

    private final Encoder leftEncoder, rightEncoder, frontEncoder;

    public StandardTrackingWheelLocalizer(HardwareMap hardwareMap) {
        super(Arrays.asList(
                new Pose2d(0, LATERAL_DISTANCE / 2, 0), // left
                new Pose2d(0, -LATERAL_DISTANCE / 2, 0), // right
                new Pose2d(FORWARD_OFFSET, 0, Math.toRadians(90)) // front
        ));

        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "FL"));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "BR"));
        frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "BL"));
//
//        leftEncoder.setDirection(Encoder.Direction.REVERSE);
//        rightEncoder.setDirection(Encoder.Direction.REVERSE);
//        frontEncoder.setDirection(Encoder.Direction.REVERSE);

        // TODO: reverse any encoders using Encoder.setDirection(Encoder.Direction.REVERSE)
    }

    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    private void updateInversion(){
        if(ReverseDeadwheels.frontReversed)
            frontEncoder.setDirection(Encoder.Direction.REVERSE);
        if(ReverseDeadwheels.rightReversed)
            rightEncoder.setDirection(Encoder.Direction.REVERSE);
        if(ReverseDeadwheels.leftReversed)
            leftEncoder.setDirection(Encoder.Direction.REVERSE);
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        updateInversion();
        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getCurrentPosition()) * X_MULTIPLIER,
                encoderTicksToInches(rightEncoder.getCurrentPosition()) * X_MULTIPLIER,
                encoderTicksToInches(frontEncoder.getCurrentPosition() * Y_MULTIPLIER)
        );
    }

    @NonNull
    @Override
    public List<Double> getWheelVelocities() {
        // TODO: If your encoder velocity can exceed 32767 counts / second (such as the REV Through Bore and other
        //  competing magnetic encoders), change Encoder.getRawVelocity() to Encoder.getCorrectedVelocity() to enable a
        //  compensation method
        updateInversion();

        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getCorrectedVelocity()) * X_MULTIPLIER,
                encoderTicksToInches(rightEncoder.getCorrectedVelocity()) * X_MULTIPLIER,
                encoderTicksToInches(frontEncoder.getCorrectedVelocity()) * Y_MULTIPLIER
        );
    }

    /*
     * Warning, adventurer. It is advised never to scroll past this point.
     */






























































































































































































































































































































//    @NonNull
//    @Override
//    public Pose2d getPoseEstimate() {
//        Pose2d pose = super.getPoseEstimate();
//        return new Pose2d(pose.getX(), pose.getY(), 2 * Math.PI - pose.getHeading());
//    }





















































































































































































}
