package org.firstinspires.ftc.meepmeep.depricated;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.Constraints;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import org.firstinspires.ftc.meepmeepkt.MeepMeepPersistence;
//import org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants;

public class MeepMeepTestingNov2 {
    static final String PERSISTENCE_FILE = "./MeepMeepWork/meepmeep.properties";

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        MeepMeepPersistence persistence = new MeepMeepPersistence(meepMeep, PERSISTENCE_FILE);
        persistence.restore();

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)

            .setDimensions(15.67, 15.17)

            .setConstraints(new Constraints(
                70.396,
                41.986335266644225,
                Math.toRadians(567.98274),
                Math.toRadians(189.42045732283466),
                12.7165
            ))

            .followTrajectorySequence(drive ->
                drive.trajectorySequenceBuilder(new Pose2d(toInches(91), toInches(-159), radians(90)))
                    // Deposit preload
                    .forward(toInches(135))
                    .turn(radians(45))

                    .turn(radians(30))

                    // Auto Cycle - #1
                    .setReversed(true)
                    .splineTo(new Vector2d(toInches(145), toInches(-30.5)), radians(0))
                    .setReversed(false)
                    .splineTo(new Vector2d(toInches(91), toInches(-24)), radians(140))

                    // Auto Cycle - #2
                    .setReversed(true)
                    .splineTo(new Vector2d(toInches(145), toInches(-30.5)), radians(0))
                    .setReversed(false)
                    .splineTo(new Vector2d(toInches(91), toInches(-24)), radians(140))

                    // Auto Cycle - #3
                    .setReversed(true)
                    .splineTo(new Vector2d(toInches(145), toInches(-30.5)), radians(0))
                    .setReversed(false)
                    .splineTo(new Vector2d(toInches(91), toInches(-24)), radians(140))

                    // Auto Cycle - #4
                    .setReversed(true)
                    .splineTo(new Vector2d(toInches(145), toInches(-30.5)), radians(0))
                    .setReversed(false)
                    .splineTo(new Vector2d(toInches(91), toInches(-24)), radians(140))

                    // Auto Cycle - #5
                    .setReversed(true)
                    .splineTo(new Vector2d(toInches(145), toInches(-30.5)), radians(0))
                    .setReversed(false)
                    .splineTo(new Vector2d(toInches(91), toInches(-24)), radians(140))

                    .turn(radians(40))
                    // Park in correct zone

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
