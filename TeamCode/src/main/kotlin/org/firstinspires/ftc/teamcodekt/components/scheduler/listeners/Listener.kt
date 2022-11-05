@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package org.firstinspires.ftc.teamcodekt.components.scheduler.listeners

import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler
import org.firstinspires.ftc.teamcodekt.components.scheduler.SignalEdgeDetector
import org.firstinspires.ftc.teamcodekt.util.Condition

/**
 * A component that  performs the set of subscribed [actions][Runnable] when the state of the
 * [condition][Condition] matches the signal state that the action was
 * subscribed on (e.g. `on rising edge` or `while low`).
 *
 * The listener is updated on every tick of the [Scheduler] when hooked.
 *
 * _The listener is not hooked by default; it is only hooked once it has actions subscribed to it._
 * This means that it is safe to create excess unused [Listeners][Listener] without worrying about
 * performance burdens.
 *
 * Java raw usage example:
 * ```java
 * @Override
 * public void runOpMode() throws InterruptedException {
 *     Listener listener = new Listener(() -> someCondition == true);
 *
 *     // Runs when the condition changes from false to true
 *     listener.onRise(this::doSomething);
 *
 *     // Runs while the condition is true
 *     listener.whileHigh(this::doSomethingElse);
 *
 *     // Runs when the condition changes from true to false
 *     listener.onFall(this::doEomethingSlse);
 *
 *     // Runs while the condition is false
 *     listener.whileLow(this::doElseSomething);
 *
 *     // Must be called to start the main loop.
 *     Scheduler.start(this);
 * }
 * ```
 *
 * @param condition The condition that must be met for the action to be performed.
 *
 * @author KG
 *
 * @see Scheduler
 * @see GamepadEx2
 * @see Timer
 */
open class Listener(_condition: Condition) {

    constructor() : this({ false })

    var condition = _condition
        set(value) {
            conditionSED = SignalEdgeDetector(value)
            field = value
        }

    /**
     * The subscribed set of [actions][Runnable] that are performed when the given
     * condition's state matches the given [SignalTrigger][SignalTrigger].
     */
    private val actions = mutableMapOf<Runnable, Condition>()

    /**
     * A listener that evaluates the [condition][Condition] and checks whether the state of the
     * signal is [high][SignalTrigger.IS_HIGH], [low][SignalTrigger.IS_LOW],
     * [rising][SignalTrigger.RISING_EDGE], and/or [falling][SignalTrigger.FALLING_EDGE].
     */
    private var conditionSED = SignalEdgeDetector(condition)

    /**
     * Remembers whether or not this listener has been hooked.
     */
    private val isHooked = false

    /**
     * Schedules the given action to run when the trigger [condition][Condition] changes from false to true.
     * @param action The action to run.
     * @return This [Listener] instance.
     */
    fun onRise(action: Runnable) = this.also {
        hookIfNotHooked()
        actions[action] = conditionSED::risingEdge
    }

    /**
     * Schedules the given action to run while the trigger [condition][Condition] changes from true to false.
     * @param action The action to run.
     * @return This [Listener] instance.
     */
    fun onFall(action: Runnable) = this.also {
        hookIfNotHooked()
        actions[action] = conditionSED::fallingEdge
    }

    /**
     * Schedules the given action to run when the trigger [condition][Condition] is true.
     * @param action The action to run.
     * @return This [Listener] instance.
     */
    fun whileHigh(action: Runnable) = this.also {
        hookIfNotHooked()
        actions[action] = conditionSED::isHigh
    }

    /**
     * Schedules the given action to run while the trigger [condition][Condition] is false.
     * @param action The action to run.
     * @return This [Listener] instance.
     */
    fun whileLow(action: Runnable) = this.also {
        hookIfNotHooked()
        actions[action] = conditionSED::isLow
    }

    /**
     * Hooks this listener to the [Scheduler] if it has not already been hooked.
     */
    private fun hookIfNotHooked() {
        if (!isHooked) {
            Scheduler.hookListener(this)
        }
    }

    /**
     * Updates the state of the class's [SignalEdgeDetector]
     */
    fun update() {
        conditionSED.update()
    }

    /**
     * Performs the actions who's conditions evaluate to true.
     */
    fun doActiveActions() {
        actions.forEach { (action, condition) ->
            if (condition()) action.run()
        }
    }

    operator fun invoke() = condition()

    // -----------------------------------------------------------------
    // Listener builders
    // -----------------------------------------------------------------

    fun and(other: Condition) = Listener { condition() && other() }

    fun or(other: Condition) = Listener { condition() || other() }

    fun not() = Listener { !condition() }

    fun xor(other: Condition) = Listener { condition() xor other() }

    fun nand(other: Condition) = Listener { !(condition() && other()) }

    fun nor(other: Condition) = Listener { !(condition() || other()) }

    fun xnor(other: Condition) = Listener { condition() == other() }


    fun and(other: Listener) = Listener { condition() && other.condition() }

    fun or(other: Listener) = Listener { condition() || other.condition() }

    fun xor(other: Listener) = Listener { condition() xor other.condition() }

    fun nand(other: Listener) = Listener { !(condition() && other.condition()) }

    fun nor(other: Listener) = Listener { !(condition() || other.condition()) }

    fun xnor(other: Listener) = Listener { condition() == other.condition() }
}
