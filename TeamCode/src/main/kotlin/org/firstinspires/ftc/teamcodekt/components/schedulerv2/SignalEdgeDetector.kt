package org.firstinspires.ftc.teamcodekt.components.schedulerv2

class SignalEdgeDetector(val condition: () -> Boolean) {
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

