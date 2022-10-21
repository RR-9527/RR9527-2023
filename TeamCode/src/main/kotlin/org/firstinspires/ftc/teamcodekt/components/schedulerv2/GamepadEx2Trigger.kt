package org.firstinspires.ftc.teamcodekt.components.schedulerv2

interface GamepadEx2Trigger {
    fun onRise(action: Runnable): GamepadEx2Trigger
    fun onFall(action: Runnable): GamepadEx2Trigger
    fun onHigh(action: Runnable): GamepadEx2Trigger
    fun onLow(action: Runnable): GamepadEx2Trigger
}
