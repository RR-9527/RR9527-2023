package org.firstinspires.ftc.meepmeep.depricated;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.Constraints;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import org.firstinspires.ftc.meepmeepkt.MeepMeepPersistence;
//import org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants;

public class MeepMeepTestingNov1 {
    static final String PERSISTENCE_FILE = "./MeepMeepWork/meepmeep.properties";

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        MeepMeepPersistence persistence = new MeepMeepPersistence(meepMeep, PERSISTENCE_FILE);
        persistence.restore();

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)

            .setDimensions(15.67, 15.17)

            .setConstraints(new Constraints(
                49.396,
                41.986335266644225,
                Math.toRadians(612.2409366559378),
                Math.toRadians(178.19554133333332),
                12.7165
            ))

            .followTrajectorySequence(drive ->
                drive.trajectorySequenceBuilder(new Pose2d(toInches(91), toInches(-159), radians(90)))
                    // Deposit preload
                    .splineTo(new Vector2d(toInches(91), toInches(-30)), radians(90))
                    .turn(radians(-135))

                    // Move to auto cycle and cycle first
                    .turn(radians(30))
                    .splineToSplineHeading(new Pose2d(toInches(140), toInches(-33), radians(0)), radians(0))

                    .setReversed(true)
                    .splineTo(new Vector2d(toInches(91), toInches(-30)), radians(160))
                    .setReversed(false)
                    .turn(radians(-25))

                    // 2nd auto cycle
                    .turn(radians(30))
                    .splineToSplineHeading(new Pose2d(toInches(140), toInches(-33), radians(0)), radians(0))

                    .setReversed(true)
                    .splineTo(new Vector2d(toInches(91), toInches(-30)), radians(160))
                    .setReversed(false)
                    .turn(radians(-25))

                    // 3rd auto cycle
                    .turn(radians(30))
                    .splineToSplineHeading(new Pose2d(toInches(140), toInches(-33), radians(0)), radians(0))

                    .setReversed(true)
                    .splineTo(new Vector2d(toInches(91), toInches(-30)), radians(160))
                    .setReversed(false)
                    .turn(radians(-25))


                    // 4th auto cycle
                    .turn(radians(30))
                    .splineToSplineHeading(new Pose2d(toInches(140), toInches(-33), radians(0)), radians(0))

                    .setReversed(true)
                    .splineTo(new Vector2d(toInches(91), toInches(-30)), radians(160))
                    .setReversed(false)
                    .turn(radians(-25))

                    // 5th auto cycle
                    .turn(radians(30))
                    .splineToSplineHeading(new Pose2d(toInches(140), toInches(-33), radians(0)), radians(0))

                    .setReversed(true)
                    .splineTo(new Vector2d(toInches(91), toInches(-30)), radians(160))
                    .setReversed(false)
                    .turn(radians(-70)) // Turn further to drive back to park

                    .splineTo(new Vector2d(toInches(91), toInches(-92)), radians(-90))
                    .strafeRight(toInches(60))

                    .build()
            );


        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
            .setDarkMode(true)
            .setBackgroundAlpha(0.95f)
            .addEntity(myBot)
            .start();
    }

    public static double radians(double degrees) {
        return Math.toRadians(degrees);
    }

    // FROM CENTIMETERS
    public static double toInches(double centimeters) {
        return centimeters * 0.3837008;
    }
}
