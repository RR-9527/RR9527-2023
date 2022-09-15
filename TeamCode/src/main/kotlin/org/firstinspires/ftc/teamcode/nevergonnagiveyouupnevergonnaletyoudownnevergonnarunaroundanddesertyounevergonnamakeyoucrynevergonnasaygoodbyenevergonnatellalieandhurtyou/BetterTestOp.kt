package org.firstinspires.ftc.teamcode.nevergonnagiveyouupnevergonnaletyoudownnevergonnarunaroundanddesertyounevergonnamakeyoucrynevergonnasaygoodbyenevergonnatellalieandhurtyou

import com.acmerobotics.roadrunner.localization.Localizer
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.components.gamepad.getDriveSticks
import org.firstinspires.ftc.teamcode.components.gamepad.isJoystickTriggered
import org.firstinspires.ftc.teamcode.components.motors.DriveMotors
import org.firstinspires.ftc.teamcode.components.motors.initializedMotor
import org.firstinspires.ftc.teamcode.components.shooter.Shooter
import org.firstinspires.ftc.teamcode.components.shooter.initializedShooter
import org.firstinspires.ftc.teamcode.roadrunner.drive.StandardTrackingWheelLocalizer
import org.firstinspires.ftc.teamcode.util.LateInitVal
import kotlin.math.*

@TeleOp(name = "BetterTestOpKt")
class BetterTestOp : OpMode() {
    private var motors: DriveMotors by LateInitVal()
    private var shooter: Shooter by LateInitVal()
    private var localizer: Localizer by LateInitVal()

    private var driveType: DriveType = NormalDrive()

    override fun init() {
        motors = initializedDriveMotorsV2(hardwareMap)
        shooter = initializedShooter(hardwareMap)
        localizer = StandardTrackingWheelLocalizer(hardwareMap)
    }

    override fun loop() {
        drive()
        shoot()
        localizer.update()
        telemetry.update()
    }

    private fun drive() {
        driveType = when {
            gamepad1.dpad_left -> NormalDrive()
            gamepad1.dpad_up -> FieldCentricDrive()
            gamepad1.dpad_right -> NormalImprovedDrive()
            else -> driveType
        }

        driveType.drive()
        telemetry.addData("Drive Type", driveType::class.simpleName)
        motors.logData(telemetry) { it.power }
        telemetry.addData("Heading", localizer.poseEstimate.heading)
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

        telemetry.addData("Power scale", powerScale)
        telemetry.addData("Power multi", powerMulti)

        motors.setPowers(flp, frp, blp, brp)
        motors.scalePowers { it * powerMulti / powerScale }
    }

    private fun driveImproved() = with(gamepad1) {
        val (speed, strafe, rotation) = gamepad1.getDriveSticks()

        val direction = atan2(speed, strafe)
        val power = sqrt(speed * speed + strafe * strafe)

        val xComponent = sin(direction - PI / 4) * power
        val yComponent = cos(direction - PI / 4) * power

        val max = max(abs(xComponent), abs(yComponent))

        telemetry.addData("Max", max);

        val flp = ((yComponent / max).takeUnless(Double::isNaN) ?: .0) + rotation
        val frp = ((xComponent / max).takeUnless(Double::isNaN) ?: .0) - rotation
        val blp = ((xComponent / max).takeUnless(Double::isNaN) ?: .0) + rotation
        val brp = ((yComponent / max).takeUnless(Double::isNaN) ?: .0) - rotation

        val powerScale = listOf(flp, frp, blp, brp)
            .maxByOrNull(Double::absoluteValue)!!
            .coerceAtLeast(1.0)

        val powerMulti = when {
            !gamepad1.isJoystickTriggered() -> 0.0
            left_trigger > 0.8 -> (1.0 - left_trigger).coerceAtLeast(.1)
            else -> 1.0
        }

        telemetry.addData("Rotation", rotation)
        telemetry.addData("Flp", flp)

        motors.setPowers(flp, frp, blp, brp)
        motors.scalePowers { it * powerMulti / powerScale }
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

        val powerScale = listOf(flp, frp, blp, brp)
            .map { abs(it) }
            .maxByOrNull(Double::absoluteValue)!!
            .coerceAtLeast(1.0)

        val powerMulti = when {
            !gamepad1.isJoystickTriggered() -> 0.0
            left_trigger > 0.8 -> 1.0 - left_trigger
            else -> 1.0
        }

        telemetry.addData("FLP", flp * powerMulti / powerScale)
        telemetry.addData("FRP", frp * powerMulti / powerScale)
        telemetry.addData("BLP", blp * powerMulti / powerScale)
        telemetry.addData("BRP", brp * powerMulti / powerScale)

        motors.setPowers(flp, frp, blp, brp)
        motors.scalePowers { it * powerMulti / powerScale }
    }

    private fun shoot() = with(shooter) {
        motor.power = gamepad1.right_trigger.toDouble().takeIf { it > 0.6 } ?: 0.0
        setIndexerToggled(gamepad1.a)
    }

    private sealed class DriveType(val driveFunction: () -> Unit) {
        fun drive() = driveFunction()
    }

    private inner class NormalDrive : DriveType(::driveNormal)
    private inner class NormalImprovedDrive : DriveType(::driveImproved)
    private inner class FieldCentricDrive : DriveType(::driveFc)

    private fun initializedDriveMotorsV2(hwMap: HardwareMap) = DriveMotors().apply {
        frontLeft = initializedMotor("FL", hwMap, reversed = true)
        frontRight = initializedMotor("FR", hwMap)
        backLeft = initializedMotor("BL", hwMap, reversed = true)
        backRight = initializedMotor("BR", hwMap)
    }
}