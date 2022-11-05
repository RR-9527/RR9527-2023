package org.firstinspires.ftc.teamcodekt.components.scheduler.taskchains

import org.firstinspires.ftc.teamcodekt.components.scheduler.listeners.Listener

interface CancellableTaskChain : TaskChain {
    fun cancelOn(listener: Listener)
}
