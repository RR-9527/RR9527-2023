package org.firstinspires.ftc.teamcodekt.components.schedulerv2

import org.firstinspires.ftc.teamcode.components.scheduler.Action

class Listener(val id: String, condition: () -> Boolean) {
    private val actions = mutableMapOf<Action, () -> Boolean>()
    private val condition = SignalEdgeDetector(condition)

    fun subscribe(action: Action, on: SignalTriggers) = when (on) {
        SignalTriggers.RISING_EDGE  -> actions[action] = condition::risingEdge
        SignalTriggers.FALLING_EDGE -> actions[action] = condition::fallingEdge
        SignalTriggers.IS_HIGH      -> actions[action] = condition::isHigh
        SignalTriggers.IS_LOW       -> actions[action] = condition::isLow
    }

    fun update() {
        condition.update()
    }

    fun doActiveActions() {
        actions.forEach { (action, condition) ->
            if (condition()) action()
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Listener && other.id == id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
