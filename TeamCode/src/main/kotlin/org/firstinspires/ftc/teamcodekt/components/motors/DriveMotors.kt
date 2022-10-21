package org.firstinspires.ftc.teamcodekt.components.motors

import com.acmerobotics.roadrunner.localization.Localizer
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcodekt.components.gamepad.getDriveSticks
import org.firstinspires.ftc.teamcodekt.components.gamepad.isJoystickTriggered
import org.firstinspires.ftc.teamcodekt.util.DataSupplier
import org.firstinspires.ftc.teamcodekt.util.LateInitVal
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
class DriveMotors {
    var frontLeft:  DcMotorEx by LateInitVal()
    var frontRight: DcMotorEx by LateInitVal()
    var backLeft:   DcMotorEx by LateInitVal()
    var backRight:  DcMotorEx by LateInitVal()

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

    fun drive(gamepad: Gamepad, localizer: Localizer, type: DriveType) = when (type) {
        DriveType.NORMAL -> driveNormal(gamepad)
        DriveType.IMPROVED -> driveImproved(gamepad)
        DriveType.FIELD_CENTRIC -> driveFc(gamepad, localizer)
    }

    private fun driveNormal(gamepad: Gamepad) = with(gamepad) {
        val (speed, strafe, rotation) = gamepad.getDriveSticks()

        val flp = speed + strafe + rotation
        val frp = speed - strafe - rotation
        val blp = speed - strafe + rotation
        val brp = speed + strafe - rotation

        val powerScale = listOf(flp, frp, blp, brp)
            .maxByOrNull(kotlin.Float::absoluteValue)!!
            .coerceAtLeast(1f)

        val powerMulti = when {
            !gamepad.isJoystickTriggered() -> 0.0
            left_trigger > 0.8 -> (1.0 - left_trigger).coerceAtLeast(.1)
            else -> 1.0
        }

        setPowers(flp, frp, blp, brp)
        transformPowers { it * powerMulti / powerScale }
    }

    private fun driveImproved(gamepad: Gamepad) = with(gamepad) {
        val (speed, strafe, rotation) = gamepad.getDriveSticks()

        val direction = atan2(speed, strafe)
        val power = sqrt(speed * speed + strafe * strafe)

        val xComponent = sin(direction - kotlin.math.PI / 4) * power
        val yComponent = cos(direction - kotlin.math.PI / 4) * power

        val max = max(abs(xComponent), abs(yComponent))

        val flp = ((yComponent / max).takeUnless(kotlin.Double::isNaN) ?: .0) + rotation
        val frp = ((xComponent / max).takeUnless(kotlin.Double::isNaN) ?: .0) - rotation
        val blp = ((xComponent / max).takeUnless(kotlin.Double::isNaN) ?: .0) + rotation
        val brp = ((yComponent / max).takeUnless(kotlin.Double::isNaN) ?: .0) - rotation

        val powerScale = listOf(flp, frp, blp, brp, 1.0).maxOf { abs(it) }

        val powerMulti = when {
            !gamepad.isJoystickTriggered() -> 0.0
            left_trigger > 0.8 -> (1.0 - left_trigger).coerceAtLeast(.1)
            else -> 1.0
        }

        setPowers(flp, frp, blp, brp)
        transformPowers { it * powerMulti / powerScale }
    }

    private fun driveFc(gamepad: Gamepad, localizer: Localizer) = with(gamepad) {
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

        val powerMulti = when {
            !gamepad.isJoystickTriggered() -> 0.0
            left_trigger > 0.8 -> 1.0 - left_trigger
            else -> 1.0
        }

        setPowers(flp, frp, blp, brp)
        transformPowers { it * powerMulti / powerScale }
    }
}

enum class DriveType {
    NORMAL, IMPROVED, FIELD_CENTRIC
}

/**
 * Initializes a [DriveMotors] object with the default configurations.
 *
 * Kotlin usage example:
 * ```
 * val motors = initializedDriveMotors(hardwareMap)
 * ```
 *
 * Java usage example:
 * ```
 * DriveMotors motors = DriveMotorsKt.initializedDriveMotors(hardwareMap)
 * ```
 *
 * @param hwMap The [HardwareMap]
 * @return A [DriveMotors] object with the motors initialized initialized.`
 *
 * @author KG
 */
fun initializedDriveMotors(hwMap: HardwareMap) = DriveMotors().apply {
    frontLeft   =   initializedMotor("FL", hwMap)
    frontRight  =   initializedMotor("FR", hwMap)
    backLeft    =   initializedMotor("BL", hwMap)
    backRight   =   initializedMotor("BR", hwMap)
}

/**
 * Initializes a [DriveMotors] object with the default configurations 2.0.
 * Just the same as the above, except the left motors are reversed
 *
 * @param hwMap The [HardwareMap]
 * @return A [DriveMotors] object with the motors initialized initialized.`
 *
 * @author KG
 */
fun initializedDriveMotorsV2(hwMap: HardwareMap) = DriveMotors().apply {
    frontLeft   =   initializedMotor("FL", hwMap, reversed = true)
    frontRight  =   initializedMotor("FR", hwMap)
    backLeft    =   initializedMotor("BL", hwMap, reversed = true)
    backRight   =   initializedMotor("BR", hwMap)
}