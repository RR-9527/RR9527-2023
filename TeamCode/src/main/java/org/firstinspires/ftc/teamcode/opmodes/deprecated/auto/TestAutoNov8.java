package org.firstinspires.ftc.teamcode.opmodes.deprecated.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.opmodes.auto.AutoData;
import org.firstinspires.ftc.teamcode.opmodes.auto.RougeBaseAuto;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.RobotConstants;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;

@Disabled
@Autonomous
public class TestAutoNov8 extends RougeBaseAuto {
    private Runnable armPosFunction;
    private Runnable wristPosFunction;

    private int signalZone;

    // Variable added to signal when to start the parking sequence
    private boolean startParking = false;

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();

        // Park in zone 1
        TrajectorySequence parkTraj1 = drive.trajectorySequenceBuilder(new Pose2d())
            .back(in(50))
            .build();

        // Park in zone 3
        TrajectorySequence parkTraj3 = drive.trajectorySequenceBuilder(new Pose2d())
            .forward(in(50))
            .build();

        armPosFunction = arm::setToRestingPos; // TODO: Clean up this notation
        wristPosFunction = wrist::setToRestingPos;

        Scheduler.beforeEach(() -> {
            armPosFunction.run();
            wristPosFunction.run();
        });

        schedulePaths();

        // Added this during init
        do {
            int detectedZone = waitForStartWithVision();
            if (detectedZone != -1)
                signalZone = detectedZone;
            telemetry.addData("Signal zone detected", signalZone);
            telemetry.update();
        }
        while (!opModeIsActive());

