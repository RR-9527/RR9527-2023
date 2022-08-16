package org.firstinspires.ftc.teamcode.nevergonnagiveyouupnevergonnaletyoudownnevergonnarunaroundanddesertyounevergonnamakeyoucrynevergonnasaygoodbyenevergonnatellalieandhurtyou

import com.acmerobotics.roadrunner.drive.MecanumDrive
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.noahbres.meepmeep.core.util.FieldUtil
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.components.shooter.Shooter
import org.firstinspires.ftc.teamcode.components.shooter.initializedShooter
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.testing.PATH_RADIUS
import org.firstinspires.ftc.teamcode.testing.PATH_SIDES
import org.firstinspires.ftc.teamcode.testing.ROBOT_SIZE
import org.firstinspires.ftc.teamcode.testing.rad
import org.firstinspires.ftc.teamcode.util.LateInitVal
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Autonomous(name = "TestAutoKt")
class TestAuto : LinearOpMode() {
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

        val startY = (ROBOT_SIZE - FieldUtil.FIELD_HEIGHT) / 2.0
        val startPose = Pose2d(coords[0].x, startY, rad(90))

        val trajectorySequence = drive.trajectorySequenceBuilder(startPose).run {
            lineTo(Vector2d(coords[0].x, coords[0].y))

            for (i in 1..coords.size) {
                val (x, y) = coords[i % coords.size]

                val theta = atan2(rad(y), rad(x))
                splineToLinearHeading(
                    Pose2d(x, y, theta + rad(180)),
                    theta
                )

                addDisplacementMarker {
                    shooter.motor.power = 1.0
                    shooter.setIndexerToggled(true)
                }

                UNSTABLE_addTemporalMarkerOffset(0.5) {
                    shooter.motor.power = 0.0
                    shooter.setIndexerToggled(false)
                }

                waitSeconds(.51)
            }

            lineTo(Vector2d(coords[0].x, startY))
            build()
        }

        waitForStart()

        if (isStopRequested) return

        drive.followTrajectorySequence(trajectorySequence)
    }
}