@file:Suppress("PrivatePropertyName", "LocalVariableName")

package org.firstinspires.ftc.teamcodekt.components.scheduler

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.components.scheduler.Task
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotors
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler.schedule
import org.firstinspires.ftc.teamcodekt.components.scheduler.keywords.please
import org.firstinspires.ftc.teamcodekt.util.LateInitVal

class TestScheduler : LinearOpMode() {
    private var driveMotors: DriveMotors by LateInitVal()

    override fun runOpMode() {
        val gamepad1_a = Trigger { gamepad1.a }

        please schedule print_a_10_times on gamepad1_a.risingEdge

        please schedule print_done after print_a_10_times

        Scheduler.run(this)
    }

    private var print_a_10_times_counter = 0

    private val print_a_10_times = Task { scheduledTask ->
        println("a" + print_a_10_times_counter++)

        if (print_a_10_times_counter == 10) {
            scheduledTask.flagAsDone()
        }
    }

    private val print_done = Task { scheduledTask ->
        println("done")
        print_a_10_times_counter = 0
        scheduledTask.flagAsDone()
    }
}

fun main() {
    TestScheduler().runOpMode()
}
























