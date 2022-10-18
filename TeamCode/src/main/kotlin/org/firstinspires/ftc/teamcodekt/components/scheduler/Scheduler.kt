package org.firstinspires.ftc.teamcodekt.components.scheduler

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.components.scheduler.Task
import org.firstinspires.ftc.teamcodekt.components.scheduler.keywords.please

//TODO: Make this shitty code less shitty holy shit

/**
 * A utility class for scheduling tasks to run relative to one another in a [LinearOpMode].
 *
 * There are three main ways to use this class:
 *
 * Kotlin:
 * ```kotlin
 * please schedule task1 right now
 * please schedule task2 after task1
 * please schedule task3 during task2
 *
 * please schedule task4 on Trigger { gamepad1.a }
 *
 * Scheduler.run(this);
 * ```
 *
 * Java:
 * ```java
 * // Note: you can `import static` schedule to prevent prefixing with `Scheduler.` every time
 * schedule(task1).now();
 * schedule(task2).after(task1);
 * schedule(task3).during(task2);
 *
 * schedule(task4).when(new Trigger(() -> gamepad1.a));
 * //or
 * schedule(task4).on(new Trigger(() -> gamepad1.a));
 *
 * Scheduler.run(this);
 * ```
 *
 * Java2:
 * ```java
 * // Note: you can `import static` schedule to prevent prefixing with `Scheduler.` every time
 * ScheduledTask task1 = schedule(this::task1).now();
 * ScheduledTask task2 = schedule(this::task2).after(task1);
 * ScheduledTask task3 = schedule(this::task3).during(task2);
 *
 * ScheduledTask task4 = schedule(this::task4).when(new Trigger(() -> gamepad1.a));
 *
 * Scheduler.run(this);
 * ```
 *
 * __IMPORTANT NOTE: to make use of relative scheduling in the first two methods,__
 * __methods MUST be declared as lambdas bound to variables__
 * ```java
 * // This will work
 * public static void main(String[] args) {
 *     Scheduler.schedule(task1).now();
 *     Scheduler.schedule(task2).after(task1);
 * }
 *
 * private final Task task1 = (scheduledTask) -> {
 *    // do something
 *    scheduledTask.flagAsDone();
 * }
 *
 * private final Task task2 = (scheduledTask) -> {
 *    // do something
 *    scheduledTask.flagAsDone();
 * }
 *
 * // ----------------------------
 *
 * // This will NOT work
 * public static void main(String[] args) {
 *     Scheduler.schedule(this::task1).now();
 *     Scheduler.schedule(this::task2).after(this::task1);
 * }
 *
 * private void task1(ScheduledTask scheduledTask) {
 *    // do something
 *    scheduledTask.flagAsDone();
 * }
 *
 * private void task2(ScheduledTask scheduledTask) {
 *    // do something
 *    scheduledTask.flagAsDone();
 * }
 *
 * // The above is because the `this::task1` syntax is a method reference, and the method
 * // references are not cached nor equivalent, meaning `this::task1 == this::task1` is
 * // false. Declaring them as lambdas bound to variables ensures that the methods are
 * // equivalent as they are physically the same reference.
 * ```
 *
 * !!!_The above is not required when making use of the third syntax._
 *
 * @author KG
 *
 * @see [Task]
 * @see [ScheduledTask]
 */
object Scheduler {
    private val scheduledTasks = mutableSetOf<ScheduledTask>()

    /**
     * Syntactic sugar for relative scheduling.
     *
     * Java usage examples:
     * ```java
     * Scheduler.schedule(task1).now()
     * Scheduler.schedule(task2).after(task1)
     * Scheduler.schedule(task3).during(task2)
     * ```
     *
     * __NOTE: to make use of relative scheduling, you must create the tasks as follows:__
     * ```java
     * // Must be declared as lambda expressions bound to a variable so that all method
     * // references are the same object
     * private final task1 = (scheduledTask) -> {
     *     // task logic
     *     scheduledTask.setState(TaskState.FINISHED);
     * }
     * ```
     *
     * @param task The [Task] to schedule
     * @return The same [Task] object
     */
    @JvmStatic
    fun schedule(task: Task) = ScheduledTask(task)

    /**
     * Syntactic sugar for relative scheduling. __Note: this is Kotlin only.__
     *
     * Kotlin usage examples:
     * ```
     * please schedule ::task1 right  now
     * please schedule ::task2 after  ::task1
     * please schedule ::task3 during ::task2
     * ```
     * __NOTE: to make use of relative scheduling, you must create the tasks as follows:__
     * ```
     * // Must be declared as lambda expressions bound to a variable so that all method
     * // references are the same object
     * private val task1 = { scheduledTask ->
     *     // task logic
     *     scheduledTask.state = TaskState.FINISHED
     * }
     * ```
     *
     * @param task The [Task] to schedule
     * @return The same [Task] object
     */
    @JvmSynthetic
    infix fun please.schedule(task: Task) = ScheduledTask(task)

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
    fun scheduleNow(task: ScheduledTask) = with(task) {
        state = TaskState.RUNNING
        scheduledTasks.add(this)
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
    fun scheduleAfter(scheduledTask: ScheduledTask, newTask: ScheduledTask): ScheduledTask? {
        scheduledTasks.add(newTask)

        scheduledTasks.find { it == scheduledTask }?.let {
            newTask.predicateToScheduleOn = { it.state == TaskState.FINISHED }
            return newTask
        }
        return null
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
    fun scheduleDuring(scheduledTask: ScheduledTask, newTask: ScheduledTask): ScheduledTask? {
        scheduledTasks.add(newTask)

        scheduledTasks.find { it == scheduledTask }?.let {
            newTask.predicateToScheduleOn = { it.state == TaskState.RUNNING }
            return newTask
        }
        return null
    }

    /**
     * Schedules a [Task] to run as soon as the given predicate is true.
     *
     * Kotlin usage example:
     * ```kotlin
     * val gamepad1_a = Trigger { gamepad1.a }
     *
     *
     * please schedule task2 on gamepad1_a.risingEdge
     *
     * please schedule task3 on Trigger { gamepad1.right_stick_x > .3 }.fallingEdge
     * ```
     *
     * Java usage example:
     * ```java
     * Trigger gamepad1_a = new Trigger(() -> gamepad.a);
     *
     * Scheduler.schedule(task2).on(gamepad1_a.risingEdge);
     * //or
     * Scheduler.schedule(task2).when(gamepad1_a.risingEdge);
     *
     * Scheduler.schedule(task3)
     *     .on(new Trigger(() -> gamepad1.right_stick_x > .3).fallingEdge);
     * ```
     *
     * @param scheduledTask The [ScheduledTask] to run after
     * @param task The [Task] to schedule after the given [ScheduledTask]
     */
    fun scheduleOn(newTask: ScheduledTask, predicate: () -> Boolean): ScheduledTask {
        scheduledTasks.add(newTask)
        newTask.predicateToScheduleOn = predicate
        return newTask
    }

    /**
     * Runs all scheduled tasks while the [LinearOpMode] is in the main 'loop' phase.
     *
     * @param opmode The [LinearOpMode] to run the tasks in
     */
    @JvmStatic
    fun run(opmode: LinearOpMode) {
        while (opmode.opModeIsActive() && !opmode.isStopRequested) {
            scheduledTasks.forEach {
                if (it.state == TaskState.RUNNING)
                    it.task(it)

                else if (it.predicateToScheduleOn?.invoke() == true)
                    it.state = TaskState.RUNNING
            }

            scheduledTasks.removeIf { it.state == TaskState.FINISHED }

            Trigger.updateAll()
        }
    }
}
