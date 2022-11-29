package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.util.RobotConstants;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;

import java.util.HashMap;
import java.util.Map;

@Autonomous
public class LM2_Left_v2 extends RougeBaseAuto {
    private final Map<String, TrajectorySequence> trajectories;

    private State currentState;
    private Pose2d nextIntendedStartPose;

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

    public LM2_Left_v2() {
        trajectories = new HashMap<>();
        currentState = State.WAITING;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();

        armPosFunction = arm::setToRestingPos;
        wristPosFunction = wrist::setToRestingPos;

        Scheduler.beforeEach(() -> {
            armPosFunction.run();
            wristPosFunction.run();
        });

        buildStaticTrajectories();

        signalZone = waitForStartWithVision();
        telemetry.addData("Final signal zone", signalZone);
        telemetry.update();

        currentState = State.TEST;

        Scheduler.start(this, () -> {
            runStateMachine();

            arm.update(telemetry, false);
            lift.update(telemetry, RobotConstants.Lift.USE_AGGRESSIVE_ASCENDANCE);
            wrist.update();
            drive.update();

            telemetry.addData("Current state", currentState.name());

            telemetry.update();
        });
    }

    private void runStateMachine() {
        switch (currentState) {
            case TEST:
                followTrajectoryIfNotBusy("test");
                break;
            case INTAKING:
                followTrajectoryIfNotBusy("intake");
                break;
            case DEPOSITING:
                followTrajectoryIfNotBusy("deposit");
                break;
            case DISTANCE_ADJUSTING:
                followTrajectoryIfNotBusy("adjustment");
                break;
            case CYCLE_INTAKE_PATH:
                followTrajectoryIfNotBusy("intake_cycle");
                break;
            case CYCLE_DEPOSIT_PATH:
                followTrajectoryIfNotBusy("deposit_cycle");
                break;
            case PRELOAD_DEPOSIT_PATH:
                followTrajectoryIfNotBusy("preload");
                break;
            case PARKING:
                followTrajectoryIfNotBusy("parking");
                break;
            case PARKING_PREP:
                followTrajectoryIfNotBusy("parking_prep");
                break;
        }
    }

    private void buildStaticTrajectories() {
        Pose2d startPose = new Pose2d(in(-91), in(-159), rad(90));

        createTrajectory("test", drive.trajectorySequenceBuilder(startPose)
            .forward(10)
            .turn(90));

        createTrajectory("preload", drive.trajectorySequenceBuilder(startPose)
            .addTemporalMarker(() -> {
                lift.setHeight(RobotConstants.Lift.HIGH+75);
                wristPosFunction = wrist::setToForwardsPos;
                armPosFunction = arm::setToForwardsPos;
            })

            .splineTo(cmVector(-91, 50), rad(90))

            .splineTo(
                cmVector(-AutoData.DEPOSIT_X + 1.5, AutoData.DEPOSIT_Y + 1.5),
                rad(180 - (AutoData.DEPOSIT_ANGLE - .02) - 12)
            )

            .addTemporalMarker(() -> {
                currentState = State.DISTANCE_ADJUSTING;
                createTrajectory("adjustment", createDistanceAdjustmentTrajectory());
            }));
        nextIntendedStartPose = trajectories.get("preload").end();

        createTrajectory("deposit", drive.trajectorySequenceBuilder(nextIntendedStartPose)
            .UNSTABLE_addTemporalMarkerOffset(AutoData.LOWER_OFFSET, () -> {
                lift.setHeight(RobotConstants.Lift.HIGH+75 - AutoData.DEPOSIT_DROP_AMOUNT);
            })

            .UNSTABLE_addTemporalMarkerOffset(AutoData.DEPOSIT_OFFSET, () -> {
                claw.openForDeposit();
            })

            .waitSeconds(AutoData.DEPOSIT_DELAY + .05)

            .addTemporalMarker(() -> {
                currentState = State.CYCLE_INTAKE_PATH;
            }));

        createTrajectory("intake_cycle", drive.trajectorySequenceBuilder(nextIntendedStartPose)
            .UNSTABLE_addTemporalMarkerOffset(AutoData.RETRACT_OFFSET, () -> {
                claw.openForIntake();
                lift.setHeight(liftOffsets[cycleNumber]);

                armPosFunction = arm::setToBackwardsPos;
                wristPosFunction = wrist::setToBackwardsPos;
            })

            .setReversed(true)
            .splineTo(cmVector(-AutoData.INTAKE_X + 1.25, AutoData.INTAKE_Y + 4.5), rad(180))
            .setReversed(false)

            .addTemporalMarker(() -> {
                currentState = State.INTAKING;
            }));
        nextIntendedStartPose = trajectories.get("intake_cycle").end();

        createTrajectory("intake", drive.trajectorySequenceBuilder(nextIntendedStartPose)
            .UNSTABLE_addTemporalMarkerOffset(AutoData.CLAW_CLOSE_OFFSET, () -> {
                claw.close();
            })

            .UNSTABLE_addTemporalMarkerOffset(AutoData.INTAKE_LIFT_OFFSET, () -> {
                lift.setHeight(RobotConstants.Lift.HIGH+75);
            })

            .UNSTABLE_addTemporalMarkerOffset(AutoData.INTAKE_LIFT_OFFSET, () -> {
                armPosFunction = arm::setToForwardsPos;
                wristPosFunction = wrist::setToForwardsPos;
            })

            .waitSeconds(AutoData.INTAKE_DELAY + 0.125)

            .addTemporalMarker(() -> {
                currentState = State.CYCLE_DEPOSIT_PATH;
            }));

        createTrajectory("deposit_cycle", drive.trajectorySequenceBuilder(nextIntendedStartPose)
            .splineTo(
                cmVector(-AutoData.DEPOSIT_X + 1.5, AutoData.DEPOSIT_Y + 1.5),
                rad(180 - (AutoData.DEPOSIT_ANGLE - 1))
            )

            .addTemporalMarker(() -> {

                if (++cycleNumber < MAX_CYCLES) {
                    currentState = State.DISTANCE_ADJUSTING;
                    createTrajectory("adjustment", createDistanceAdjustmentTrajectory());
                } else {
                    currentState = State.PARKING_PREP;
                }
            })
        );
        nextIntendedStartPose = trajectories.get("deposit_cycle").end();

        createTrajectory("parking_prep", drive.trajectorySequenceBuilder(nextIntendedStartPose)
            .UNSTABLE_addTemporalMarkerOffset(AutoData.RETRACT_OFFSET, () -> {
                claw.openForIntake();
                lift.setHeight(RobotConstants.Lift.AUTO_INTAKE_5);

                armPosFunction = arm::setToBackwardsPos;
                wristPosFunction = wrist::setToBackwardsPos;
            })

            .setReversed(true)
            .splineTo(cmVector(-AutoData.INTAKE_X + 1.25, AutoData.INTAKE_Y + 4.5), rad(180))
            .setReversed(false)

            .UNSTABLE_addTemporalMarkerOffset(AutoData.CLAW_CLOSE_OFFSET, () -> {
                claw.close();
            })

            .addTemporalMarker(() -> {
                currentState = State.PARKING;
                createTrajectory("parking", createParkingTrajectory());
            }));
        nextIntendedStartPose = trajectories.get("parking_prep").end();
    }

