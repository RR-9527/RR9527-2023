package org.firstinspires.ftc.teamcodekt.opmodes.teleop

import com.acmerobotics.roadrunner.localization.Localizer
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.roadrunner.drive.StandardTrackingWheelLocalizer
import org.firstinspires.ftc.teamcodekt.components.gamepad.getDriveSticks
import org.firstinspires.ftc.teamcodekt.components.gamepad.isJoystickTriggered
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotors
import org.firstinspires.ftc.teamcodekt.util.LateInitVal
import kotlin.math.*

@Disabled
@TeleOp(name = "BetterTestOpKt")
open class BetterTestOp : OpMode() {
    private var motors: DriveMotors by LateInitVal()
    private var localizer: Localizer by LateInitVal()

    private var driveFunction = ::driveNormal

    override fun init() {
        motors = DriveMotors(hardwareMap)
        localizer = StandardTrackingWheelLocalizer(hardwareMap)
    }

    override fun loop() {
        measureLoopTime()

        drive()

        localizer.update()
        telemetry.update()
    }

    private val loopTimes = mutableListOf(System.currentTimeMillis())

    private fun measureLoopTime() {
        loopTimes += System.currentTimeMillis() - loopTimes.last()
        if (loopTimes.size > 50)
            loopTimes.removeAt(1)

        telemetry.addData("Avg. loop time", loopTimes.average())
    }

    private fun drive() {
        if (gamepad1.dpad_left ) driveFunction = ::driveNormal
        if (gamepad1.dpad_up   ) driveFunction = ::driveImproved
        if (gamepad1.dpad_right) driveFunction = ::driveFc

        driveFunction()

        telemetry.addData("Drive Type", driveFunction::class.simpleName)
        motors.logData(telemetry) { it.power }
    }

    private fun driveNormal() = with(gamepad1) {
        val (speed, strafe, rotation) = gamepad1.getDriveSticks()

        val flp = speed + strafe + rotation
        val frp = speed - strafe - rotation
        val blp = speed - strafe + rotation
        val brp = speed + strafe - rotation

        val powerScale = listOf(flp, frp, blp, brp)
            .maxByOrNull(Float::absoluteValue)!!
            .coerceAtLeast(1f)

        val powerMulti = when {
            !gamepad1.isJoystickTriggered() -> 0.0
            left_trigger > 0.8 -> (1.0 - left_trigger).coerceAtLeast(.1)
            else -> 1.0
        }

        motors.setPowers(flp, frp, blp, brp)
        motors.transformPowers { it * powerMulti / powerScale }
    }

    private fun driveImproved() = with(gamepad1) {
        val (speed, strafe, rotation) = gamepad1.getDriveSticks()

        val direction = atan2(speed, strafe)
        val power = sqrt(speed * speed + strafe * strafe)

        val xComponent = sin(direction - PI / 4) * power
        val yComponent = cos(direction - PI / 4) * power

        val max = max(abs(xComponent), abs(yComponent))

        val flp = ((yComponent / max).takeUnless(Double::isNaN) ?: .0) + rotation
        val frp = ((xComponent / max).takeUnless(Double::isNaN) ?: .0) - rotation
        val blp = ((xComponent / max).takeUnless(Double::isNaN) ?: .0) + rotation
        val brp = ((yComponent / max).takeUnless(Double::isNaN) ?: .0) - rotation

        val powerScale = listOf(flp, frp, blp, brp, 1.0).maxOf { abs(it) }

        val powerMulti = when {
            !gamepad1.isJoystickTriggered() -> 0.0
            left_trigger > 0.8 -> (1.0 - left_trigger).coerceAtLeast(.1)
            else -> 1.0
        }

        motors.setPowers(flp, frp, blp, brp)
        motors.transformPowers { it * powerMulti / powerScale }
    }

    private fun driveFc() = with(gamepad1) {
        val (speed, strafe, turn) = gamepad1.getDriveSticks()

        val heading = -localizer.poseEstimate.heading
        val xRotation = strafe * cos(heading) - speed * sin(heading)
        val yRotation = strafe * sin(heading) + speed * cos(heading)

        val flp = yRotation + xRotation + turn
        val frp = yRotation - xRotation - turn
        val blp = yRotation - xRotation + turn
        val brp = yRotation + xRotation - turn

        val powerScale = listOf(flp, frp, blp, brp).maxOf { abs(it) }
            .coerceAtLeast(1.0)

        val powerMulti = when {
            !gamepad1.isJoystickTriggered() -> 0.0
            left_trigger > 0.8 -> 1.0 - left_trigger
            else -> 1.0
        }

        motors.setPowers(flp, frp, blp, brp)
        motors.transformPowers { it * powerMulti / powerScale }
    }
}