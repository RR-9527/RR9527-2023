package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.util.RobotConstants;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@SuppressWarnings("CodeBlock2Expr")
public class LM2_Right_v2_v2 extends RougeBaseAuto {
    public static final int MAX_CYCLES = 4;

    private int cycleNumber;
    private int signalZone;

    private Runnable armPosFunction;
    private Runnable wristPosFunction;

    private static final int[] liftOffsets = {
        RobotConstants.Lift.AUTO_INTAKE_1,
        RobotConstants.Lift.AUTO_INTAKE_2,
        RobotConstants.Lift.AUTO_INTAKE_3,
        RobotConstants.Lift.AUTO_INTAKE_4,
    };

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();

        armPosFunction = arm::setToRestingPos;
        wristPosFunction = wrist::setToRestingPos;

        Scheduler.beforeEach(() -> {
            armPosFunction.run();
            wristPosFunction.run();
        });

        signalZone = waitForStartWithVision();
        telemetry.addData("Final signal zone", signalZone);
        telemetry.update();

        Pose2d startPose = new Pose2d(in(91), in(-159), rad(90));
        createAndFollowPreload(startPose);

        Scheduler.start(this, () -> {
            arm.update(telemetry, false);
            lift.update(telemetry, RobotConstants.Lift.USE_AGGRESSIVE_ASCENDANCE);
            wrist.update();
            drive.update();
            telemetry.update();
        });
    }

    private void createAndFollowPreload(Pose2d startPose) {
        createAndFollowTrajectory(startPose, (builder, endPose) -> builder
            .addTemporalMarker(() -> {
                lift.setHeight(RobotConstants.Lift.HIGH + 200);
                wristPosFunction = wrist::setToForwardsPos;
                armPosFunction = arm::setToForwardsAutoPos;
            })

            .splineTo(cmVector(91, 50), rad(90))

            .splineTo(
                cmVector(AutoData.DEPOSIT_X + .75, AutoData.DEPOSIT_Y + .5),
                rad(AutoData.DEPOSIT_ANGLE - 3)
            )

            .addTemporalMarker(() -> {
                createAndFollowPoleDistanceAdjustment(endPose.get());
            }));
    }

    private void createAndFollowPoleDistanceAdjustment(Pose2d startPose) {
        createAndFollowTrajectory(startPose, (builder, endPose) -> {
            double distance = frontSensor.getDistance();

            if (Math.abs(distance - 15) > 1) {
                builder.forward(in(distance - 15));
            }

            builder.addTemporalMarker(() -> {
                createAndFollowDeposit(endPose.get());
            });

            return builder;
        });
    }

    private void createAndFollowDeposit(Pose2d startPose) {
        createAndFollowTrajectory(startPose, (builder, endPose) -> builder
            .UNSTABLE_addTemporalMarkerOffset(AutoData.LOWER_OFFSET, () -> {
                lift.setHeight(RobotConstants.Lift.HIGH - AutoData.DEPOSIT_DROP_AMOUNT);
            })

            .UNSTABLE_addTemporalMarkerOffset(AutoData.DEPOSIT_OFFSET, () -> {
                claw.openForDeposit();
            })

            .waitSeconds(AutoData.DEPOSIT_DELAY + .05)

            .addTemporalMarker(() -> {
                createAndFollowIntakeCycle(endPose.get());
            }));
    }

    private void createAndFollowIntakeCycle(Pose2d startPose) {
        createAndFollowTrajectory(startPose, (builder, endPose) -> builder
            .UNSTABLE_addTemporalMarkerOffset(AutoData.RETRACT_OFFSET, () -> {
                claw.openForIntakeNarrow();
                lift.setHeight(liftOffsets[cycleNumber]);

                armPosFunction = arm::setToBackwardsAutoPos;
                wristPosFunction = wrist::setToBackwardsPos;
            })

            .setReversed(true)
            .splineTo(cmVector(AutoData.INTAKE_X + 0.325, AutoData.INTAKE_Y + 2), 0)
            .setReversed(false)

            .addTemporalMarker(() -> {
                createAndFollowIntake(endPose.get());
            }));
    }

    private void createAndFollowIntake(Pose2d startPose) {
        createAndFollowTrajectory(startPose, (builder, endPose) -> builder
            .UNSTABLE_addTemporalMarkerOffset(AutoData.CLAW_CLOSE_OFFSET, () -> {
                claw.close();
            })

            .UNSTABLE_addTemporalMarkerOffset(AutoData.INTAKE_LIFT_OFFSET - 0.25, () -> {
                lift.setHeight(RobotConstants.Lift.HIGH + 200);
            })

            .UNSTABLE_addTemporalMarkerOffset(AutoData.INTAKE_LIFT_OFFSET - 0.125, () -> {
                armPosFunction = arm::setToForwardsAutoPos;
                wristPosFunction = wrist::setToForwardsPos;
            })

            .waitSeconds(AutoData.INTAKE_DELAY + 0.25)

            .addTemporalMarker(() -> {
                createAndFollowDepositCycle(endPose.get());
            }));
    }

    private void createAndFollowDepositCycle(Pose2d startPose) {
        createAndFollowTrajectory(startPose, (builder, endPose) -> builder
            .splineTo(
                cmVector(AutoData.DEPOSIT_X + 1.125, AutoData.DEPOSIT_Y + 0.625),
                rad(AutoData.DEPOSIT_ANGLE + 2 + AutoData.DEPOSIT_ANGLE_ADJUSTMENT * cycleNumber)
            )

            .addTemporalMarker(() -> {
                if (cycleNumber++ < MAX_CYCLES) {
                    createAndFollowPoleDistanceAdjustment(endPose.get());
                } else {
                    createAndFollowParkingPrep(endPose.get());
                }
            }));
    }

    private void createAndFollowParkingPrep(Pose2d startPose) {
        createAndFollowTrajectory(startPose, (builder, endPose) -> builder
            .UNSTABLE_addTemporalMarkerOffset(AutoData.RETRACT_OFFSET, () -> {
                claw.openForIntakeNarrow();
                lift.setHeight(RobotConstants.Lift.AUTO_INTAKE_5);

                armPosFunction = arm::setToBackwardsAutoPos;
                wristPosFunction = wrist::setToBackwardsPos;
            })

            .setReversed(true)
            .splineTo(cmVector(AutoData.INTAKE_X + 0.325, AutoData.INTAKE_Y), 0)
            .setReversed(false)

            .UNSTABLE_addTemporalMarkerOffset(AutoData.CLAW_CLOSE_OFFSET, () -> {
                claw.close();
            })

            .addTemporalMarker(() -> {
                createAndFollowPark(endPose.get());
            }));
    }

    private void createAndFollowPark(Pose2d startPose) {
        createAndFollowTrajectory(startPose, (builder, endPose) -> {
            builder
                .turn(rad(1.75))
                .UNSTABLE_addTemporalMarkerOffset(0.05, () -> {
                    lift.goToZero();
                    armPosFunction = arm::setToRestingPos;
                    wristPosFunction = wrist::setToRestingPos;
                });

            switch (signalZone) {
                case 3:
                    builder.forward(in(126));
                    break;
                case 2:
                    builder.forward(in(70));
                    break;
                default:
                    builder.forward(in(1));
                    break;
            }

            return builder;
        });
    }

    private void createAndFollowTrajectory(
        Pose2d startPose,
        BiFunction<TrajectorySequenceBuilder, Supplier<Pose2d>, TrajectorySequenceBuilder> builder
    ) {
        TrajectorySequence[] trajectory = new TrajectorySequence[1];

        trajectory[0] = builder.apply(
            drive.trajectorySequenceBuilder(startPose),
            () -> trajectory[0].end()
        ).build();

        drive.followTrajectorySequenceAsync(trajectory[0]);
    }

    // Temporary utility functions (very temporary I swear) (more temporary than liz truss)

    private Vector2d cmVector(double x, double y) {
        return new Vector2d(in(x), in(y));
    }

    public static double rad(double degrees) {
        return Math.toRadians(degrees);
    }

    public static double in(double centimeters) {
        return centimeters * 0.3837008;
    }
}
