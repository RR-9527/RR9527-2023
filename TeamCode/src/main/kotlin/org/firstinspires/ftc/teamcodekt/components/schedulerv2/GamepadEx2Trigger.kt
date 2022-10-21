package org.firstinspires.ftc.teamcodekt.components.schedulerv2

import org.firstinspires.ftc.teamcodekt.util.Condition

class GamepadEx2Trigger(private val id: String, private val condition: Condition) {
    fun onRise(action: Runnable) = this.also {
        Scheduler.getOrCreateListener(id, condition).subscribe(action, on = SignalTriggers.RISING_EDGE)
    }

    fun onFall(action: Runnable) = this.also {
        Scheduler.getOrCreateListener(id, condition).subscribe(action, on = SignalTriggers.FALLING_EDGE)
    }

    fun onHigh(action: Runnable) = this.also {
        Scheduler.getOrCreateListener(id, condition).subscribe(action, on = SignalTriggers.IS_HIGH)
    }

    fun onLow(action: Runnable) = this.also {
        Scheduler.getOrCreateListener(id, condition).subscribe(action, on = SignalTriggers.IS_LOW)
    }
}