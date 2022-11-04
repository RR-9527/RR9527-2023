package org.firstinspires.ftc.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.Constraints;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import org.firstinspires.ftc.meepmeepkt.MeepMeepPersistence;
//import org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants;

public class MeepMeepTestingNov4 {
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
                drive.trajectorySequenceBuilder(new Pose2d(in(91), in(-159), rad(90)))
                    // Deposit preload
                    .splineTo(new Vector2d(in(91), in(-50)), rad(90))
                    .splineTo(new Vector2d(in(86), in(-22)), rad(135))


                    // Auto Cycle - #1
                    .setReversed(true)
                    .splineTo(new Vector2d(in(145), in(-30.5)), rad(0))
                    .setReversed(false)
                    .splineTo(new Vector2d(in(86), in(-22)), rad(140))

                    // Auto Cycle - #2
                    .setReversed(true)
                    .splineTo(new Vector2d(in(145), in(-30.5)), rad(0))
                    .setReversed(false)
                    .splineTo(new Vector2d(in(86), in(-22)), rad(140))

                    // Auto Cycle - #3
                    .setReversed(true)
                    .splineTo(new Vector2d(in(145), in(-30.5)), rad(0))
                    .setReversed(false)
                    .splineTo(new Vector2d(in(86), in(-22)), rad(140))

                    .turn(rad(40))
                    // Park in correct zone

                    .build()

            );


        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_KAI_LIGHT)
            .setDarkMode(true)
            .setBackgroundAlpha(0.95f)
            .addEntity(myBot)
            .start();
    }

    public static double rad(double degrees) {
        return Math.toRadians(degrees);
    }

    // FROM CENTIMETERS
    public static double in(double centimeters) {
        return centimeters * 0.3837008;
    }
}
