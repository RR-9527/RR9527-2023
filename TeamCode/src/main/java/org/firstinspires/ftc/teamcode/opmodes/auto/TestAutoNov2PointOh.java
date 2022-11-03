package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerUnit;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.SequenceWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.WrapperBuilder;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;

import java.lang.reflect.Field;

public class TestAutoNov2PointOh extends RougeBaseAuto {
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        waitForStart();

        Scheduler.beforeEach(() -> {
            arm.setToRestingPos();
            wrist.setToRestingPos();
        });

        schedulePaths();

        Scheduler.start(this, () -> {
            arm.update(telemetry);
            lift.update(telemetry);
            wrist.update();
            drive.update();
        });
    }

    public void schedulePaths() {
        RoadrunnerWrapper pathing = new RoadrunnerWrapper(hardwareMap, 91, -159, 90, RoadrunnerUnit.CM);
        pathing.sequenceWrapper = new SequenceWrapper(new WrapperBuilder(pathing)
            .forward(135)
            .turn(45)
            .turn(30)

            // Auto Cycle #1
            .setReversed(true)
            .splineTo(150, -30, 0)
            .setReversed(false)
            .splineTo(91, -24, 140)

            // Auto Cycle #2
            .setReversed(true)
            .splineTo(150, -30, 0)
            .setReversed(false)
            .splineTo(91, -24, 140)

            // Auto Cycle #3
            .setReversed(true)
            .splineTo(150, -30, 0)
            .setReversed(false)
            .splineTo(91, -24, 140)

            .turn(-50)
            .back(12)

            .addTemporalMarker(0.5, () -> {
                wrist.setToBackwardsPos();
                wrist.update();
            })

            .addTemporalMarker(2.3, () -> {
                claw.openForDeposit();
            })

            .addTemporalMarker(2.4, () -> {
                claw.openForDeposit();
            })
        );

        try {
            pathing.build();

            Field f = pathing.getClass().getField("trajectorySequence");
            f.setAccessible(true);

            TrajectorySequence sequence = (TrajectorySequence) f.get(pathing);
            drive.followTrajectorySequenceAsync(sequence);
        } catch (Exception ignored) {}
    }
}
