package org.firstinspires.ftc.teamcode.nevergonnagiveyouupnevergonnaletyoudownnevergonnarunaroundanddesertyounevergonnamakeyoucrynevergonnasaygoodbyenevergonnatellalieandhurtyou

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.components.deadwheels.Deadwheels
import org.firstinspires.ftc.teamcode.components.deadwheels.initializedDeadwheels
import org.firstinspires.ftc.teamcode.components.motors.DriveMotors
import org.firstinspires.ftc.teamcode.components.motors.initializedDriveMotors
import org.firstinspires.ftc.teamcode.components.shooter.Shooter
import org.firstinspires.ftc.teamcode.components.shooter.initializedShooter
import org.firstinspires.ftc.teamcode.util.LateInitVal
import kotlin.math.abs
import kotlin.math.absoluteValue

@TeleOp(name = "TestOpKt")
class TestOp : OpMode() {
    private var motors: DriveMotors by LateInitVal()
    private var deadwheels: Deadwheels by LateInitVal()
    private var shooter: Shooter by LateInitVal()

    //TODO: Probably remove the deadwheels from the TeleOp
    override fun init() {
        motors = initializedDriveMotors(hardwareMap)
        deadwheels = initializedDeadwheels(motors)
        shooter = initializedShooter(hardwareMap)
    }

    override fun loop() {
        drive()
        shoot()

        deadwheels.snapshotTicks()
        updateTelemetry(telemetry)
    }

    private fun drive() = with(gamepad1) {
        val triggered = listOf(left_stick_y, left_stick_x, right_stick_x)
            .any { abs(it) > 0.1 }

        val flp = left_stick_y - left_stick_x - right_stick_x
        val frp = -left_stick_y - left_stick_x - right_stick_x
        val blp = left_stick_y + left_stick_x - right_stick_x
        val brp = -left_stick_y + left_stick_x - right_stick_x

        val max = listOf(flp, frp, blp, brp)
            .maxByOrNull(Float::absoluteValue)!!
            .coerceAtLeast(1f)

        val powerMulti = when {
            !triggered -> 0.0
            left_trigger > 0.5 -> 0.35
            else -> 1.0
        }

        motors.frontLeft.power =  flp * powerMulti / max
        motors.frontRight.power = frp * powerMulti / max
        motors.backLeft.power = blp * powerMulti / max
        motors.backRight.power = brp * powerMulti / max

        motors.logData(telemetry) { it.power }
        shooter.logMotorData(telemetry) { it.power }
        telemetry.addData("Right trigger", gamepad1.right_trigger)
        telemetry.update()
    }

    private fun shoot() = with(shooter) {
        motor.power = gamepad1.right_trigger.toDouble().takeIf { it > 0.6 } ?: 0.0
        setIndexerToggled(gamepad1.a)
    }
}