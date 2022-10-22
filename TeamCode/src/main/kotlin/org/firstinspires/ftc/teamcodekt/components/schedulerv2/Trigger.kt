package org.firstinspires.ftc.teamcodekt.components.schedulerv2

import org.firstinspires.ftc.teamcodekt.util.Condition

/**
 * Represents a trigger that can schedule a listener to listen to and run the given tasks when the
 * given [condition][Condition] is met.
 *
 * @param id The ID of the trigger.
 * @param condition The condition that must be met for the trigger to execute.
 *
 * @author KG
 *
 * @see SignalTrigger
 * @see Scheduler
 * @see Listener
 */
class Trigger(private val id: String, private val condition: Condition) {
    /**
     * Schedules the given action to run when the trigger [condition][Condition] changes from false to true.
     * @param action The action to run.
     * @return This trigger instance.
     */
    fun onRise(action: Runnable) = this.also {
        Scheduler.getOrCreateListener(id, condition).subscribe(action, on = SignalTrigger.RISING_EDGE)
    }

    /**
     * Schedules the given action to run while the trigger [condition][Condition] changes from true to false.
     * @param action The action to run.
     * @return This trigger instance.
     */
    fun onFall(action: Runnable) = this.also {
        Scheduler.getOrCreateListener(id, condition).subscribe(action, on = SignalTrigger.FALLING_EDGE)
    }

    /**
     * Schedules the given action to run when the trigger [condition][Condition] is true.
     * @param action The action to run.
     * @return This trigger instance.
     */
    fun onHigh(action: Runnable) = this.also {
        Scheduler.getOrCreateListener(id, condition).subscribe(action, on = SignalTrigger.IS_HIGH)
    }

    /**
     * Schedules the given action to run while the trigger [condition][Condition] is false.
     * @param action The action to run.
     * @return This trigger instance.
     */
    fun onLow(action: Runnable) = this.also {
        Scheduler.getOrCreateListener(id, condition).subscribe(action, on = SignalTrigger.IS_LOW)
    }
}