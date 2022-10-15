package org.firstinspires.ftc.teamcodekt.components.scheduler

import org.firstinspires.ftc.teamcode.components.scheduler.Task
import kotlin.properties.Delegates.observable

/**
 * Represents a [Task] that can be scheduled to run on the [Scheduler] at a given point in time. This
 * task will run until the task concludes itself finished from within the task itself.
 *
 *
 *@param task The [Task] to be scheduled
 *
 * @author KG
 *
 * @see Scheduler
 * @see Task
 */
class ScheduledTask(val task: Task) {
    /**
     * The map of [Tasks][Task] that are dependent on this task.
     */
    private val observers = mutableMapOf<ScheduledTask, TaskState>()

    /**
     * Okay... so this will be a tricky one to explain
     *
     * The variable itself represents the state of the corresponding [Task] via a [TaskState] enum.
     * Every time the [TaskState] is changed, the [observers] are checked to see if any of the
     * [Tasks][Task] are dependent on the [TaskState] of this [ScheduledTask]. If so, the [Task] is
     * scheduled to run on the [Scheduler].
     *
     * Kotlin demonstration:
     * ```kotlin
     * please schedule liftLiftTo20 right now
     * please schedule openClaw after liftLiftTo20
     *
     * please schedule lowerLiftTo10 after liftLiftTo20
     * please schedule closeClaw during lowerLiftTo10
     *
     * //...
     *
     * private val liftLiftTo20 = { scheduledTask: ScheduledTask ->
     *     // Lift logic here
     *
     *     if (/* Lift is at height */) {
     *        scheduledTask.flagAsDone()
     *    }
     * }
     *
     * private val openClaw = { scheduledTask: ScheduledTask ->
     *     // Claw logic here
     *
     *     if (/* Claw is open */) {
     *         scheduledTask.flagAsDone()
     *     }
     * }
     *
     * //...
     *
     * // So let's see what's happened there:
     *
     * // When liftLift is first created, it's state is set to TaskState.PENDING, and then is
     * // immediately set to TaskState.STARTING, as it is immediately scheduled.
     *
     * // In the meantime, none of the other tasks are running as they are all dependent on
     * // liftLift to finish first. However, all of their stats are set to TaskState.PENDING
     * // as they are created, just not scheduled.
     *
     * // When liftLift decides it's finished, it's state is set to TaskState.FINISHED, and
     * // then the openClaw task is scheduled to run, and it's state is set to
     * // TaskState.STARTING.
     *
     * // The above process repeats for the openClaw, lowerLift, and closeClaw tasks.
     *
     * // Under the hood, when the state of a task is changed, the observers are checked to
     * // see if any of the relative tasks are dependent on the new state to be scheduled.
     * // If so, the dependent task is scheduled to run.
     * ```
     *
     * Java demonstration:
     * ```java
     * Scheduler.schedule(liftLiftTo20).now();
     * Scheduler.schedule(openClaw).after(liftLiftTo20);
     *
     * Scheduler.schedule(lowerLiftTo10).after(liftLiftTo20);
     * Scheduler.schedule(closeClaw).during(lowerLiftTo10);
     *
     *
     * private final Task liftLiftTo20 = scheduledTask -> {
     *     // Lift logic here
     *
     *     if (/* Lift is at height */) {
     *         scheduledTask.flagAsDone();
     *     }
     * };
     *
     * private final Task openClaw = scheduledTask -> {
     *     // Claw logic here
     *
     *     if (/* Claw is open */) {
     *         scheduledTask.flagAsDone();
     *     }
     * };
    */
    var state: TaskState by observable(TaskState.PENDING) { _, _, newState ->
        // Every time state is changed, take the observers map,
        // Filter out the values who are not scheduled to run at the new state,
        // Then schedule the remaining tasks whose target state matches the new state to run

        // So if { ::task1, TaskState.FINISHED } is in the observers map, and the new state is
        // TaskState.FINISHED, then task1 will be scheduled to run.
        // If the new state is not TaskState.FINISHED, then task1 will not be scheduled to run.

        observers
            .filterValues { targetState -> targetState == newState }
            .forEach { observer -> Scheduler.scheduleNow(observer.key) }
    }

    /**
     * Binds the given [Task] to this [ScheduledTask] to be scheduled when the [TaskState] of this
     * [ScheduledTask] is the same as the target state provided.
     *
     * @param newTask The [Task] to be scheduled
     * @param targetState The [TaskState] that the [Task] will be scheduled on
     *
     * @return The [ScheduledTask] that was created to schedule the given [Task]
     */
    fun addObserver(newTask: ScheduledTask, targetState: TaskState): ScheduledTask {
        return newTask.also { observers[it] = targetState }
    }

    /**
     * Sets this [ScheduledTask's][ScheduledTask] state to [TaskState.FINISHED].
     */
    fun flagAsDone() {
        state = TaskState.FINISHED
    }

    /**
     * See [Scheduler] for info about these methods' usage
     */
    infix fun after(task: Task) = after(ScheduledTask(task))
    infix fun after(task: ScheduledTask) = Scheduler.scheduleAfter(task, this)

    /**
     * See [Scheduler] for info about these methods' usage
     */
    infix fun during(task: Task) = during(ScheduledTask(task))
    infix fun during(task: ScheduledTask) = Scheduler.scheduleDuring(task, this)

    /**
     * See [Scheduler] for info about these methods' usage
     */
    infix fun right(now: now) = Scheduler.scheduleNow(this)
    fun now() = Scheduler.scheduleNow(this)

    override fun equals(other: Any?): Boolean {
        return other === this || (other as? ScheduledTask)?.let { it.task == task } ?: false
    }

    override fun hashCode() = task.hashCode()
}