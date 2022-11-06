package org.firstinspires.ftc.teamcodekt.components.scheduler.auto

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive

interface AutoProvider {
    fun scheduleAuto(drive: SampleMecanumDrive)
}