package org.firstinspires.ftc.teamcodekt.components.schedulerv2

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

object Scheduler {
    private val listeners = mutableSetOf<Listener>()

    @JvmStatic
    @JvmOverloads
    fun start(opmode: LinearOpMode, block: Runnable? = null) {
        while (opmode.opModeIsActive() && !opmode.isStopRequested) {
            tick()
            block?.run()
        }
    }

    private fun tick() = listeners.forEach {
        it.update()
        it.doActiveActions()
    }

    @JvmStatic
    fun getOrCreateListener(id: String, condition: () -> Boolean): Listener {
        return getListener(id) ?: addListener(Listener(id, condition))
    }

    private fun addListener(listener: Listener): Listener {
        return listener.also { listeners.add(it) }
    }

    private fun getListener(id: String): Listener? {
        return listeners.find { it.id == id }
    }
}