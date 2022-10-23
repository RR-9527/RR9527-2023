package org.firstinspires.ftc.teamcodekt.components.schedulerv2

import org.firstinspires.ftc.teamcodekt.util.Condition

/**
 * A component that  performs the set of subscribed [actions][Runnable] when the state of the
 * [condition][Condition] matches the [signal state][SignalTrigger] that the action was
 * subscribed with.
 *
 * @param id The id of the listener.
 * @param condition The condition that must be met for the action to be performed.
 */
class Listener(val id: String, condition: Condition) {
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
    private val conditionSED = SignalEdgeDetector(condition)

    /**
     * Subscribes the given [action][Runnable] to the given [SignalTrigger].
     *
     * @param action The action to be performed when the condition's state matches the given
     * signal trigger.
     * @param on The [SignalTrigger] that the condition's state must match in order
     * for the action to be performed.
     */
    fun subscribe(action: Runnable, on: SignalTrigger) = when (on) {
        SignalTrigger.RISING_EDGE  -> actions[action] = conditionSED::risingEdge
        SignalTrigger.FALLING_EDGE -> actions[action] = conditionSED::fallingEdge
        SignalTrigger.IS_HIGH      -> actions[action] = conditionSED::isHigh
        SignalTrigger.IS_LOW       -> actions[action] = conditionSED::isLow
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

    /**
     * Equals method for the [Listener] class. Only takes into consideration the [id] of
     * the [Listener].
     * @param other The other [Listener] to compare to.
     * @return Whether the two listeners are equal.
     */
    override fun equals(other: Any?): Boolean {
        return other is Listener && other.id == id
    }

    /**
     * Hashcode method for the [Listener] class. Only takes into consideration the [id] of
     * the [Listener].
     * @return The hashcode of the [Listener].
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}
