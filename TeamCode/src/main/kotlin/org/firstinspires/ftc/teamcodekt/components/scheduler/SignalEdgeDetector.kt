package org.firstinspires.ftc.teamcodekt.components.scheduler

import org.firstinspires.ftc.teamcodekt.util.Condition

/**
 * Evaluates a condition, and checks if the state of the condition is on the rising edge, falling
 * edge, is high, and/or is low.
 *
 * @param condition The condition to evaluate.
 *
 * @author KG
 *
 * @see Condition
 * @see SignalTrigger
 */
class SignalEdgeDetector(val condition: Condition) {
    /**
     * The current state of the condition
     */
    private var state = false

    /**
     * The previous state of the condition
     */
    private var lastState = false

    /**
     * Evaluates the condition and updates the state.
     */
    fun update() {
        lastState = state
        state = condition()
    }

    /**
     * Checks if the condition is on the rising edge.
     * @return True if the condition is on the rising edge, false otherwise.
     */
    fun risingEdge() = state && !lastState

    /**
     * Checks if the condition is on the falling edge.
     * @return True if the condition is on the falling edge, false otherwise.
     */
    fun fallingEdge() = !state && lastState

    /**
     * Checks if the condition is high.
     * @return True if the condition is true, false otherwise.
     */
    fun isHigh() = state

    /**
     * Checks if the condition is low.
     * @return True if the condition is false, false otherwise.
     */
    fun isLow() = !state
}
