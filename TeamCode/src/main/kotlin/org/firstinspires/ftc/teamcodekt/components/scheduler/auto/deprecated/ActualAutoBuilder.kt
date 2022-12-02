package org.firstinspires.ftc.teamcodekt.components.scheduler.auto.deprecated

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.MarkerCallback
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcodekt.util.toIn
import org.firstinspires.ftc.teamcodekt.util.toRad

class ActualAutoBuilder(drive: SampleMecanumDrive) {
    private val trajectorySequenceBuilder = drive.trajectorySequenceBuilder(Pose2d())

    fun startAt(pose: Pose2d) = this.apply {
        trajectorySequenceBuilder.javaClass.getDeclaredField("lastPose").apply {
            isAccessible = true
            set(trajectorySequenceBuilder, pose)
        }
    }

    fun forward(distance: Double) = this.apply {
        trajectorySequenceBuilder.forward(distance.toIn())
    }

    fun back(distance: Double) = this.apply {
        trajectorySequenceBuilder.back(distance.toIn())
    }

    fun turn(angle: Double) = this.apply {
        trajectorySequenceBuilder.turn(angle.toRad())
    }

    fun splineTo(x: Double, y: Double, heading: Double) = this.apply {
        trajectorySequenceBuilder.splineTo(Vector2d(x.toIn(), y.toIn()), heading.toRad())
    }

    fun waitSeconds(time: Double) = this.apply {
        trajectorySequenceBuilder.waitSeconds(time)
    }

    fun setReversed(reversed: Boolean) = this.apply {
        trajectorySequenceBuilder.setReversed(reversed)
    }

    @JvmOverloads
    fun temporalMarker(offset: Double = 0.0, action: MarkerCallback) = this.apply {
        trajectorySequenceBuilder.UNSTABLE_addTemporalMarkerOffset(offset, action)
    }

    fun build() = trajectorySequenceBuilder.build()
}
