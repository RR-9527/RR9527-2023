@file:Suppress("PropertyName", "ObjectPropertyName", "FunctionName", "MemberVisibilityCanBePrivate")

package org.firstinspires.ftc.teamcodekt.components.schedulerv2

import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.components.scheduler.Action
import kotlin.math.abs

class GamepadEx2(val gamepad: Gamepad) {
    private val gamepadID = "gp${gamepad.user.id}"


    @JvmField
    val a = gamepadListener("a", gamepad::a)

    @JvmField
    val b = gamepadListener("b", gamepad::b)

    @JvmField
    val x = gamepadListener("x", gamepad::x)

    @JvmField
    val y = gamepadListener("y", gamepad::y)


    @JvmField
    val dpad_up = gamepadListener("dpad_up", gamepad::dpad_up)

    @JvmField
    val dpad_down = gamepadListener("dpad_down", gamepad::dpad_down)

    @JvmField
    val dpad_left = gamepadListener("dpad_left", gamepad::dpad_left)

    @JvmField
    val dpad_right = gamepadListener("dpad_right", gamepad::dpad_right)


    @JvmField
    val left_bumper = gamepadListener("left_bumper", gamepad::left_bumper)

    @JvmField
    val right_bumper = gamepadListener("right_bumper", gamepad::right_bumper)


    @JvmField
    val left_stick_x = left_stick_x(deadzone = .5)

    fun left_stick_x(deadzone: Double): GamepadEx2Listener {
        return gamepadListener("left_stick") { abs(gamepad.left_stick_x) > deadzone }
    }


    @JvmField
    val left_stick_y = left_stick_x(deadzone = .5)

    fun left_stick_y(deadzone: Double): GamepadEx2Listener {
        return gamepadListener("left_stick") { abs(gamepad.left_stick_y) > deadzone }
    }


    @JvmField
    val right_stick_x = right_stick_x(deadzone = .5)

    fun right_stick_x(deadzone: Double): GamepadEx2Listener {
        return gamepadListener("right_stick") { abs(gamepad.right_stick_x) > deadzone }
    }


    @JvmField
    val right_stick_y = right_stick_x(deadzone = .5)

    fun right_stick_y(deadzone: Double): GamepadEx2Listener {
        return gamepadListener("right_stick") { abs(gamepad.right_stick_y) > deadzone }
    }


    @JvmField
    val left_trigger = left_trigger(deadzone = .5)

    fun left_trigger(deadzone: Double): GamepadEx2Listener {
        return gamepadListener("left_trigger") { gamepad.left_trigger > deadzone }
    }


    @JvmField
    val right_trigger = left_trigger(deadzone = .5)

    fun right_trigger(deadzone: Double): GamepadEx2Listener {
        return gamepadListener("right_trigger") { gamepad.right_trigger > deadzone }
    }


    @JvmField
    val joysticks = joysticks(deadzone = .5)

    fun joysticks(deadzone: Double): GamepadEx2Listener {
        return gamepadListener("joysticks") {
            abs(gamepad.left_stick_x)  > deadzone ||
            abs(gamepad.left_stick_y)  > deadzone ||
            abs(gamepad.right_stick_x) > deadzone ||
            abs(gamepad.right_stick_y) > deadzone
        }
    }


    private fun gamepadListener(id: String, condition: () -> Boolean) = object : GamepadEx2Listener {
        private val _id = "${gamepadID}$id"

        override fun onTriggered(action: Action) {
            Scheduler.getOrCreateListener(_id, condition).subscribe(action, on = SignalTriggers.RISING_EDGE)
        }

        override fun onUntriggered(action: Action) {
            Scheduler.getOrCreateListener(_id, condition).subscribe(action, on = SignalTriggers.FALLING_EDGE)
        }

        override fun whileTriggered(action: Action) {
            Scheduler.getOrCreateListener(_id, condition).subscribe(action, on = SignalTriggers.IS_HIGH)
        }
    }
}

