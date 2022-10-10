package org.firstinspires.ftc.teamcode.opmodes.scheduler

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import kotlin.properties.Delegates.observable

typealias Task = (ScheduledTask) -> Unit

enum class State { PENDING, STARTING, IN_PROGRESS, FINISHED }

class ScheduledTask(val task: Task) {
    private val observers = mutableMapOf<Task, State>()

    var state: State by observable(State.PENDING) { _, _, newState ->
        observers
            .filterValues { targetState -> targetState == newState }
            .forEach { observer -> Scheduler.schedule { observer.key } }

        if (newState == State.FINISHED) {
            observers.clear()
            Scheduler.unschedule(this)
        }
    }

    fun addObserver(task: Task, state: State) {
        observers[task] = state
    }
}

object Scheduler {
    private val commands = mutableListOf<ScheduledTask>()

    fun schedule(task: Task): ScheduledTask {
        var taskToAdd = ScheduledTask(task).apply { state = State.STARTING }
        commands.add(taskToAdd)
        return taskToAdd
    }

    fun scheduleAfter(scheduledTask: ScheduledTask, task: Task) {
        scheduledTask.addObserver(task, State.FINISHED)
    }

    fun scheduleDuring(scheduledTask: ScheduledTask, task: Task) {
        scheduledTask.addObserver(task, State.STARTING)
    }

    fun unschedule(scheduledTask: ScheduledTask) {
        commands.removeAll { it === scheduledTask }
    }

    fun run(opmode: LinearOpMode) {
        while (opmode.opModeIsActive() && !opmode.isStopRequested) {
            commands.forEach { sheduledTask -> sheduledTask.task(sheduledTask) }
        }
    }
}

