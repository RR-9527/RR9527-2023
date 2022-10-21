@file:Suppress("PropertyName", "ObjectPropertyName", "FunctionName", "MemberVisibilityCanBePrivate")

package org.firstinspires.ftc.teamcodekt.components.schedulerv2

import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcodekt.util.Condition
import kotlin.math.abs

/**
 * A wrapper around the base [Gamepad] class that can create [Listeners][Listener] for each button.
 * Listeners are only hooked when it's usage is required.
 *
 * Java usage example:
 * ```java
 * @Override
 * public void runOpMode() throws InterruptedException {
 *     GamepadEx2 gamepadx1 = new GamepadEx2(gamepad1);
 *
 *     // Use onRise and onFall to run the action once when the
 *     // condition is first true or first false.
 *     // (In this case, when the button is first pressed or released)
 *     gamepadx1.a.onRise(this::openClaw)
 *                .onFall(this::closeClaw);
 *
 *     // Use onHigh if the action should be run as long as
 *     // the condition is true, and onLow for the opposite.
 *     gamepadx1.left_trigger(.1).onHigh(this::driveSlow) // <- Notice method
 *                               .onLow(this::driveFast); // chaining allowed
 *
 *     // Gamepad buttons returning a float can be passed an option deadzone.
 *     // If called as a normal variable, it defaults to .5.
 *     // e.g.
 *     // `gamepadx1.left_trigger(.1)` triggers when abs(left_trigger) > .1
 *     // `gamepadx1.left_trigger` triggers when abs(left_trigger) > .5
 *
 *     // Runs the code while the OpMode is active.
 *     Scheduler.run(this, () -> {
 *         // Optional block of code to be run after the above listeners
 *         // This parameter may be omitted if unnecessary.
 *         updateLift();
 *     });
 * }
 *
 * private void openClaw() {
 *     claw.setPosition(Claw.OPEN);
 * }
 *
 * //...
 * ```
 *
 * @author KG
 *
 * @see Gamepad
 * @see Scheduler
 */
class GamepadEx2(val gamepad: Gamepad) {
    private val userID = "gp${gamepad.user.id}"


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


    private fun gamepadTrigger(id: String, condition: Condition): GamepadEx2Trigger {
        return GamepadEx2Trigger("${userID}$id", condition)
    }
}

