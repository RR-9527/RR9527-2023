package org.firstinspires.ftc.teamcodekt.components.scheduler

import java.util.concurrent.TimeUnit

/**
 * A timer that can be used to schedule actions to be performed at a specific time.
 *
 * Java usage example:
 * ```java
 * @Override
 * public void runOpMode() throws InterruptedException {
 *     Timer timer = new Timer(5, TimeUnit.SECONDS);
 *
 *     // Runs the given Runnable while waiting for the timer to finish
 *     timer.whileWaiting(this::doSomething);
 *
 *     // Runs the given Runnable once after 5 seconds
 *     timer.onDone(this::doSomethingElse);
 *
 *     // Runs the given Runnable every tick after 5 seconds, until the timer is reset
 *     timer.whileDone(this::doYetAnotherThing);
 *
 *     waitForStart();
 *
 *     // Timer should be reset before usage. Prevents the timer from running immediately.
 *     // This will primarily be used in combination with a gamepad trigger.
 *     gamepadx1.a.onHigh(timer::reset);
 *
 *     // Starts the timer
 *     Scheduler.start(this);
 * }
 * ```
 *
 * @author KG
 *
 * @see Scheduler
 * @see Listener
 * @see GamepadEx2
 */
class Timer @JvmOverloads constructor(length: Long, unit: TimeUnit = TimeUnit.MILLISECONDS) {
    /**
     * The length of the timer in milliseconds.
     */
    private val length = unit.toMillis(length)

    /**
     * The time at which the timer started.
     */
    private var startTime = System.currentTimeMillis()

    /**
     * Determines whether the timer is running or simply waiting to start.
     */
    private var isPending = false

    /**
     * The trigger which keeps track of when the timer is done.
     */
    private var listener = Listener { !isPending && timeMs() >= this.length }

    /**
     * Schedules the given action to run while the timer is running and not yet finsihed.
     * @param action The action to run.
     * @return The timer instance.
     */
    fun whileWaiting(action: Runnable) = this.also { listener.whileLow(action) }

    /**
     * Schedules the given action to run once when the timer is finished.
     * @param action The action to run.
     * @return The timer instance.
     */
    fun onDone(action: Runnable) = this.also { listener.onRise(action) }

    /**
     * Resets the timer and sets it to not pending.
     */
    fun start() {
        isPending = false
        startTime = System.currentTimeMillis()
    }

    /**
     * Resets the timer.
     */
    fun reset() {
        startTime = System.currentTimeMillis()
    }

    fun finishPrematurely() {
        startTime = System.currentTimeMillis() + length
    }

    /**
     * Sets the timer to `pending`.
     */
    fun setPending() {
        isPending = true
    }

    /**
     * Gets the current time in milliseconds.
     * @return The current time in milliseconds.
     */
    fun timeMs(): Long {
        return if (isPending) -1 else System.currentTimeMillis() - startTime
    }
}
