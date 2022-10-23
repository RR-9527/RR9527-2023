package org.firstinspires.ftc.teamcodekt.components.schedulerv2

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcodekt.util.Condition

/**
 * Schedules tasks to run when their given conditions are met in a [LinearOpMode].
 *
 * __NOTE:__ While the methods in the `block` parameter of [start] and [time] are called in the
 * order they were written, and are guaranteed to be called after the scheduled [actions][Runnable],
 * ___the order in which the scheduled actions are called is not guaranteed___.
 *
 * Java usage example:
 * ```java
 * @Override
 * public void runOpMode() throws InterruptedException {
 *     GamepadEx2 gamepadx1 = new GamepadEx2(gamepad1);
 *
 *     gamepadx1.a.onHigh(this::doSomething);
 *
 *     Scheduler.getOrCreateListener("someCondition", someCondition == true)
 *         .onRise(this::doSomethingElse)
 *         .onFall(this::doYetAnotherThing);
 *
 *     Scheduler.start(this, () -> {
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
 */
object Scheduler {
    /**
     * The [Listeners][Listener] subscribed to this [Scheduler]. Updated on every tick.
     */
    private val listeners = mutableMapOf<String, Listener>()

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
    private fun tick() = listeners.forEach { (_, listener) ->
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
    fun getOrCreateListener(id: String, condition: Condition): Listener {
        return listeners.getOrPut(id) { Listener(id, condition) }
    }
}
