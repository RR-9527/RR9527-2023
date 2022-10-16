package org.firstinspires.ftc.teamcodekt.components.scheduler

class Trigger(private val trigger: () -> Boolean) {
    init {
        register(this)
    }

    private var state: Boolean = false
        set(value) {
            previousState = state
            field = value
        }

    private var previousState = false

    val isTriggered
        get() = state

    @JvmField
    val risingEdge = { state && !previousState }

    @JvmField
    val fallingEdge = { !state && previousState }

    private fun update() {
        state = trigger()
    }

    companion object {
        private val triggers = mutableSetOf<Trigger>()

        @JvmStatic
        fun register(trigger: Trigger) {
            triggers.add(trigger)
        }

        @JvmStatic
        fun updateAll() {
            triggers.forEach { it.update() }
        }
    }
}