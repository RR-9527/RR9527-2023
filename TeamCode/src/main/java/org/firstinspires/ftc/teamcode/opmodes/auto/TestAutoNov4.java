package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.RobotConstants;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;

@Autonomous
public class TestAutoNov4 extends RougeBaseAuto {
    private Runnable armPosFunction;
    private Runnable wristPosFunction;

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();

        armPosFunction = arm::setToRestingPos; // TODO: Clean up this notation
        wristPosFunction = wrist::setToRestingPos;

        Scheduler.beforeEach(() -> {
            armPosFunction.run();
            wristPosFunction.run();
        });

        schedulePaths();

        waitForStart();

        Scheduler.start(this, () -> {
            arm.update(telemetry);
            lift.update(telemetry);
            wrist.update();
            drive.update();
        });
    }

    public void schedulePaths() {
        // TODO: Fix RoadrunnerPlus in order to accommodate asynchronous trajectories

        // Start moving the lift up to the middle position to be ready for depositing the preload
        lift.setHeight(RobotConstants.Lift.MID);

        Pose2d startPose = new Pose2d(in(91), in(-159), rad(90));

        drive.setPoseEstimate(startPose);

        TrajectorySequence trajSeq = drive.trajectorySequenceBuilder(startPose)
            .splineTo(new Vector2d(in(91), in(-50)), rad(90))

            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                // Start getting the lift ready while turning
                lift.setHeight(RobotConstants.Lift.HIGH);
                wristPosFunction = wrist::setToForwardsPos;
                armPosFunction = arm::setToForwardsPos;
            })
            .UNSTABLE_addTemporalMarkerOffset(0.5, () -> { // TODO: Tune depositing time
                // Deposit the cone while turning
                claw.openForDeposit();
            })

            .splineTo(new Vector2d(in(86), in(-22)), rad(135))


            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                // Prepare the robot for intaking
                claw.openForIntake();
                intake.enable();
                lift.setHeight(400); // TODO: I don't think this works... look into this later
                armPosFunction = arm::setToBackwardsPos;
                wristPosFunction = wrist::setToBackwardsPos;
            })


            // Auto Cycle #1
            .setReversed(true)
            .splineTo(new Vector2d(in(150), in(-28.5)), rad(0))
            .setReversed(false)
            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                // TODO: add a longer delay while intaking to actually be able to auto cycle
                claw.close();
            })
            .UNSTABLE_addTemporalMarkerOffset(0.25, () -> {
                armPosFunction = arm::setToForwardsPos;
                lift.setHeight(RobotConstants.Lift.HIGH);
                wristPosFunction = wrist::setToForwardsPos;
            })
            .splineTo(new Vector2d(in(91), in(-24)), rad(130))
            .UNSTABLE_addTemporalMarkerOffset(0.15, () -> {
                claw.openForDeposit();
            })
            .UNSTABLE_addTemporalMarkerOffset(0.35, () -> {
                // TODO: Increase delay, at the moment (11/3) the lift goes up and down too quickly,
                //  no time to deposit
                claw.openForIntake();
                intake.enable();
                lift.setHeight(RobotConstants.Lift.ZERO);
                armPosFunction = arm::setToBackwardsPos;

                wristPosFunction = wrist::setToBackwardsPos;

            })

//            // Auto Cycle #2
//            .setReversed(true)
//            .splineTo(new Vector2d(in(150), in(-28.5)), rad(0))
//            .setReversed(false)
//            .splineTo(new Vector2d(in(91), in(-24)), rad(140))
//
//            // Auto Cycle #3
//            .setReversed(true)
//            .splineTo(new Vector2d(in(150), in(-28.5)), rad(0))
//            .setReversed(false)
//            .splineTo(new Vector2d(in(91), in(-24)), rad(140))

            .build();


        drive.followTrajectorySequenceAsync(trajSeq);
    }

    private static double in(double cm) {
        return cm / 2.54;
    }

    private static double rad(double deg) {
        return deg * Math.PI / 180;
    }
}
