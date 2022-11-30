package org.firstinspires.ftc.teamcode.opmodes.deprecated.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.opmodes.auto.AutoData;
import org.firstinspires.ftc.teamcode.opmodes.auto.RougeBaseAuto;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.util.RobotConstants;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;

@Disabled
@Autonomous
public class LM1_Left extends RougeBaseAuto {
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
            case 3:
                parkTrajBuilder
                    .strafeRight(in(3))
                    .forward(in(126));
                break;
            case 2:
                parkTrajBuilder
                    .strafeRight(in(3))
                    .forward(in(70));
//                parkTrajBuilder
//                    .UNSTABLE_addTemporalMarkerOffset(0, () -> {
//                        lift.setHeight(RobotConstants.Lift.HIGH);
//                    })
//                    .UNSTABLE_addTemporalMarkerOffset(AutoData.INTAKE_LIFT_OFFSET, () -> {
//                        armPosFunction = arm::setToForwardsPos;
//                        wristPosFunction = wrist::setToForwardsPos;
//                    })
//
//                    .setReversed(false)
//                    .splineTo(new Vector2d(in(AutoData.DEPOSIT_X), in(AutoData.DEPOSIT_Y)), rad(AutoData.DEPOSIT_ANGLE - 1 - AutoData.DEPOSIT_ANGLE_ADJUSTMENT * 5))
//
//                    .UNSTABLE_addTemporalMarkerOffset(AutoData.LOWER_OFFSET, () -> {
//                        lift.setHeight(RobotConstants.Lift.HIGH - AutoData.DEPOSIT_DROP_AMOUNT);
//                    })
//
//                    .UNSTABLE_addTemporalMarkerOffset(AutoData.DEPOSIT_OFFSET, () -> {
//                        claw.openForDeposit(); // Deposit the cone while turning
//                    })
//                    .UNSTABLE_addTemporalMarkerOffset(AutoData.DEPOSIT_OFFSET+0.5, () -> {
//                        lift.setHeight(RobotConstants.Lift.ZERO);
//                        armPosFunction = arm::setToForwardsPos;
//                        wristPosFunction = wrist::setToForwardsPos;
//                    })
//                ;
                break;
            default:
                parkTrajBuilder.forward(in(1));
                break;
        }

        parkTraj = parkTrajBuilder.build();

        waitForStart();

        Scheduler.start(this, () -> {
            arm.update(telemetry, false);

            lift.update(telemetry);
            wrist.update();
            drive.update();

            if (startParking) {
                startParking = false;

                drive.followTrajectorySequence(parkTraj);
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

        Pose2d startPose = new Pose2d(in(-91), in(-159), rad(90));
        drive.setPoseEstimate(startPose);

        TrajectorySequenceBuilder builder = drive.trajectorySequenceBuilder(startPose);

        builder
            .UNSTABLE_addTemporalMarkerOffset(0.0, () -> {
                lift.setHeight(RobotConstants.Lift.HIGH);
                wristPosFunction = wrist::setToForwardsPos;
                armPosFunction = arm::setToForwardsAutoPos;
            })

            .splineTo(new Vector2d(in(-91), in(-50)), rad(90))
            .splineTo(new Vector2d(in(-AutoData.DEPOSIT_X+1.5), in(AutoData.DEPOSIT_Y+1.5)), rad(180 - (AutoData.DEPOSIT_ANGLE - .02)- 12))

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

                    armPosFunction = arm::setToBackwardsAutoPos;
                    wristPosFunction = wrist::setToBackwardsPos;
                })

                .setReversed(true)
                .splineTo(new Vector2d(in(-AutoData.INTAKE_X+1.25), in(AutoData.INTAKE_Y+3.5+1)), rad(180))

                .UNSTABLE_addTemporalMarkerOffset(AutoData.CLAW_CLOSE_OFFSET, () -> {
                    claw.close();
                })

                .UNSTABLE_addTemporalMarkerOffset(AutoData.INTAKE_LIFT_OFFSET, () -> {
                    lift.setHeight(RobotConstants.Lift.HIGH);
                })

                .UNSTABLE_addTemporalMarkerOffset(AutoData.INTAKE_LIFT_OFFSET, () -> {
                    armPosFunction = arm::setToForwardsAutoPos;
                    wristPosFunction = wrist::setToForwardsPos;
                })

                .waitSeconds(AutoData.INTAKE_DELAY)

                .setReversed(false)
                .waitSeconds(0.125)
                .splineTo(new Vector2d(in(-AutoData.DEPOSIT_X+1.5), in(AutoData.DEPOSIT_Y+1.5)), rad(180 - (AutoData.DEPOSIT_ANGLE - 1 - (AutoData.DEPOSIT_ANGLE_ADJUSTMENT - .02) * i)))

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
                claw.openForIntake();
                lift.setHeight(RobotConstants.Lift.AUTO_INTAKE_5);

                armPosFunction = arm::setToBackwardsAutoPos;
                wristPosFunction = wrist::setToBackwardsPos;
            })

            .setReversed(true)
            .splineTo(new Vector2d(in(-AutoData.INTAKE_X+1.25), in(AutoData.INTAKE_Y+4.5)), rad(180))

            .UNSTABLE_addTemporalMarkerOffset(AutoData.CLAW_CLOSE_OFFSET, () -> {
                claw.close();
            });

        builder
            .turn(rad(3))
            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                startParking = true;
                armPosFunction = arm::setToRestingPos;
                telemetry.addData("Entering parking auto","");
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
