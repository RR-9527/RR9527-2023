package org.firstinspires.ftc.teamcodekt.components.scheduler

import org.firstinspires.ftc.teamcode.components.scheduler.Task
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler.after
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler.during
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler.right
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler.schedule
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
     * The map of [Tasks][Task] that are dependent on this task. The given [Tasks][Task] will scheduled when
     * the [TaskState] of the [ScheduledTask] it is bound to is [TaskState.FINISHED].
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
     * ```
     * val liftLift  = Scheduler.schedule { setLiftHeight(it, 20) } now please
     * please schedule ::openClaw after liftLift
     *
     * val lowerLift = Scheduler.schedule { setLiftHeight(it, 10) } after openClaw
     * please schedule ::closeClaw during lowerLift
     *
     * fun setLiftHeight(task: ScheduledTask, height: Int) {
     *     // Lift logic here
     *
     *     if (lift is at height) {
     *          task.state = TaskState.FINISHED
     *     }
     * }
     *
     * fun openClaw(task: ScheduledTask) {
     *    // Claw logic here
     *
     *    if (claw is open) {
     *      task.state = TaskState.FINISHED
     *    }
     * }
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
     * // Under the hood, when the state of a task is changed, the observers are checked to see
     * // if any of the relative tasks are dependent on the new state to be scheduled. If so,
     * // the dependent task is scheduled to run.
     * ```
     *
     * Java demonstration:
     * ```java
     * ScheduledTask liftLift =
     *      Scheduler.scheduleNow((st) -> setLiftHeight(st, 20));
     *
     * ScheduledTask openClaw =
     *      Scheduler.scheduleAfter(liftLift, this::openClaw);
     *
     * ScheduledTask lowerLift =
     *      Scheduler.scheduleAfter(openClaw, (st) -> setLiftHeight(st, 10));
     *
     * ScheduledTask closeClaw =
     *      Scheduler.scheduleDuring(lowerLift, this::closeClaw);
     *
     *
     * void setLiftHeight(ScheduledTask task, int height) {
     *    // Lift logic here
     *
     *    if (lift is at height) {
     *      task.setState(TaskState.FINISHED);
     *    }
     * }
     *
     * void openClaw(ScheduledTask task) {
     *   // Claw logic here
     *
     *   if (claw is open) {
     *      task.setState(TaskState.FINISHED);
     *   }
     * }
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

        // If the new state is TaskState.FINISHED, then the observers map is cleared,
        // and the task unschedules itself, so it stops running.
        if (newState == TaskState.FINISHED) {
            observers.clear()
            Scheduler.unschedule(this)
        }
    }

    /**
     * Binds the given [Task] to this [ScheduledTask] to be scheduled when the [TaskState] of this
     * [ScheduledTask] is the same as the target state provided.
     *
     * @param task The [Task] to be scheduled
     * @param targetSTate The [TaskState] that the [Task] will be scheduled on
     *
     * @return The [ScheduledTask] that was created to schedule the given [Task]
     */
    fun addObserver(task: ScheduledTask, targetSTate: TaskState): ScheduledTask {
        return task.also { observers[it] = targetSTate }
    }

    override fun equals(other: Any?): Boolean {
        return (other as? ScheduledTask)?.task == task
    }

    override fun hashCode() = task.hashCode()
}