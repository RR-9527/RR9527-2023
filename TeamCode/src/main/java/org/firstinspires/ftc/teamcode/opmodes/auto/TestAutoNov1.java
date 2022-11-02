package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerUnit;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.SequenceWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.WrapperBuilder;

@Disabled
@Autonomous
public class TestAutoNov1 extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();


        // TODO: Reverse this path, otherwise intaking/depositing is on the wrong side.
        RoadrunnerWrapper pathing = new RoadrunnerWrapper(hardwareMap, 91, -159, 90, RoadrunnerUnit.CM);
        pathing.sequenceWrapper = new SequenceWrapper(new WrapperBuilder(pathing)
            .splineTo(91, -30, 90)
            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                // Set lift height to max and extend arm
            })
            .turn(-135)
            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                // Deposit
            })

            // 2nd auto cycle

            .turn(30)
            .splineToSplineHeading(140, -33, 0, 0)
            .setReversed(true)
            .splineTo(91, -30, 160)
            .setReversed(false)

            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                // Set lift height to max and extend arm
            })

            .turn(-25)

            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                // Deposit
            })
        );

        pathing.build();
        pathing.follow();

    }
}