        Scheduler.start(this, () -> {
            arm.update(telemetry, true);
            lift.update(telemetry);
            wrist.update();
            drive.update();
            telemetry.addData("Drive is busy", drive.isBusy());

            if(startParking){
                startParking = false;
                if(signalZone == 1)
                    drive.followTrajectorySequence(parkTraj1);
                else if(signalZone == 3)
                    drive.followTrajectorySequence(parkTraj3);
            }

            telemetry.update();
        });
    }

    public void schedulePaths() {
        // TODO: Fix RoadrunnerPlus in order to accommodate asynchronous trajectories

        Pose2d startPose = new Pose2d(in(91), in(-159), rad(90));

        drive.setPoseEstimate(startPose);

        TrajectorySequence trajSeq = drive.trajectorySequenceBuilder(startPose)
            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                // Start getting the lift ready while turning
                lift.setHeight(RobotConstants.Lift.HIGH);
                wristPosFunction = wrist::setToForwardsPos;

                armPosFunction = arm::setToForwardsPos;
            })

            .splineTo(new Vector2d(in(91), in(-50)), rad(90))
            .splineTo(new Vector2d(in(AutoData.DEPOSIT_X), in(AutoData.DEPOSIT_Y)), rad(AutoData.DEPOSIT_ANGLE))
            .UNSTABLE_addTemporalMarkerOffset(AutoData.LOWER_OFFSET, () -> lift.setHeight(RobotConstants.Lift.HIGH - AutoData.DEPOSIT_DROP_AMOUNT))
            .UNSTABLE_addTemporalMarkerOffset(AutoData.DEPOSIT_OFFSET, () -> {
                // Deposit the cone while turning
                claw.openForDeposit();
            })

            .waitSeconds(AutoData.DEPOSIT_DELAY)

            .UNSTABLE_addTemporalMarkerOffset(AutoData.RETRACT_OFFSET, () -> {
                // Prepare the robot for intaking
                claw.openForIntake();
                intake.enable();
                lift.setHeight(RobotConstants.Lift.AUTO_INTAKE_1);
                armPosFunction = arm::setToBackwardsPos;
                wristPosFunction = wrist::setToBackwardsPos;
            })

            // Auto Cycle #1
            .setReversed(true)
            .splineTo(new Vector2d(in(AutoData.INTAKE_X), in(AutoData.INTAKE_Y)), rad(0))
            .setReversed(false)

            .UNSTABLE_addTemporalMarkerOffset(AutoData.CLAW_CLOSE_OFFSET, () -> claw.close())

            .UNSTABLE_addTemporalMarkerOffset(AutoData.INTAKE_LIFT_OFFSET, () -> {
                lift.setHeight(RobotConstants.Lift.HIGH);
                wristPosFunction = wrist::setToForwardsPos;
            })

            .UNSTABLE_addTemporalMarkerOffset(0.1, () -> {
                armPosFunction = arm::setToForwardsPos;
                wristPosFunction = wrist::setToForwardsPos;
            })
            .waitSeconds(AutoData.INTAKE_DELAY)


            .splineTo(new Vector2d(in(AutoData.DEPOSIT_X), in(AutoData.DEPOSIT_Y)), rad(AutoData.DEPOSIT_ANGLE-1))
            .UNSTABLE_addTemporalMarkerOffset(AutoData.LOWER_OFFSET, () -> lift.setHeight(RobotConstants.Lift.HIGH - AutoData.DEPOSIT_DROP_AMOUNT))

            .UNSTABLE_addTemporalMarkerOffset(AutoData.DEPOSIT_OFFSET, () -> {
                // Deposit the cone while turning
                claw.openForDeposit();
            })

            .waitSeconds(AutoData.DEPOSIT_DELAY)

            .UNSTABLE_addTemporalMarkerOffset(AutoData.RETRACT_OFFSET, () -> {
                // Prepare the robot for intaking
                claw.openForIntake();
                intake.enable();
                lift.setHeight(RobotConstants.Lift.AUTO_INTAKE_1);
                armPosFunction = arm::setToBackwardsPos;
                wristPosFunction = wrist::setToBackwardsPos;
            })


            // Auto Cycle #2
            .setReversed(true)
            .UNSTABLE_addTemporalMarkerOffset(AutoData.CLAW_CLOSE_OFFSET, () -> claw.close())
            .splineTo(new Vector2d(in(AutoData.INTAKE_X), in(AutoData.INTAKE_Y)), rad(0))
            .setReversed(false)
            .UNSTABLE_addTemporalMarkerOffset(AutoData.INTAKE_LIFT_OFFSET, () -> {
                lift.setHeight(RobotConstants.Lift.HIGH);
                wristPosFunction = wrist::setToForwardsPos;
            })

            .UNSTABLE_addTemporalMarkerOffset(0.1, () -> {
                armPosFunction = arm::setToForwardsPos;
                wristPosFunction = wrist::setToForwardsPos;
            })
            .waitSeconds(AutoData.INTAKE_DELAY)

            .splineTo(new Vector2d(in(AutoData.DEPOSIT_X), in(AutoData.DEPOSIT_Y)), rad(AutoData.DEPOSIT_ANGLE-2))
            .UNSTABLE_addTemporalMarkerOffset(AutoData.LOWER_OFFSET, () -> lift.setHeight(RobotConstants.Lift.HIGH - AutoData.DEPOSIT_DROP_AMOUNT))
            .UNSTABLE_addTemporalMarkerOffset(AutoData.DEPOSIT_OFFSET, () -> {
                // Deposit the cone while turning
                claw.openForDeposit();
            })

            .waitSeconds(AutoData.DEPOSIT_DELAY)

            .UNSTABLE_addTemporalMarkerOffset(AutoData.RETRACT_OFFSET, () -> {
                // Prepare the robot for intaking
                claw.openForIntake();
                intake.enable();
                lift.setHeight(RobotConstants.Lift.AUTO_INTAKE_2);
                armPosFunction = arm::setToBackwardsPos;

                wristPosFunction = wrist::setToBackwardsPos;
            })


            // Auto Cycle #3
            .setReversed(true)
            .splineTo(new Vector2d(in(AutoData.INTAKE_X), in(AutoData.INTAKE_Y)), rad(0))

            .setReversed(false)
            .UNSTABLE_addTemporalMarkerOffset(AutoData.CLAW_CLOSE_OFFSET, () -> claw.close())
            .UNSTABLE_addTemporalMarkerOffset(AutoData.INTAKE_LIFT_OFFSET, () -> {
                lift.setHeight(RobotConstants.Lift.HIGH);
                wristPosFunction = wrist::setToForwardsPos;
            })

            .UNSTABLE_addTemporalMarkerOffset(0.1, () -> {
                armPosFunction = arm::setToForwardsPos;
                wristPosFunction = wrist::setToForwardsPos;
            })
            .waitSeconds(AutoData.INTAKE_DELAY)
            .splineTo(new Vector2d(in(AutoData.DEPOSIT_X), in(AutoData.DEPOSIT_Y)), rad(AutoData.DEPOSIT_ANGLE-3))
            .UNSTABLE_addTemporalMarkerOffset(AutoData.LOWER_OFFSET, () -> lift.setHeight(RobotConstants.Lift.HIGH - AutoData.DEPOSIT_DROP_AMOUNT))
            .UNSTABLE_addTemporalMarkerOffset(AutoData.DEPOSIT_OFFSET, () -> {
                // Deposit the cone while turning
                claw.openForDeposit();
            })

            .waitSeconds(AutoData.DEPOSIT_DELAY)

            .UNSTABLE_addTemporalMarkerOffset(AutoData.RETRACT_OFFSET, () -> {
                // Prepare the robot for intaking
                claw.openForIntake();
                intake.enable();
                lift.setHeight(RobotConstants.Lift.AUTO_INTAKE_3);

                armPosFunction = arm::setToBackwardsPos;
                wristPosFunction = wrist::setToBackwardsPos;
            })

            // Auto Cycle #4
            .setReversed(true)
            .splineTo(new Vector2d(in(AutoData.INTAKE_X), in(AutoData.INTAKE_Y)), rad(0))

            .setReversed(false)
            .UNSTABLE_addTemporalMarkerOffset(AutoData.CLAW_CLOSE_OFFSET, () -> claw.close())
            .UNSTABLE_addTemporalMarkerOffset(AutoData.INTAKE_LIFT_OFFSET, () -> {
                lift.setHeight(RobotConstants.Lift.HIGH);
                wristPosFunction = wrist::setToForwardsPos;
            })

            .UNSTABLE_addTemporalMarkerOffset(0.1, () -> {
                armPosFunction = arm::setToForwardsPos;
                wristPosFunction = wrist::setToForwardsPos;
            })
            .waitSeconds(AutoData.INTAKE_DELAY)

            .splineTo(new Vector2d(in(AutoData.DEPOSIT_X), in(AutoData.DEPOSIT_Y)), rad(AutoData.DEPOSIT_ANGLE-4))
            .UNSTABLE_addTemporalMarkerOffset(AutoData.LOWER_OFFSET, () -> lift.setHeight(RobotConstants.Lift.HIGH - AutoData.DEPOSIT_DROP_AMOUNT))
            .UNSTABLE_addTemporalMarkerOffset(AutoData.DEPOSIT_OFFSET, () -> {
                // Deposit the cone while turning
                claw.openForDeposit();
            })

            .waitSeconds(AutoData.DEPOSIT_DELAY)

            .UNSTABLE_addTemporalMarkerOffset(AutoData.RETRACT_OFFSET, () -> {
                // Prepare the robot for intaking
                claw.openForIntake();
                intake.enable();
                lift.setHeight(RobotConstants.Lift.AUTO_INTAKE_4);
                armPosFunction = arm::setToBackwardsPos;

                wristPosFunction = wrist::setToBackwardsPos;
            })

            .back(in(10))
            .turn(rad(180 - AutoData.DEPOSIT_ANGLE))
            .UNSTABLE_addTemporalMarkerOffset(0.1, () -> startParking = true)

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
