package org.firstinspires.ftc.teamcodekt.opmodes.auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoData.*
import org.firstinspires.ftc.teamcode.opmodes.auto.RougeBaseAuto
import org.firstinspires.ftc.teamcode.util.RobotConstants
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler
import org.firstinspires.ftc.teamcodekt.components.scheduler.auto.TrajectorySequenceBuilderEx
import org.firstinspires.ftc.teamcodekt.components.scheduler.auto.TrajectorySequenceBuilderEx.Companion.createTrajectory
import org.firstinspires.ftc.teamcodekt.util.toIn
import org.firstinspires.ftc.teamcodekt.util.toRad
import kotlin.math.abs

@Suppress("RemoveRedundantQualifierName")
class LM2_Right_v2_v3 : RougeBaseAuto() {
    private var cycleNumber = 0
    private var signalZone = 0

    private var armPosFunction = arm::setToRestingPos
    private var wristPosFunction = wrist::setToRestingPos

    override fun runOpMode() {
        initHardware()

        Scheduler.beforeEach {
            armPosFunction()
            wristPosFunction()
        }

        signalZone = waitForStartWithVision()
        telemetry.addData("Final signal zone", signalZone)
        telemetry.update()

        val startPose = Pose2d(91.toIn(), (-159).toIn(), 90.toRad())
        preload(startPose).runAsync()

        Scheduler.start(this) {
            arm.update(telemetry, false)
            lift.update(telemetry)
            wrist.update()
            drive.update()
            telemetry.update()
        }
    }

    private fun preload(startPose: Pose2d): TrajectorySequenceBuilderEx =
        createTrajectory(drive, startPose) {
            addTemporalMarker {
                lift.height = RobotConstants.Lift.HIGH + 200

                wristPosFunction = wrist::setToForwardsPos
                armPosFunction = arm::setToForwardsAutoPos
            }

            splineTo(91.0, 50.0, 90.0)

            splineTo(DEPOSIT_X + .75, DEPOSIT_Y + .5, DEPOSIT_ANGLE - 3)

            thenRunAsync(::poleDistanceAdjustment)
        }

    private fun poleDistanceAdjustment(startPose: Pose2d): TrajectorySequenceBuilderEx =
        createTrajectory(drive, startPose) {
            val distance = frontSensor.distance

            if (abs(distance - 15) > 1) {
                forward(distance - 15)
            }

            thenRunAsync(::deposit)
        }

    private fun deposit(startPose: Pose2d): TrajectorySequenceBuilderEx =
        createTrajectory(drive, startPose) {
            addTemporalMarker(LOWER_OFFSET) {
                lift.height = RobotConstants.Lift.HIGH - DEPOSIT_DROP_AMOUNT
            }

            addTemporalMarker(DEPOSIT_OFFSET) {
                claw.openForDeposit()
            }

            waitSeconds(DEPOSIT_DELAY + .05)

            thenRunAsync(::intakeCycle)
        }

    private fun intakeCycle(startPose: Pose2d): TrajectorySequenceBuilderEx =
        createTrajectory(drive, startPose) {
            addTemporalMarker(RETRACT_OFFSET) {
                claw.openForIntakeNarrow()

                lift.height = liftOffsets[cycleNumber]

                armPosFunction = arm::setToBackwardsAutoPos
                wristPosFunction = wrist::setToBackwardsPos
            }

            inReverse {
                splineTo(INTAKE_X + 0.325, INTAKE_Y + 2, 0.0)
            }

            thenRunAsync(::intake)
        }

    private fun intake(startPose: Pose2d): TrajectorySequenceBuilderEx =
        createTrajectory(drive, startPose) {
            addTemporalMarker(CLAW_CLOSE_OFFSET) {
                claw.close()
            }

            addTemporalMarker(INTAKE_LIFT_OFFSET - 0.25) {
                lift.height = RobotConstants.Lift.HIGH + 200
            }

            addTemporalMarker(INTAKE_LIFT_OFFSET - 0.125) {
                armPosFunction = arm::setToForwardsAutoPos
                wristPosFunction = wrist::setToForwardsPos
            }

            waitSeconds(INTAKE_DELAY + 0.25)

            thenRunAsync(::depositCycle)
        }

    private fun depositCycle(startPose: Pose2d): TrajectorySequenceBuilderEx =
        createTrajectory(drive, startPose) {
            splineTo(
                DEPOSIT_X + 1.125,
                DEPOSIT_Y + 0.625,
                DEPOSIT_ANGLE + 2 + DEPOSIT_ANGLE_ADJUSTMENT * cycleNumber
            )

            addTemporalMarker { cycleNumber++ }

            thenRunAsyncIf(::poleDistanceAdjustment, startPose) {
                cycleNumber <= MAX_CYCLES
            }

            thenRunAsyncIf(::parkingPrepIntakeThing, startPose) {
                cycleNumber > MAX_CYCLES
            }
        }

    private fun parkingPrepIntakeThing(startPose: Pose2d): TrajectorySequenceBuilderEx =
        createTrajectory(drive, startPose) {
            addTemporalMarker(RETRACT_OFFSET) {
                intake.enable()
                claw.openForIntakeNarrow()

                lift.height = RobotConstants.Lift.AUTO_INTAKE_5

                armPosFunction = arm::setToBackwardsAutoPos
                wristPosFunction = wrist::setToBackwardsPos
            }

            inReverse {
                splineTo(INTAKE_X, INTAKE_Y, 0.0)
            }

            addTemporalMarker(CLAW_CLOSE_OFFSET - .03) {
                intake.disable()
                claw.close()
            }

            thenRunAsync(::park)
        }

    private fun park(startPose: Pose2d): TrajectorySequenceBuilderEx =
        createTrajectory(drive, startPose) {
            turn(1.75)

            addTemporalMarker(0.05) {
                lift.goToZero()
                armPosFunction = arm::setToRestingPos
                wristPosFunction = wrist::setToRestingPos
            }

            when (signalZone) {
                3 ->    forward(126.0)
                2 ->    forward(70.0)
                else -> forward(1.0)
            }
        }

    companion object {
        const val MAX_CYCLES = 4

        private val liftOffsets = intArrayOf(
            RobotConstants.Lift.AUTO_INTAKE_1,
            RobotConstants.Lift.AUTO_INTAKE_2,
            RobotConstants.Lift.AUTO_INTAKE_3,
            RobotConstants.Lift.AUTO_INTAKE_4,
        )
    }
}
