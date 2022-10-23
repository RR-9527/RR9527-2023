package org.firstinspires.ftc.teamcodekt.components.schedulerv2

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcodekt.util.Condition

/**
 * A component that simplifies the process of scheduling actions to be performed at a
 * specific time or condition, for use in a [LinearOpMode]. It seeks to transform teleop code
 * into a declarative haven, where the robot's actions are defined in a set of [actions][Runnable]
 * bound to a [condition][Condition] on which to perform it.
 *
 * Usage of this component groups all mutation of state into once central location, making it
 * infinitely easier to debug and maintain.
 *
 * For example:
 * ```java
 * while (opmode.opModeIsActive() && !opmode.isStopRequested) {
 *     openClaw();  // ?? When is this used and how do I use it ??
 * }
 *
 * // 300 lines later...
 *
 * private boolean gamepad1aWasFalseBefore = //...;
 *
 * private void checkIfShouldDoSomething() {
 *    if (gamepad1.a && gamepad1aWasFalseBefore) {
 *        claw.open();
 *    }
 *    gamepad1aWasFalseBefore = //...
 * } // ugh imperative hell
 *
 * // --- VS: ---
 *
 * gamepadx1.a.onRise(claw::open); // So clean and cool wow the person who made
 * Scheduler.start(this);  // this was probably so smart and a genius I'm so jealous!!
 * ```
 *
 * The performance impact of this component is minimal, with [Listeners][Listener] being lazily
 * hooked only when required, and each tick barely encumbering the stack.
 *
 * All tasks are guaranteed to run in the order that they are scheduled. The methods in the `block`
 * parameter of [start] and [time] are called after the scheduled [actions][Runnable]
 *
 * Java usage example:
 * ```java
 * @Override
 * public void runOpMode() throws InterruptedException {
 *
 *     // Usage of scheduler through a more convenient method
 *     GamepadEx2 gamepadx1 = new GamepadEx2(gamepad1);
 *     gamepadx1.a.onHigh(this::doSomething);
 *
 *     // Usage of scheduler through the raw API
 *     Scheduler.getOrCreateListener("someCondition", someCondition == true)
 *         .onRise(this::doSomethingElse)
 *         .onFall(this::doYetAnotherThing);
 *
 *     // Runs the code while the OpMode is active.
 *     Scheduler.start(this, () -> {
 *         // Optional block of code to be run after the above listeners
 *         // This parameter may be omitted if unnecessary.
 *         updateSomething();
 *         doSomethingElse();
 *         blahBlahBlah();
 *     });
 * }
 * ```
 *
 * @author KG
 *
 * @see Listener
 * @see Condition
 * @see GamepadEx2
 * @see Timer
 */
object Scheduler {
    /**
     * The [Listeners][Listener] subscribed to this [Scheduler]. Updated on every tick.
     */
    private val listeners = mutableSetOf<Listener>()

    /**
     * Starts the [Scheduler], and runs the program in the given [block] until the [LinearOpMode]
     * is no longer active.
     * Java usage example:
     * ```java
     * @Override
     * public void runOpMode() throws InterruptedException {
     *    // Instantiate listeners...
     *
     *    waitForStart();
     *
     *    Scheduler.start(this, () -> {
     *        updateSomething();
     *        updateTelemetry(telemetry);
     *    });
     * }
     *
     * //or
     *
     * @Override
     * public void runOpMode() throws InterruptedException {
     *    // Instantiate listeners...
     *
     *    waitForStart();
     *
     *    Scheduler.start(this);
     * }
     * ```
     * @param opmode The [LinearOpMode] to run the [Scheduler] in.
     * @param block An optional block of code to run every tick, after the listeners have ran.
     */
    @JvmStatic
    @JvmOverloads
    fun start(opmode: LinearOpMode, block: Runnable? = null) {
        while (opmode.opModeIsActive() && !opmode.isStopRequested) {
            tick()
            block?.run()
        }
    }

    /**
     * Starts the [Scheduler], and runs the program in the given [block] until the [LinearOpMode]
     * is no longer active. The loop time is then calculated, and send to the [Telemetry] object.
     *
     * __Note 1:__ this method is for development and optimization purposes only, _and should not
     * be used in final code_.
     *
     * __Note 2:__ This method automatically calls [Telemetry.update] after the loop time is
     * calculated; _you should not call [Telemetry.update] yourself when using this_.
     *
     * Java usage example:
     * ```java
     * @Override
     * public void runOpMode() throws InterruptedException {
     *    // Instantiate listeners...
     *
     *    waitForStart();
     *
     *    Scheduler.time(this, telemetry, () -> {
     *        updateSomething();
     *        updateTelemetry(telemetry);
     *    });
     * }
     *
     * //or
     *
     * @Override
     * public void runOpMode() throws InterruptedException {
     *    // Instantiate listeners...
     *
     *    waitForStart();
     *
     *    Scheduler.this(this, telemetry);
     * }
     * ```
     * @param opmode The [LinearOpMode] to run the [Scheduler] in.
     * @param block An optional block of code to run every tick, after the listeners have ran.
     */
    @JvmStatic
    @JvmOverloads
    fun time(opmode: LinearOpMode, telemetry: Telemetry, block: Runnable? = null) {
        while (opmode.opModeIsActive() && !opmode.isStopRequested) {
            val startTime = System.currentTimeMillis()

            tick()
            block?.run()

            val endTime = System.currentTimeMillis()
            telemetry.addData("Loop time (ms)", endTime - startTime)
            telemetry.update()
        }
    }

    /**
     * Updates the listeners and runs their actions if their conditions are met.
     */
    private fun tick() = listeners.forEach { listener ->
        listener.update()
        listener.doActiveActions()
    }

    /**
     * Gets the [Listener] with the given [id], or creates and registers one if it doesn't exist.
     * @param id The unique ID of the [Listener].
     * @param condition The [Condition] to check.
     * @return The [Listener] with the given [id] and [condition].
     */
    @JvmStatic
    fun hookListener(listener: Listener) = listener.also {
        listeners += listener
    }
}
