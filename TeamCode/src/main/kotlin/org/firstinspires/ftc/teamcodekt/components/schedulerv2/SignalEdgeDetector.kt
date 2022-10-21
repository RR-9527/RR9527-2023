package org.firstinspires.ftc.teamcodekt.components.schedulerv2

import org.firstinspires.ftc.teamcodekt.util.Condition

class SignalEdgeDetector(val condition: Condition) {
    private var state = false
    private var lastState = false

    fun update() {
        lastState = state
        state = condition()
    }

    fun risingEdge() = state && !lastState
    fun fallingEdge() = !state && lastState
    fun isHigh() = state
    fun isLow() = !state
}

