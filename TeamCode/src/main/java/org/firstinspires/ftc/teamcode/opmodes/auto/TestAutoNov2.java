package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerUnit;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.SequenceWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.WrapperBuilder;

@Autonomous
public class TestAutoNov2 extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();

        RoadrunnerWrapper pathing = new RoadrunnerWrapper(hardwareMap, 91, -159, -90, RoadrunnerUnit.CM);
        pathing.sequenceWrapper = new SequenceWrapper(new WrapperBuilder(pathing)
            .back(135)
            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                // Extend lift and arm
            })
            .turn(-135)
            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                // Deposit, bring lift down
            })

            // Auto Cycle #1
            .setReversed(true)
            .splineTo(145, -30.5, 0)
            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                // Intake
            })
            .UNSTABLE_addTemporalMarkerOffset(0.25, () -> {
                // Extend lift
            })
            .setReversed(false)
            .splineTo(91, -24, 140)
            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                // Deposit, bring lift down
            })

            // Auto Cycle #2
            .setReversed(true)
            .splineTo(145, -30.5, 0)
            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                // Intake
            })
            .UNSTABLE_addTemporalMarkerOffset(0.25, () -> {
                // Extend lift
            })
            .setReversed(false)
            .splineTo(91, -24, 140)
            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                // Deposit, bring lift down
            })

            // Auto Cycle #3
            .setReversed(true)
            .splineTo(145, -30.5, 0)
            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                // Intake
            })
            .UNSTABLE_addTemporalMarkerOffset(0.25, () -> {
                // Extend lift
            })
            .setReversed(false)
            .splineTo(91, -24, 140)
            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                // Deposit, bring lift down
            })

            // Move to park, TODO: Implement parking in the correct zones
            .turn(-50)
            .back(70)
        );

        pathing.build();
        pathing.follow();

    }
}
