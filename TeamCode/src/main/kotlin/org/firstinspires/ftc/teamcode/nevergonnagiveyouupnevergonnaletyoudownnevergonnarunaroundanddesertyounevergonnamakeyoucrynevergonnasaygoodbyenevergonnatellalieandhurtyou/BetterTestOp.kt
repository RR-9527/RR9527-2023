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
        telemetry.update()
    }

    private fun drive() {
        if (gamepad1.y) {
            driveType = when (driveType) {
                is NormalDrive -> NormalImprovedDrive()
                is NormalImprovedDrive -> FieldCentricDrive()
                is FieldCentricDrive -> NormalDrive()
            }
        }

        driveType.drive()
        telemetry.addData("Drive Type", driveType::class.simpleName)
    }

    private fun driveNormal() = with(gamepad1) {
        val (speed, strafe, rotation) = gamepad1.getDriveSticks()

        val flp = speed + strafe + rotation
        val frp = speed - strafe - rotation
        val blp = speed - strafe + rotation
        val brp = speed + strafe - rotation

        val powerScale = listOf(flp, frp, blp, brp, 1f)
            .maxByOrNull(Float::absoluteValue)!!

        val powerMulti = when {
            !gamepad1.isJoystickTriggered() -> 0.0
            left_trigger > 0.8 -> 1.0 - left_trigger
            else -> 1.0
        }

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

        val flp = yComponent / max + rotation
        val frp = xComponent / max - rotation
        val blp = xComponent / max + rotation
        val brp = yComponent / max - rotation

        val powerScale = (power + rotation).coerceAtLeast(1f)

        val powerMulti = when {
            !gamepad1.isJoystickTriggered() -> 0.0
            left_trigger > 0.8 -> 1.0 - left_trigger
            else -> 1.0
        }

        motors.setPowers(flp, frp, blp, brp)
        motors.scalePowers { it * powerMulti / powerScale }
    }


    private fun driveFc() = with(gamepad1) {
        val (speed, strafe, turn) = gamepad1.getDriveSticks()

        val heading = localizer.poseEstimate.heading
        val xRotation = strafe * cos(heading) - speed * sin(heading)
        val yRotation = strafe * sin(heading) + speed * cos(heading)

        val flp = yRotation + xRotation + turn
        val frp = yRotation - xRotation - turn
        val blp = yRotation - xRotation + turn
        val brp = yRotation + xRotation - turn

        val powerScale = listOf(flp, frp, blp, brp, 1.0)
            .maxByOrNull(Double::absoluteValue)!!

        val powerMulti = when {
            !gamepad1.isJoystickTriggered() -> 0.0
            left_trigger > 0.8 -> 1.0 - left_trigger
            else -> 1.0
        }

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

    private inner class NormalDrive : DriveType({ driveNormal() })
    private inner class NormalImprovedDrive : DriveType({ driveImproved() })
    private inner class FieldCentricDrive : DriveType({ driveFc() })

    private fun initializedDriveMotorsV2(hwMap: HardwareMap) = DriveMotors().apply {
        frontLeft = initializedMotor("FL", hwMap)
        frontRight = initializedMotor("FR", hwMap, reversed = true)
        backLeft = initializedMotor("BL", hwMap)
        backRight = initializedMotor("BR", hwMap, reversed = true)
    }
}