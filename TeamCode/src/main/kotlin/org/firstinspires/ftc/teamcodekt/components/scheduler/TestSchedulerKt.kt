@file:Suppress("IllegalIdentifier", "PrivatePropertyName")

package org.firstinspires.ftc.teamcodekt.components.scheduler

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.components.scheduler.Task
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotors
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler.schedule
import org.firstinspires.ftc.teamcodekt.util.LateInitVal

class TestScheduler : LinearOpMode() {
    private var driveMotors: DriveMotors by LateInitVal()

    override fun runOpMode() {
        waitForStart()

        please schedule `print 'a' 10 times` right now

        please schedule `print done` after `print 'a' 10 times`

        Scheduler.run(this)
    }

    private var print_a_10_times_counter = 0

    private val `print 'a' 10 times` = Task { scheduledTask ->
        println("a" + print_a_10_times_counter++)

        if (print_a_10_times_counter == 10) {
            scheduledTask.flagAsDone()
        }
    }

    private val `print done` = Task { scheduledTask ->
        println("done")
        scheduledTask.flagAsDone()
    }
}
