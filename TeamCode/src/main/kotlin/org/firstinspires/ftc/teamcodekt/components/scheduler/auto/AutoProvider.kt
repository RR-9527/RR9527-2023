package org.firstinspires.ftc.teamcodekt.components.scheduler.auto

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence

interface AutoProvider {
    fun createAuto(drive: SampleMecanumDrive): TrajectorySequence?
}