    private TrajectorySequenceBuilder createDistanceAdjustmentTrajectory() {
        TrajectorySequenceBuilder adjustment = drive.trajectorySequenceBuilder(nextIntendedStartPose);

        double distance = frontSensor.getDistance();
        if (Math.abs(distance - 15) > 1) {
            adjustment.forward(distance - 15);
        }

        adjustment.addTemporalMarker(() -> {
            currentState = State.DEPOSITING;
        });
        nextIntendedStartPose = adjustment.build().end();

        return adjustment;
    }

    private TrajectorySequenceBuilder createParkingTrajectory() {
        TrajectorySequenceBuilder parkTrajBuilder = drive.trajectorySequenceBuilder(nextIntendedStartPose)
            .UNSTABLE_addTemporalMarkerOffset(0.05, () -> {
                lift.goToZero();
                armPosFunction = arm::setToRestingPos;
                wristPosFunction = wrist::setToRestingPos;
            });

        switch (signalZone) {
            case 3:
                parkTrajBuilder
                    .strafeRight(in(3))
                    .forward(in(126));
                break;
            case 2:
                parkTrajBuilder
                    .strafeRight(in(3))
                    .forward(in(70));
                break;
            default:
                parkTrajBuilder.forward(in(1));
                break;
        }

        return parkTrajBuilder;
    }

    // Temporary utility functions (very temporary I swear) (more temporary than liz truss)

    private Vector2d cmVector(double x, double y) {
        return new Vector2d(in(x), in(y));
    }

    private void createTrajectory(String name, TrajectorySequenceBuilder builder) {
        trajectories.put(name, builder.build());
    }

    private void followTrajectoryIfNotBusy(String name) {
        if (!drive.isBusy()) {
            drive.followTrajectorySequenceAsync(trajectories.get(name));
        }
    }

    public static double rad(double degrees) {
        return Math.toRadians(degrees);
    }

    public static double in(double centimeters) {
        return centimeters * 0.3837008;
    }
}

enum State {
    WAITING,
    INTAKING,
    DEPOSITING,
    DISTANCE_ADJUSTING,
    CYCLE_INTAKE_PATH,
    CYCLE_DEPOSIT_PATH,
    PRELOAD_DEPOSIT_PATH,
    PARKING,
    PARKING_PREP,
    TEST,
}
