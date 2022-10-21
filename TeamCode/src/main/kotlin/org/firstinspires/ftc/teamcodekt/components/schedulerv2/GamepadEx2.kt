@file:Suppress("PropertyName", "ObjectPropertyName", "FunctionName", "MemberVisibilityCanBePrivate")

package org.firstinspires.ftc.teamcodekt.components.schedulerv2

import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcodekt.util.Condition
import kotlin.math.abs

class GamepadEx2(val gamepad: Gamepad) {
    private val gamepadID = "gp${gamepad.user.id}"


    @JvmField
    val a = gamepadTrigger("a", gamepad::a)

    @JvmField
    val b = gamepadTrigger("b", gamepad::b)

    @JvmField
    val x = gamepadTrigger("x", gamepad::x)

    @JvmField
    val y = gamepadTrigger("y", gamepad::y)


    @JvmField
    val dpad_up = gamepadTrigger("dpad_up", gamepad::dpad_up)

    @JvmField
    val dpad_down = gamepadTrigger("dpad_down", gamepad::dpad_down)

    @JvmField
    val dpad_left = gamepadTrigger("dpad_left", gamepad::dpad_left)

    @JvmField
    val dpad_right = gamepadTrigger("dpad_right", gamepad::dpad_right)


    @JvmField
    val left_bumper = gamepadTrigger("left_bumper", gamepad::left_bumper)

    @JvmField
    val right_bumper = gamepadTrigger("right_bumper", gamepad::right_bumper)


    @JvmField
    val left_stick_x = left_stick_x(deadzone = .5)

    fun left_stick_x(deadzone: Double): GamepadEx2Trigger {
        return gamepadTrigger("left_stick$deadzone") { abs(gamepad.left_stick_x) > deadzone }
    }


    @JvmField
    val left_stick_y = left_stick_x(deadzone = .5)

    fun left_stick_y(deadzone: Double): GamepadEx2Trigger {
        return gamepadTrigger("left_stick$deadzone") { abs(gamepad.left_stick_y) > deadzone }
    }


    @JvmField
    val right_stick_x = right_stick_x(deadzone = .5)

    fun right_stick_x(deadzone: Double): GamepadEx2Trigger {
        return gamepadTrigger("right_stick$deadzone") { abs(gamepad.right_stick_x) > deadzone }
    }


    @JvmField
    val right_stick_y = right_stick_x(deadzone = .5)

    fun right_stick_y(deadzone: Double): GamepadEx2Trigger {
        return gamepadTrigger("right_stick$deadzone") { abs(gamepad.right_stick_y) > deadzone }
    }


    @JvmField
    val left_trigger = left_trigger(deadzone = .5)

    fun left_trigger(deadzone: Double): GamepadEx2Trigger {
        return gamepadTrigger("left_trigger$deadzone") { gamepad.left_trigger > deadzone }
    }


    @JvmField
    val right_trigger = left_trigger(deadzone = .5)

    fun right_trigger(deadzone: Double): GamepadEx2Trigger {
        return gamepadTrigger("right_trigger$deadzone") { gamepad.right_trigger > deadzone }
    }


    @JvmField
    val joysticks = joysticks(deadzone = .5)

    fun joysticks(deadzone: Double): GamepadEx2Trigger {
        return gamepadTrigger("joysticks$deadzone") {
            abs(gamepad.left_stick_x)  > deadzone ||
            abs(gamepad.left_stick_y)  > deadzone ||
            abs(gamepad.right_stick_x) > deadzone ||
            abs(gamepad.right_stick_y) > deadzone
        }
    }


    private fun gamepadTrigger(id: String, condition: Condition) = object : GamepadEx2Trigger {
        private val _id = "${gamepadID}$id"

        override fun onRise(action: Runnable) = this.also {
            Scheduler.getOrCreateListener(_id, condition).subscribe(action, on = SignalTriggers.RISING_EDGE)
        }

        override fun onFall(action: Runnable) = this.also {
            Scheduler.getOrCreateListener(_id, condition).subscribe(action, on = SignalTriggers.FALLING_EDGE)
        }

        override fun onHigh(action: Runnable) = this.also {
            Scheduler.getOrCreateListener(_id, condition).subscribe(action, on = SignalTriggers.IS_HIGH)
        }

        override fun onLow(action: Runnable) = this.also {
            Scheduler.getOrCreateListener(_id, condition).subscribe(action, on = SignalTriggers.IS_LOW)
        }
    }
}

