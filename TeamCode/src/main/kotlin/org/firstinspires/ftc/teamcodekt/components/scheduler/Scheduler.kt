package org.firstinspires.ftc.teamcodekt.components.scheduler

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.components.scheduler.Task

object Scheduler {
    private val commands = mutableListOf<ScheduledTask>()

    /**
     * Schedules a [Task] to run immediately.
     *
     * Kotlin usage examples:
     * ```
     * Scheduler.scheduleNow(::exampleTask)
     * Scheduler.scheduleNow { exampleTask() }
     * ```
     *
     * Java usage examples:
     * ```
     * Scheduler.scheduleNow(this::exampleTask)
     * Scheduler.scheduleNow(() -> exampleTask())
     * ```
     *
     * @param task The [ScheduledTask] to schedule
     * @return The scheduled task
     */
    @JvmStatic
    fun scheduleNow(task: Task) = scheduleNow(ScheduledTask(task))

    /**
     * Schedules a pre-created [ScheduledTask] task to run immediately.
     *
     * @param task The [ScheduledTask] to schedule
     * @return The scheduled task
     */
    fun scheduleNow(task: ScheduledTask) = with(task) {
        state = TaskState.STARTING
        commands.add(this)
        this
    }

    /**
     * Schedules a [Task] to run after a given [ScheduledTask] finishes operation.
     *
     * Kotlin usage examples:
     * ```
     * val task1 = Scheduler.scheduleNow(::task1)
     * Scheduler.scheduleAfter(task1, ::task2)
     * ```
     *
     * Java usage examples:
     * ```
     * ScheduledTask task1 = Scheduler.scheduleNow(this::task1);
     * Scheduler.scheduleAfter(task1, this::task2);
     * ```
     *
     * @param scheduledTask The [ScheduledTask] to run after
     * @param task The [Task] to schedule after the given [ScheduledTask]
     */
    @JvmStatic
    fun scheduleAfter(scheduledTask: ScheduledTask, task: Task): ScheduledTask {
        return scheduledTask.addObserver(task, TaskState.FINISHED)
    }

    /**
     * Schedules a [Task] to run as soon as a [ScheduledTask] starts operation.
     *
     * Kotlin usage examples:
     * ```
     * val task1 = Scheduler.scheduleNow(::task1)
     * Scheduler.scheduleDuring(task1, ::task2)
     * ```
     *
     * Java usage examples:
     * ```
     * ScheduledTask task1 = Scheduler.scheduleNow(this::task1);
     * Scheduler.scheduleDuring(task1, this::task2);
     * ```
     *
     * @param scheduledTask The [ScheduledTask] to run after
     * @param task The [Task] to schedule after the given [ScheduledTask]
     */
    @JvmStatic
    fun scheduleDuring(scheduledTask: ScheduledTask, task: Task): ScheduledTask {
        return scheduledTask.addObserver(task, TaskState.STARTING)
    }

    /**
     * Syntactic sugar for relative scheduling. __Note: this is Kotlin only.__
     *
     * Kotlin usage examples:
     * ```
     * val task1 = Scheduler.schedule(::task1) now please // The 'please' is important
     *
     * Scheduler.schedule(::task2) during task1
     *
     * Scheduler.schedule(::task3) after task1
     * ```
     *
     * @param task The [Task] to schedule
     * @return The same [Task] object
     */
    @JvmSynthetic
    fun schedule(task: Task): Task {
        return task
    }

    @JvmSynthetic
    infix fun Task.after(task: ScheduledTask): ScheduledTask {
        return scheduleAfter(task, this)
    }

    @JvmSynthetic
    infix fun Task.during(task: ScheduledTask): ScheduledTask {
        return scheduleDuring(task, this)
    }

    @JvmSynthetic
    infix fun Task.now(please: please): ScheduledTask {
        return scheduleNow(this)
    }

    /**
     * Removes a [ScheduledTask] from the scheduler. _Note: The given task must be the exact same
     * object as the task to remove._
     *
     *
     * @param scheduledTask The [ScheduledTask] to remove
     */
    fun unschedule(scheduledTask: ScheduledTask) {
        commands.removeAll { it === scheduledTask }
    }

    /**
     * Runs all scheduled tasks while the [LinearOpMode] is in the main 'loop' phase.
     *
     * @param opmode The [LinearOpMode] to run the tasks in
     */
    @JvmStatic
    fun run(opmode: LinearOpMode) {
        while (opmode.opModeIsActive() && !opmode.isStopRequested) {
            commands.forEach { scheduledTask -> scheduledTask.task(scheduledTask) }
        }
    }
}

