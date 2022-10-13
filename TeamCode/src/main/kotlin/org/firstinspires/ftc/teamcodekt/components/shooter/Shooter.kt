package org.firstinspires.ftc.teamcodekt.components.shooter

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcodekt.components.motors.DCMode
import org.firstinspires.ftc.teamcodekt.components.motors.ZPB
import org.firstinspires.ftc.teamcodekt.components.motors.initializedMotor
import org.firstinspires.ftc.teamcodekt.components.servos.initializedServo
import org.firstinspires.ftc.teamcodekt.util.DataSupplier
import org.firstinspires.ftc.teamcodekt.util.LateInitVal

/**
 * Logically grouped components of the bot's ring shooter.
 *
 * @property indexer The servo that controls throughput of the rings into the flywheel
 * @property motor The motor that powers the shooter.
 *
 * @see [initializedShooter]
 *
 * @author KG
 */
class Shooter {
    var indexer: Servo by LateInitVal()
    var motor: DcMotorEx by LateInitVal()

    /**
     * Sets the shooter motor power to the given input, as long as it's above the optional
     * deadzone. If the given power is below this deadzone, it is set to `0`.
     *
     * @param power The desired power
     * @param minThreshold The minimum power threshold; defaults to `0.1`
     */
    fun setPower(power: Float, minThreshold: Double = 0.1) {
        motor.power = power.toDouble().takeIf { it > minThreshold } ?: 0.0
    }

    /**
     * Sets the indexer to either the `INDEXER_FORWARD` or `INDEXER_BACK` position depending on
     * the input. `true` corresponds to `INDEXER_FORWARD`.
     *
     * @param value Whether or not the indexer is engaged
     */
    fun setIndexerToggled(value: Boolean) {
        if (motor.velocity < .3f) {
            indexer.position = INDEXER_BACK
        } else{
            indexer.position = if (value) INDEXER_FORWARD else INDEXER_BACK
        }
    }

    fun logIndexerData(telemetry: Telemetry, dataSupplier: DataSupplier<Servo>) {
        telemetry.addData("Indexer", dataSupplier(indexer))
    }

    fun logMotorData(telemetry: Telemetry, dataSupplier: DataSupplier<DcMotorEx>) {
        telemetry.addData("Motor", dataSupplier(motor))
    }

    /**
     * The constants that represent the servo positions for the indexer.
     *
     * @property INDEXER_FORWARD The position where the indexer pushes a ring into the flywheel.
     * @property INDEXER_BACK The position where the indexer is at rest, not doing anything.
     */
    companion object {
        const val INDEXER_BACK = .51
        const val INDEXER_FORWARD = .60
    }
}

/**
 * Initializes a [Shooter]'s `motor` and `indexer` with their default configurations.
 *
 * Kotlin usage example:
 * ```
 * val shooter = initializedShooter(hardwareMap)
 * ```
 *
 * Java usage example:
 * ```java
 * Shooter shooter = ShooterKt.initializedShooter(hardwareMap);
 * ```
 *
 * @param hwMap The [HardwareMap]
 * @return A [Shooter] object with the `indexer` and `motor` initialized.`
 *
 * @author KG
 */
fun initializedShooter(hwMap: HardwareMap) = Shooter().apply {
    indexer = initializedServo("IND", hwMap, pos = Shooter.INDEXER_BACK)
    motor = initializedMotor("SH", hwMap, zpb = ZPB.FLOAT, runMode = DCMode.RUN_USING_ENCODER)
}