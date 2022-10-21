package org.firstinspires.ftc.teamcodekt.components.schedulerv2

import org.firstinspires.ftc.teamcodekt.util.Condition

class Listener(val id: String, condition: Condition) {
    private val actions = mutableMapOf<Runnable, Condition>()
    private val conditionSED = SignalEdgeDetector(condition)

    fun subscribe(action: Runnable, on: SignalTriggers) = when (on) {
        SignalTriggers.RISING_EDGE  -> actions[action] = conditionSED::risingEdge
        SignalTriggers.FALLING_EDGE -> actions[action] = conditionSED::fallingEdge
        SignalTriggers.IS_HIGH      -> actions[action] = conditionSED::isHigh
        SignalTriggers.IS_LOW       -> actions[action] = conditionSED::isLow
    }

    fun update() {
        conditionSED.update()
    }

    fun doActiveActions() {
        actions.forEach { (action, condition) ->
            if (condition()) action.run()
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Listener && other.id == id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
