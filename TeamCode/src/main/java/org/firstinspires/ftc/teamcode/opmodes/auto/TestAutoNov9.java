package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.util.RobotConstants;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;

@Disabled
@Autonomous
public class TestAutoNov9 extends RougeBaseAuto {
    private Runnable armPosFunction;
    private Runnable wristPosFunction;

    // Variable added to signal when to start the parking sequence
    private boolean startParking = false;

    TrajectorySequence mainTraj, parkTraj;

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

        // Added this during init
        int signalZone;
        do {
            signalZone = waitForStartWithVision();
            telemetry.addData("Signal zone detected", signalZone);
            telemetry.update();
        } while (!opModeIsActive());

        TrajectorySequenceBuilder parkTrajBuilder = drive.trajectorySequenceBuilder(mainTraj.end())
            .UNSTABLE_addTemporalMarkerOffset(0.05, () -> {
                lift.goToZero();
                armPosFunction = arm::setToRestingPos;
                wristPosFunction = wrist::setToRestingPos;
            })
            .turn(rad(180 - AutoData.DEPOSIT_ANGLE))
            .back(in(24));

        switch (signalZone) {
            case 1:
                parkTrajBuilder.forward(in(75));
                break;
            case 3:
                parkTrajBuilder.back(in(44));
                break;
            default:
                prayToGodCameraIsNotBroken();
                parkTrajBuilder.forward(in(15));
                break;
        }

        parkTraj = parkTrajBuilder.build();

        waitForStart();

        Scheduler.start(this, () -> {
            arm.update(telemetry, true);
            lift.update(telemetry);
            wrist.update();
            drive.update();

            if (startParking) {
                startParking = false;
            }

            telemetry.update();
        });
    }

    public void schedulePaths() {
        int[] liftOffsets = {
            RobotConstants.Lift.AUTO_INTAKE_1,
            RobotConstants.Lift.AUTO_INTAKE_2,
            RobotConstants.Lift.AUTO_INTAKE_3,
            RobotConstants.Lift.AUTO_INTAKE_4,
        };

        Pose2d startPose = new Pose2d(in(91), in(-159), rad(90));
        drive.setPoseEstimate(startPose);

        TrajectorySequenceBuilder builder = drive.trajectorySequenceBuilder(startPose);

        builder
            .UNSTABLE_addTemporalMarkerOffset(0.0, () -> {
                lift.setHeight(RobotConstants.Lift.HIGH);
                wristPosFunction = wrist::setToForwardsPos;
                armPosFunction = arm::setToForwardsPos;
            })

            .splineTo(new Vector2d(in(91), in(-50)), rad(90))
            .splineTo(new Vector2d(in(AutoData.DEPOSIT_X), in(AutoData.DEPOSIT_Y)), rad(AutoData.DEPOSIT_ANGLE))

            .UNSTABLE_addTemporalMarkerOffset(AutoData.LOWER_OFFSET, () -> {
                lift.setHeight(RobotConstants.Lift.HIGH - AutoData.DEPOSIT_DROP_AMOUNT);
            })

            .UNSTABLE_addTemporalMarkerOffset(AutoData.DEPOSIT_OFFSET, () -> {
                claw.openForDeposit(); // Deposit the cone while turning
            })

            .waitSeconds(AutoData.DEPOSIT_DELAY + .05);

        for (int i = 0; i < 4; i++) {
            int finalI = i;

            builder
                .UNSTABLE_addTemporalMarkerOffset(AutoData.RETRACT_OFFSET, () -> {
                    claw.openForIntake();
                    lift.setHeight(liftOffsets[finalI]);

                    armPosFunction = arm::setToBackwardsPos;
                    wristPosFunction = wrist::setToBackwardsPos;
                })

                .setReversed(true)
                .splineTo(new Vector2d(in(AutoData.INTAKE_X), in(AutoData.INTAKE_Y)), rad(0))

                .UNSTABLE_addTemporalMarkerOffset(AutoData.CLAW_CLOSE_OFFSET, () -> {
                    claw.close();
                })

                .UNSTABLE_addTemporalMarkerOffset(AutoData.INTAKE_LIFT_OFFSET, () -> {
                    lift.setHeight(RobotConstants.Lift.HIGH);
                })

                .UNSTABLE_addTemporalMarkerOffset(AutoData.INTAKE_LIFT_OFFSET, () -> {
                    armPosFunction = arm::setToForwardsPos;
                    wristPosFunction = wrist::setToForwardsPos;
                })

                .waitSeconds(AutoData.INTAKE_DELAY)

                .setReversed(false)
                .splineTo(new Vector2d(in(AutoData.DEPOSIT_X), in(AutoData.DEPOSIT_Y)), rad(AutoData.DEPOSIT_ANGLE - 1 - AutoData.DEPOSIT_ANGLE_ADJUSTMENT * i))

                .UNSTABLE_addTemporalMarkerOffset(AutoData.LOWER_OFFSET, () -> {
                    lift.setHeight(RobotConstants.Lift.HIGH - AutoData.DEPOSIT_DROP_AMOUNT);
                })

                .UNSTABLE_addTemporalMarkerOffset(AutoData.DEPOSIT_OFFSET, () -> {
                    claw.openForDeposit(); // Deposit the cone while turning
                })

                .waitSeconds(AutoData.DEPOSIT_DELAY);
        }

        builder.UNSTABLE_addTemporalMarkerOffset(0, () -> {
            drive.followTrajectorySequenceAsync(parkTraj);
        });

        drive.followTrajectorySequenceAsync(mainTraj = builder.build());
    }

    private void prayToGodCameraIsNotBroken() {
        telemetry.addLine("oh god please save us all");
    }

    public static double rad(double degrees) {
        return Math.toRadians(degrees);
    }

    public static double in(double centimeters) {
        return centimeters * 0.3837008;
    }
}
