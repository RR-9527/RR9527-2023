package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.util.RobotConstants;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;

@SuppressWarnings("CodeBlock2Expr")
@Autonomous
public class LM2_Right extends RougeBaseAuto {
    private Runnable armPosFunction;
    private Runnable wristPosFunction;

    // Variable added to signal when to start the parking sequence
    private boolean startParking = false;

    private TrajectorySequence mainTraj, parkTraj;

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
        int signalZone = waitForStartWithVision();
        telemetry.addData("Final signal zone", signalZone);
        telemetry.update();


        TrajectorySequenceBuilder parkTrajBuilder = drive.trajectorySequenceBuilder(mainTraj.end())
            .UNSTABLE_addTemporalMarkerOffset(0.05, () -> {
                lift.goToZero();
                armPosFunction = arm::setToRestingPos;
                wristPosFunction = wrist::setToRestingPos;
            });

        switch (signalZone) {
            case 1:
                parkTrajBuilder
                    .turn(rad(1))
                    .forward(in(126));
                break;
            case 3:
                parkTrajBuilder.forward(in(1));
                break;
            default:
                parkTrajBuilder
                    .UNSTABLE_addTemporalMarkerOffset(AutoData.INTAKE_LIFT_OFFSET - 0.25, () -> {
                        lift.setHeight(RobotConstants.Lift.HIGH);
                    })

                    .UNSTABLE_addTemporalMarkerOffset(AutoData.INTAKE_LIFT_OFFSET - 0.125, () -> {
                        armPosFunction = arm::setToForwardsAutoPos;
                        wristPosFunction = wrist::setToForwardsPos;
                    })

                    .waitSeconds(AutoData.INTAKE_DELAY + 0.125)
                    .waitSeconds(0.125)

                    .setReversed(false)
                    .splineTo(new Vector2d(in(AutoData.DEPOSIT_X + .775 + .25), in(AutoData.DEPOSIT_Y + .475 - .25)), rad(AutoData.DEPOSIT_ANGLE + AutoData.DEPOSIT_ANGLE_ADJUSTMENT * 5 - 2))

                    .UNSTABLE_addTemporalMarkerOffset(AutoData.LOWER_OFFSET, () -> {
                        lift.setHeight(RobotConstants.Lift.HIGH - AutoData.DEPOSIT_DROP_AMOUNT);
                    })

                    .UNSTABLE_addTemporalMarkerOffset(AutoData.DEPOSIT_OFFSET, () -> {
                        claw.openForDeposit(); // Deposit the cone while turning
                    })

                    .waitSeconds(AutoData.DEPOSIT_DELAY);
                break;
        }

        parkTraj = parkTrajBuilder.build();

        waitForStart();

        Scheduler.start(this, () -> {
            arm.update(telemetry, false);

            lift.update(telemetry, RobotConstants.Lift.USE_AGGRESSIVE_ASCENDANCE);
            wrist.update();
            drive.update();

            if (startParking) {
                startParking = false;
                drive.followTrajectorySequenceAsync(parkTraj);
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
                armPosFunction = arm::setToForwardsAutoPos;
            })

            .splineTo(new Vector2d(in(91), in(-50)), rad(90))
            .setTurnConstraint(Math.toRadians(430), Math.toRadians(125))
            .splineTo(new Vector2d(in(AutoData.DEPOSIT_X + 0.75), in(AutoData.DEPOSIT_Y + 0.5)), rad(AutoData.DEPOSIT_ANGLE - 2.25))
            .resetTurnConstraint()

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
                    claw.openForIntakeWide();
                    lift.setHeight(liftOffsets[finalI]);

                    armPosFunction = arm::setToBackwardsAutoPos;
                    wristPosFunction = wrist::setToBackwardsPos;
                })

                .setReversed(true)
                .splineTo(new Vector2d(in(AutoData.INTAKE_X + 0.315), in(AutoData.INTAKE_Y + 2)), rad(0))

                .UNSTABLE_addTemporalMarkerOffset(.02, () -> {
                    claw.close();
                })

                .UNSTABLE_addTemporalMarkerOffset(.1, () -> {
                    lift.setHeight(RobotConstants.Lift.HIGH);
                })

                .UNSTABLE_addTemporalMarkerOffset(AutoData.INTAKE_LIFT_OFFSET - 0.32, () -> {
                    armPosFunction = arm::setToForwardsAutoPos;
                    wristPosFunction = wrist::setToForwardsPos;
                })

                .waitSeconds(.33)

                .setReversed(false)
                .splineTo(new Vector2d(in(AutoData.DEPOSIT_X + .775 + i * .05), in(AutoData.DEPOSIT_Y + .475 - i * .05)), rad(AutoData.DEPOSIT_ANGLE + AutoData.DEPOSIT_ANGLE_ADJUSTMENT * i - 1.45))

                .UNSTABLE_addTemporalMarkerOffset(AutoData.LOWER_OFFSET, () -> {
                    lift.setHeight(RobotConstants.Lift.HIGH - AutoData.DEPOSIT_DROP_AMOUNT);
                })

                .UNSTABLE_addTemporalMarkerOffset(AutoData.DEPOSIT_OFFSET, () -> {
                    claw.openForDeposit(); // Deposit the cone while turning
                })

                .waitSeconds(AutoData.DEPOSIT_DELAY);
        }

        builder
            .UNSTABLE_addTemporalMarkerOffset(AutoData.RETRACT_OFFSET, () -> {
                intake.enable();
                claw.openForIntakeNarrow();

                lift.setHeight(RobotConstants.Lift.AUTO_INTAKE_5);

                armPosFunction = arm::setToBackwardsAutoPos;
                wristPosFunction = wrist::setToBackwardsPos;
            })

            .setReversed(true)
            .splineTo(new Vector2d(in(AutoData.INTAKE_X + 0.15), in(AutoData.INTAKE_Y)), rad(0))

            .UNSTABLE_addTemporalMarkerOffset(AutoData.CLAW_CLOSE_OFFSET - .4, () -> {
                intake.disable();
                claw.close();
            })

            .waitSeconds(.04)

            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                startParking = true;
                armPosFunction = arm::setToRestingPos;
                telemetry.addData("Entering parking auto", "");
            });

        drive.followTrajectorySequenceAsync(mainTraj = builder.build());
    }

    public static double rad(double degrees) {
        return Math.toRadians(degrees);
    }

    public static double in(double centimeters) {
        return centimeters * 0.3837008;
    }
}
