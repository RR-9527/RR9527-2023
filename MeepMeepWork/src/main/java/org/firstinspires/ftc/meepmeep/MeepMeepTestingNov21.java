package org.firstinspires.ftc.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.Constraints;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequenceBuilder;

import org.firstinspires.ftc.meepmeepkt.MeepMeepPersistence;
import org.jetbrains.annotations.NotNull;

/**
 * Deposit in the mid level in case of conflicts
 */
public class MeepMeepTestingNov21 {
    static final String PERSISTENCE_FILE = "./MeepMeepWork/meepmeep.properties";

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        new MeepMeepPersistence(meepMeep, PERSISTENCE_FILE)
            .restore();

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
            .setDimensions(15.67, 15.17)

            .setConstraints(new Constraints(
                49.0 * .8,
                41.986335266644225 * .8,
                Math.toRadians(567.98274) * .8,
                Math.toRadians(189.42045732283466) * .8,
                12.7165
            ))

            .followTrajectorySequence(MeepMeepTestingNov21::scheduleTrajectorySequence);

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_KAI_LIGHT)
            .setDarkMode(true)
            .setBackgroundAlpha(0.95f)
            .addEntity(myBot)
            .start();
    }

    @NotNull
    public static TrajectorySequence scheduleTrajectorySequence(DriveShim drive) {
        Pose2d startPose = new Pose2d(in(91), in(-119), rad(90));

        TrajectorySequenceBuilder builder = drive.trajectorySequenceBuilder(startPose);

        builder
            .splineTo(new Vector2d(in(91), in(-50)), rad(90))
            .splineTo(new Vector2d(in(AutoData.DEPOSIT_X), in(-50)), rad(135+70))

            .waitSeconds(AutoData.DEPOSIT_DELAY);

        for (int i = 0; i < 4; i++) {
            builder
                .setReversed(true)
                .splineTo(new Vector2d(in(AutoData.INTAKE_X), in(AutoData.INTAKE_Y)), rad(0))

                .waitSeconds(AutoData.INTAKE_DELAY)

                .setReversed(false)
                .splineTo(new Vector2d(in(AutoData.DEPOSIT_X), in(-50)), rad(135+70))

                .waitSeconds(AutoData.DEPOSIT_DELAY);
        }

        builder
            .back(in(40))
            .turn(rad(-25));

        int rand = (int) (Math.random() * 3) + 1;

        builder.addTemporalMarker(() ->
            System.out.println("Going to " + rand)
        );

        switch (rand) {
            case 1:
                builder.forward(in(85));
                break;
            case 3:
                builder.back(in(24));
                break;
            default:
                builder.forward(in(35));
                break;
        }

        return builder.build();
    }

    public static double rad(double degrees) {
        return Math.toRadians(degrees);
    }

    public static double in(double centimeters) {
        return centimeters * 0.3837008;
    }
}
