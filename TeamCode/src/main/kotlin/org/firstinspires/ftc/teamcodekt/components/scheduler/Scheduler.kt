package org.firstinspires.ftc.teamcodekt.components.scheduler

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.components.scheduler.Task

object Scheduler {
    private val commands = mutableSetOf<ScheduledTask>()

    /**
     * Schedules a [Task] to run immediately.
     *
     * Kotlin usage examples:
     * ```
     * Scheduler.scheduleNow(::exampleTask1)
     * Scheduler.scheduleNow { exampleTask2(it, 100) }
     * ```
     *
     * Java usage examples:
     * ```
     * Scheduler.scheduleNow(this::exampleTask1)
     * Scheduler.scheduleNow(scheduledTask -> exampleTask2(scheduledTask, 100))
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
        commands.add(this)
        state = TaskState.RUNNING
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
    fun scheduleAfter(scheduledTask: ScheduledTask, newTask: Task) = with(ScheduledTask(newTask)) {
        commands.add(this)
        commands.find { it == scheduledTask }?.addObserver(this, TaskState.FINISHED)
    }

    @JvmStatic
    fun scheduleAfter(task: Task, newTask: Task) = scheduleAfter(ScheduledTask(task), newTask)

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
    fun scheduleDuring(scheduledTask: ScheduledTask, newTask: Task) = with(ScheduledTask(newTask)) {
        commands.add(this)
        commands.find { it == scheduledTask }?.addObserver(this, TaskState.RUNNING)
    }

    @JvmStatic
    fun scheduleDuring(task: Task, newTask: Task) = scheduleAfter(ScheduledTask(task), newTask)

    /**
     * Syntactic sugar for relative scheduling. __Note: this is Kotlin only.__
     *
     * Kotlin usage examples:
     * ```
     * please schedule ::task1 right  now
     * please schedule ::task2 after  ::task1
     * please schedule ::task3 during ::task2
     * ```
     *
     * @param task The [Task] to schedule
     * @return The same [Task] object
     */
    @JvmSynthetic
    infix fun please.schedule(task: Task): Task {
        return task
    }

    @JvmSynthetic
    infix fun Task.after(task: Task): ScheduledTask? {
        return scheduleAfter(task, this)
    }

    @JvmSynthetic
    infix fun Task.during(task: Task): ScheduledTask? {
        return scheduleDuring(task, this)
    }

    @JvmSynthetic
    infix fun Task.right(now: now): ScheduledTask {
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
            commands.forEach { if (it.state == TaskState.RUNNING) it.task(it) }
        }
    }
}
