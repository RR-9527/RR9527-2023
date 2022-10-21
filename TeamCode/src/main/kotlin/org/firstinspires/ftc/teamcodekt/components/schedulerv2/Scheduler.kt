package org.firstinspires.ftc.teamcodekt.components.schedulerv2

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcodekt.util.Condition

object Scheduler {
    private val listeners = mutableMapOf<String, Listener>()

    @JvmStatic
    @JvmOverloads
    fun start(opmode: LinearOpMode, block: Runnable? = null) {
        while (opmode.opModeIsActive() && !opmode.isStopRequested) {
            tick()
            block?.run()
        }
    }

    @JvmStatic
    @JvmOverloads
    fun time(opmode: LinearOpMode, telemetry: Telemetry, block: Runnable? = null) {
        while (opmode.opModeIsActive() && !opmode.isStopRequested) {
            val startTime = System.currentTimeMillis()

            tick()
            block?.run()

            val endTime = System.currentTimeMillis()
            telemetry.addData("Loop time (ms)", endTime - startTime)
            telemetry.update()
        }
    }

    private fun tick() = listeners.forEach { (_, listener) ->
        listener.update()
        listener.doActiveActions()
    }

    @JvmStatic
    fun getOrCreateListener(id: String, condition: Condition): Listener {
        return listeners.getOrPut(id) { Listener(id, condition) }
    }
}
