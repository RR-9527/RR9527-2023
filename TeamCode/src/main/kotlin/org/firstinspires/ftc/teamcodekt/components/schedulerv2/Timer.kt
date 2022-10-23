package org.firstinspires.ftc.teamcodekt.components.schedulerv2

import java.util.concurrent.TimeUnit

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
     * The trigger which keeps track of when the timer is done.
     */
    private var listener = Listener { timeMs() >= this.length }

    /**
     * Schedules the given action to run while the timer is running and not yet finsihed.
     * @param action The action to run.
     * @return The trigger instance.
     */
    fun whileWaiting(action: Runnable) = listener.whileLow(action)

    /**
     * Schedules the given action to run while the timer is done.
     * @param action The action to run.
     * @return The trigger instance.
     */
    fun whileDone(action: Runnable) = listener.whileHigh(action)

    /**
     * Schedules the given action to run once when the timer is finished.
     * @param action The action to run.
     * @return The trigger instance.
     */
    fun onDone(action: Runnable) = listener.onRise(action)

    /**
     * Resets the timer.
     */
    fun reset() {
        startTime = System.currentTimeMillis()
    }

    /**
     * Gets the current time in milliseconds.
     * @return The current time in milliseconds.
     */
    fun timeMs(): Long {
        return System.currentTimeMillis() - startTime
    }
}
