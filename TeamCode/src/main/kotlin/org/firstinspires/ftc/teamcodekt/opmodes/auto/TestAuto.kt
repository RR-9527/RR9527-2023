package org.firstinspires.ftc.teamcodekt.opmodes.auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcodekt.components.shooter.Shooter
import org.firstinspires.ftc.teamcodekt.components.shooter.initializedShooter
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcodekt.util.LateInitVal
import org.firstinspires.ftc.teamcodekt.util.rad
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Autonomous(name = "TestAutoKt")
class TestAuto : LinearOpMode() {
    companion object {
        const val FIELD_SIZE = 130
        const val ROBOT_LENGTH = 16.45
        const val PATH_RADIUS = 50
        const val PATH_SIDES = 4
    }

    var drive: SampleMecanumDrive by LateInitVal()
    var shooter: Shooter by LateInitVal()
    
    override fun runOpMode() {
        drive = SampleMecanumDrive(hardwareMap)
        shooter = initializedShooter(hardwareMap)

        data class Coordinate(val x: Double, val y: Double)

        val coords = (0 until PATH_SIDES).map { i ->
            val x = -PATH_RADIUS * sin(x = 2 * i * Math.PI / PATH_SIDES)
            val y = -PATH_RADIUS * cos(x = 2 * i * Math.PI / PATH_SIDES)
            Coordinate(x, y)
        }

        val startY = (ROBOT_LENGTH - FIELD_SIZE) / 2.0
        val startPose = Pose2d(0.0, startY, rad(90))

        val trajectorySequence = drive.trajectorySequenceBuilder(startPose).run {
            lineTo(Vector2d(coords[0].x, coords[0].y))

            for (i in 1..coords.size) {
                val (x, y) = coords[i % coords.size]

                val theta = atan2(rad(y), rad(x))
                splineToLinearHeading(
                    Pose2d(x, y, theta + rad(180)),
                    theta
                )

                if (i < coords.size) {

                    addDisplacementMarker {
                        shooter.setIndexerToggled(true)
                    }

                    UNSTABLE_addTemporalMarkerOffset(0.25) {
                        shooter.setIndexerToggled(false)
                    }

                    waitSeconds(.3)
                } else {
                    turn(Math.PI * 8)
                }
            }


            lineTo(Vector2d(0.0, startY))
            build()
        }

        waitForStart()

        if (isStopRequested) return

        shooter.motor.power = 1.0
        drive.followTrajectorySequence(trajectorySequence)
        shooter.motor.power = 0.0
        shooter.setIndexerToggled(false)
    }
}