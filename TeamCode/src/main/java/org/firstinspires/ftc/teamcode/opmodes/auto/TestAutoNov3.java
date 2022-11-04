package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerUnit;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.SequenceWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.WrapperBuilder;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.util.RobotConstants;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;

import java.lang.reflect.Field;

@Autonomous
public class TestAutoNov3 extends RougeBaseAuto {
    private Runnable armPosFunction;
    private Runnable wristPosFunction;

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();

        armPosFunction = arm::setToRestingPos;
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
        lift.setHeight(RobotConstants.Lift.MID);

        Pose2d startPose = new Pose2d(in(91), in(-159), rad(90));

        drive.setPoseEstimate(startPose);

        TrajectorySequence trajSeq = drive.trajectorySequenceBuilder(startPose)
            .forward(in(135))
            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                lift.setHeight(RobotConstants.Lift.HIGH);
                wristPosFunction = wrist::setToForwardsPos;
                armPosFunction = arm::setToForwardsPos;
            })
            .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
                claw.openForDeposit();
            })
            .turn(rad(60))
            .forward(in(4))
            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                claw.openForIntake();
                intake.enable();
                lift.setHeight(400);
                armPosFunction = arm::setToBackwardsPos;
                wristPosFunction = wrist::setToBackwardsPos;
            })
            .back(in(4))
            .turn(rad(30))

            // Auto Cycle #1
            .setReversed(true)
            .splineTo(new Vector2d(in(150), in(-28.5)), rad(0))
            .setReversed(false)
            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
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
