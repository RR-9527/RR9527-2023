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
 *     Scheduler.start(this, () -> {
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
 * @see Trigger
 */
class GamepadEx2(val gamepad: Gamepad) {
    /**
     * An ID which prefixes both gamepads' triggers' listeners to allow separate listeners for
     * both gamepads.
     */
    private val gamepadEx2ID = "gp"


    /**
     * Allows client to perform an action when the gamepad's 'a' button's state is mutated.
     * ```java
     * //e.g:
     * gamepad_x1.a.onRise(this::doSomething)
     */
    @JvmField
    val a = gamepadTrigger("a", gamepad::a)

    /**
     * Allows client to perform an action when the gamepad's 'b' button's state is mutated.
     * ```java
     * //e.g:
     * gamepad_x1.b.onHigh(this::doSomething)
     */
    @JvmField
    val b = gamepadTrigger("b", gamepad::b)

    /**
     * Allows client to perform an action when the gamepad's 'x' button's state is mutated.
     * ```java
     * //e.g:
     * gamepad_x1.x.onFall(this::doSomething)
     */
    @JvmField
    val x = gamepadTrigger("x", gamepad::x)

    /**
     * Allows client to perform an action when the gamepad's 'y' button's state is mutated.
     * ```java
     * //e.g:
     * gamepad_x1.y.onLow(this::doSomething)
     */
    @JvmField
    val y = gamepadTrigger("y", gamepad::y)

    /**
     * Allows client to perform an action when the gamepad's 'dpad_up' button's state is mutated.
     * ```java
     * //e.g:
     * gamepad_x1.dpad_up.onRise(this::doSomething)
     */
    @JvmField
    val dpad_up = gamepadTrigger("dpad_up", gamepad::dpad_up)

    /**
     * Allows client to perform an action when the gamepad's 'dpad_down' button's state is mutated.
     * ```java
     * //e.g:
     * gamepad_x1.dpad_down.onHigh(this::doSomething)
     */
    @JvmField
    val dpad_down = gamepadTrigger("dpad_down", gamepad::dpad_down)

    /**
     * Allows client to perform an action when the gamepad's 'dpad_left' button's state is mutated.
     * ```java
     * //e.g:
     * gamepad_x1.dpad_left.onFall(this::doSomething)
     */
    @JvmField
    val dpad_left = gamepadTrigger("dpad_left", gamepad::dpad_left)

    /**
     * Allows client to perform an action when the gamepad's 'dpad_right' button's state is mutated.
     * ```java
     * //e.g:
     * gamepad_x1.dpad_right.onLow(this::doSomething)
     */
    @JvmField
    val dpad_right = gamepadTrigger("dpad_right", gamepad::dpad_right)


    /**
     * Allows client to perform an action when the gamepad's 'left_bumper' button's state is mutated.
     * ```java
     * //e.g:
     * gamepad_x1.left_bumper.onRise(this::doSomething)
     */
    @JvmField
    val left_bumper = gamepadTrigger("left_bumper", gamepad::left_bumper)

    /**
     * Allows client to perform an action when the gamepad's 'right_bumper' button's state is mutated.
     * ```java
     * //e.g:
     * gamepad_x1.right_bumper.onHigh(this::doSomething)
     */
    @JvmField
    val right_bumper = gamepadTrigger("right_bumper", gamepad::right_bumper)


    /**
     * Allows client to perform an action when the gamepad's 'left_stick_x' button's state is mutated.
     * ```java
     * //e.g: (Triggers when abs(left_stick_x) > .5)
     * gamepad_x1.left_stick_x.onFall(this::doSomething)
     */
    @JvmField
    val left_stick_x = left_stick_x(deadzone = .5)

    /**
     * Allows client to perform an action when the gamepad's 'left_stick_x' button's state is mutated.
     * ```java
     * //e.g: (Triggers when abs(left_stick_x) > deadzone)
     * gamepad_x1.left_stick_x.onFall(this::doSomething)
     *```
     * @param deadzone The minimum value that the left_stick_x must be above to trigger the event.
     */
    fun left_stick_x(deadzone: Double): Trigger {
        return gamepadTrigger("left_stick$deadzone") { abs(gamepad.left_stick_x) > deadzone }
    }


    /**
     * Allows client to perform an action when the gamepad's 'left_stick_y' button's state is mutated.
     * ```java
     * //e.g: (Triggers when abs(left_stick_y) > .5)
     * gamepad_x1.left_stick_y.onLow(this::doSomething)
     */
    @JvmField
    val left_stick_y = left_stick_y(deadzone = .5)

    /**
     * Allows client to perform an action when the gamepad's 'left_stick_y' button's state is mutated.
     * ```java
     * //e.g: (Triggers when abs(left_stick_y) > deadzone)
     * gamepad_x1.left_stick_y.onLow(this::doSomething)
     *```
     * @param deadzone The minimum value that the left_stick_y must be above to trigger the event.
     */
    fun left_stick_y(deadzone: Double): Trigger {
        return gamepadTrigger("left_stick$deadzone") { abs(gamepad.left_stick_y) > deadzone }
    }


    /**
     * Allows client to perform an action when the gamepad's 'right_stick_x' button's state is mutated.
     * ```java
     * //e.g: (Triggers when abs(right_stick_x) > .5)
     * gamepad_x1.right_stick_x.onRise(this::doSomething)
     */
    @JvmField
    val right_stick_x = right_stick_x(deadzone = .5)

    /**
     * Allows client to perform an action when the gamepad's 'right_stick_x' button's state is mutated.
     * ```java
     * //e.g: (Triggers when abs(right_stick_x) > deadzone)
     * gamepad_x1.right_stick_x.onRise(this::doSomething)
     *```
     * @param deadzone The minimum value that the right_stick_x must be above to trigger the event.
     */
    fun right_stick_x(deadzone: Double): Trigger {
        return gamepadTrigger("right_stick$deadzone") { abs(gamepad.right_stick_x) > deadzone }
    }


    /**
     * Allows client to perform an action when the gamepad's 'right_stick_y' button's state is mutated.
     * ```java
     * //e.g: (Triggers when abs(right_stick_y) > .5)
     * gamepad_x1.right_stick_y.onHigh(this::doSomething)
     */
    @JvmField
    val right_stick_y = right_stick_y(deadzone = .5)

    /**
     * Allows client to perform an action when the gamepad's 'right_stick_y' button's state is mutated.
     * ```java
     * //e.g: (Triggers when abs(right_stick_y) > deadzone)
     * gamepad_x1.right_stick_y.onHigh(this::doSomething)
     *```
     * @param deadzone The minimum value that the right_stick_y must be above to trigger the event.
     */
    fun right_stick_y(deadzone: Double): Trigger {
        return gamepadTrigger("right_stick$deadzone") { abs(gamepad.right_stick_y) > deadzone }
    }


    /**
     * Allows client to perform an action when the gamepad's 'left_trigger' button's state is mutated.
     * ```java
     * //e.g: (Triggers when abs(left_trigger) > .5)
     * gamepad_x1.left_trigger.onFall(this::doSomething)
     */
    @JvmField
    val left_trigger = left_trigger(deadzone = .5)

    /**
     * Allows client to perform an action when the gamepad's 'left_trigger' button's state is mutated.
     * ```java
     * //e.g: (Triggers when abs(left_trigger) > deadzone)
     * gamepad_x1.left_trigger.onFall(this::doSomething)
     *```
     * @param deadzone The minimum value that the left_trigger must be above to trigger the event.
     */
    fun left_trigger(deadzone: Double): Trigger {
        return gamepadTrigger("left_trigger$deadzone") { gamepad.left_trigger > deadzone }
    }


    /**
     * Allows client to perform an action when the gamepad's 'right_trigger' button's state is mutated.
     * ```java
     * //e.g: (Triggers when abs(right_trigger) > .5)
     * gamepad_x1.right_trigger.onLow(this::doSomething)
     */
    @JvmField
    val right_trigger = left_trigger(deadzone = .5)

    /**
     * Allows client to perform an action when the gamepad's 'right_trigger' button's state is mutated.
     * ```java
     * //e.g: (Triggers when abs(right_trigger) > deadzone)
     * gamepad_x1.right_trigger.onLow(this::doSomething)
     *```
     * @param deadzone The minimum value that the right_trigger must be above to trigger the event.
     */
    fun right_trigger(deadzone: Double): Trigger {
        return gamepadTrigger("right_trigger$deadzone") { gamepad.right_trigger > deadzone }
    }


    /**
     * Allows client to perform an action when any of the gamepad's joysticks' states are mutated.
     * ```java
     * //e.g: (Triggers when abs(any joystick) > .5)
     * gamepad_x1.joysticks.onRise(this::doSomething)
     */
    @JvmField
    val joysticks = joysticks(deadzone = .5)

    /**
     * Allows client to perform an action when any of the gamepad's joysticks' states are mutated.
     * ```java
     * //e.g: (Triggers when abs(any joystick) > deadzone)
     * gamepad_x1.joysticks.onRise(this::doSomething)
     *```
     * @param deadzone The minimum value that the joysticks must be above to trigger the event.
     */
    fun joysticks(deadzone: Double): Trigger {
        return gamepadTrigger("joysticks$deadzone") {
            abs(gamepad.left_stick_x)  > deadzone ||
            abs(gamepad.left_stick_y)  > deadzone ||
            abs(gamepad.right_stick_x) > deadzone ||
            abs(gamepad.right_stick_y) > deadzone
        }
    }

    /**
     * Returns an instantiated [Trigger] with the listener ID prefixed with this
     * GamepadEx2's ID.
     *
     * @param id The ID of the listener.
     * @param condition The condition the listener will be triggered on.
     *
     * @return The said [Trigger]
     */
    private fun gamepadTrigger(id: String, condition: Condition): Trigger {
        return Trigger("${gamepadEx2ID}$id", condition)
    }
}

