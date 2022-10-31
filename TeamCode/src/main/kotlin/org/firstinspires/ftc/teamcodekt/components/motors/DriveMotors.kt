package org.firstinspires.ftc.teamcodekt.components.motors

import com.acmerobotics.roadrunner.localization.Localizer
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcodekt.components.gamepad.getDriveSticks
import org.firstinspires.ftc.teamcodekt.components.gamepad.isJoystickTriggered
import org.firstinspires.ftc.teamcodekt.util.DataSupplier
import kotlin.math.*

/**
 * The bot's drive motors logically grouped together.
 *
 * Kotlin usage example:
 * ```
 * fun main() {
 *   val motors = initializedDriveMotors(hardwareMap)
 *   motors.frontLeft.power = 0.5
 *   motors.logData(telemetry) { it.power }
 * }
 * ```
 *
 * Java usage example:
 * ```java
 * public static void main(String... args) {;
 *   DriveMotors motors = DriveMotorsKt.initializedDriveMotors(hardwareMap);
 *   motors.getFrontLeft().setPower(0.5);
 *   motors.logData(telemetry, DcMotorEx::getPower);
 * }
 * ```
 * _Note: Both language examples produce the exact same outputs_
 *
 * @property frontLeft The front left motor.
 * @property frontRight The front right motor.
 * @property backLeft The back left motor.
 * @property backRight The back right motor.
 *
 * @see [initializedDriveMotors]
 *
 * @author KG
 */
class DriveMotors(hwMap: HardwareMap) {
    private val frontLeft  = initializedMotor("FL", hwMap, reversed = true)
    private val frontRight = initializedMotor("FR", hwMap)
    private val backLeft   = initializedMotor("BL", hwMap, reversed = true)
    private val backRight  = initializedMotor("BR", hwMap)

    var driveType = DriveType.IMPROVED

    fun setPowers(flp: Number, frp: Number, blp: Number, brp: Number) {
        frontLeft.power   =   flp.toDouble()
        frontRight.power  =   frp.toDouble()
        backLeft.power    =   blp.toDouble()
        backRight.power   =   brp.toDouble()
    }

    fun transformPowers(scaleFunction: (Double) -> Double) {
        frontLeft.power   =   scaleFunction(frontLeft.power)
        frontRight.power  =   scaleFunction(frontRight.power)
        backLeft.power    =   scaleFunction(backLeft.power)
        backRight.power   =   scaleFunction(backRight.power)
    }

    fun logData(telemetry: Telemetry, dataSupplier: DataSupplier<DcMotorEx>) {
        telemetry.addData("Front-left motor:",  dataSupplier(frontLeft))
        telemetry.addData("Front-right motor:", dataSupplier(frontRight))
        telemetry.addData("Back-left motor:",   dataSupplier(backLeft))
        telemetry.addData("Back-right motor:",  dataSupplier(backRight))
    }

    fun drive(gamepad: Gamepad, localizer: Localizer, powerMulti: Double) = when (driveType) {
        DriveType.NORMAL -> driveNormal(gamepad, powerMulti)
        DriveType.IMPROVED -> driveImproved(gamepad, powerMulti)
        DriveType.FIELD_CENTRIC -> driveFc(gamepad, localizer, powerMulti)
    }

    private fun driveNormal(gamepad: Gamepad, _powerMulti: Double) = with(gamepad) {
        val (speed, strafe, rotation) = gamepad.getDriveSticks()

        val flp = speed + strafe + rotation
        val frp = speed - strafe - rotation
        val blp = speed - strafe + rotation
        val brp = speed + strafe - rotation

        val powerScale = listOf(flp, frp, blp, brp)
            .maxByOrNull(kotlin.Float::absoluteValue)!!
            .coerceAtLeast(1f)

        val powerMulti = if (!isJoystickTriggered()) 0.0 else _powerMulti

        setPowers(flp, frp, blp, brp)
        transformPowers { it * powerMulti / powerScale }
    }

    private fun driveImproved(gamepad: Gamepad, _powerMulti: Double) = with(gamepad) {
        val (speed, strafe, rotation) = gamepad.getDriveSticks()

        val direction = atan2(speed, strafe)
        val power = sqrt(speed * speed + strafe * strafe)

        val xComponent = sin(direction - PI / 4) * power
        val yComponent = cos(direction - PI / 4) * power

        val max = max(abs(xComponent), abs(yComponent))

        val flp = ((yComponent / max).takeUnless(kotlin.Double::isNaN) ?: .0) + rotation
        val frp = ((xComponent / max).takeUnless(kotlin.Double::isNaN) ?: .0) - rotation
        val blp = ((xComponent / max).takeUnless(kotlin.Double::isNaN) ?: .0) + rotation
        val brp = ((yComponent / max).takeUnless(kotlin.Double::isNaN) ?: .0) - rotation

        val powerScale = listOf(flp, frp, blp, brp, 1.0).maxOf { abs(it) }

        val powerMulti = if (!isJoystickTriggered()) 0.0 else _powerMulti

        setPowers(flp, frp, blp, brp)
        transformPowers { it * powerMulti / powerScale }
    }

    private fun driveFc(gamepad: Gamepad, localizer: Localizer, _powerMulti: Double) = with(gamepad) {
        val (speed, strafe, turn) = gamepad.getDriveSticks()

        val heading = -localizer.poseEstimate.heading
        val xRotation = strafe * cos(heading) - speed * sin(heading)
        val yRotation = strafe * sin(heading) + speed * cos(heading)

        val flp = yRotation + xRotation + turn
        val frp = yRotation - xRotation - turn
        val blp = yRotation - xRotation + turn
        val brp = yRotation + xRotation - turn

        val powerScale = listOf(flp, frp, blp, brp).maxOf { abs(it) }
            .coerceAtLeast(1.0)

        val powerMulti = if (!isJoystickTriggered()) 0.0 else _powerMulti

        setPowers(flp, frp, blp, brp)
        transformPowers { it * powerMulti / powerScale }
    }
}

