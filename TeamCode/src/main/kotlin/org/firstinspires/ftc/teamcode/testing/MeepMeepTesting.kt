package org.firstinspires.ftc.teamcode.testing

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.core.util.FieldUtil
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder
import org.firstinspires.ftc.teamcode.components.meepmeep.MeepMeepPersistence
import org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants
import org.firstinspires.ftc.teamcode.components.meepmeep.MeepMeepFun
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

const val PATH_RADIUS = 55
const val PATH_SIDES = 4
const val ROBOT_SIZE = 20
const val CONFIG_FILE_PATH =
    "TeamCode/src/main/res/raw/meepmeep.properties"

fun main() {
    val meepMeep = MeepMeep(1000, fps = 165)

    val persistence = MeepMeepPersistence(meepMeep, CONFIG_FILE_PATH)
    persistence.restore()

    val bot = DefaultBotBuilder(meepMeep)
        .setConstraints(
            maxVel = DriveConstants.MAX_VEL,
            maxAccel = DriveConstants.MAX_ACCEL,
            maxAngVel = DriveConstants.MAX_ANG_VEL,
            maxAngAccel = DriveConstants.MAX_ANG_ACCEL,
            trackWidth = DriveConstants.TRACK_WIDTH,
        )
        .followTrajectorySequence {
            data class Coordinate(val x: Double, val y: Double)

            val coords = (0 until PATH_SIDES).map { i ->
                val x = -PATH_RADIUS * sin(x = 2 * i * Math.PI / PATH_SIDES)
                val y = -PATH_RADIUS * cos(x = 2 * i * Math.PI / PATH_SIDES)
                Coordinate(x, y)
            }

            val startY = (ROBOT_SIZE - FieldUtil.FIELD_HEIGHT) / 2.0
            val startPose = Pose2d(0.0, startY, rad(90))

            it.trajectorySequenceBuilder(startPose).run {
                lineTo(Vector2d(coords[0].x, coords[0].y))

                for (i in 1..coords.size) {
                    val (x, y) = coords[i % coords.size]

                    val theta = atan2(rad(y), rad(x))
                    splineToLinearHeading(
                        Pose2d(x, y, theta + rad(180)),
                        theta
                    )

                    if (i < coords.size) {
                        addDisplacementMarker {}

                        UNSTABLE_addTemporalMarkerOffset(0.5) {}

                        waitSeconds(.51)
                    } else {
                        turn(Math.PI * 8)
                    }
                }

                lineTo(Vector2d(0.0, startY))
                build()
            }
        }

    MeepMeepFun(meepMeep, bot).launch()

    meepMeep
        .setBackground(MeepMeep.Background.GRID_GRAY)
        .setDarkMode(true)
        .addEntity(bot)
        .start()
}

fun rad(d: Int) = Math.toRadians(d.toDouble())
fun rad(d: Double) = Math.toRadians(d)