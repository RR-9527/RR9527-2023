package org.firstinspires.ftc.teamcodekt.components.scheduler

interface CancellableTaskChain : TaskChain {
    fun cancelOn(listener: Listener)
}
