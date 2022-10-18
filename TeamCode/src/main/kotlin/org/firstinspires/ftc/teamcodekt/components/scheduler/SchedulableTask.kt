//package org.firstinspires.ftc.teamcodekt.components.scheduler
//
//import org.firstinspires.ftc.teamcode.components.scheduler.Task
//import org.firstinspires.ftc.teamcodekt.components.scheduler.keywords.now
//
//class SchedulableTask {
//    /**
//     * Syntactic sugar for [Scheduler.scheduleAfter]
//     *
//     * Kotlin usage example:
//     * ```kotlin
//     * please schedule task2 after task1
//     * ```
//     *
//     * Java usage example:
//     * ```java
//     * Scheduler.schedule(task1).after(task2);
//     * ```
//     *
//     * __Note: See [Scheduler] documentation for important information__
//     *
//     * @param task The [Task] to be scheduled after this [ScheduledTask]
//     */
//    infix fun after(task: Task) = after(ScheduledTask(task))
//    infix fun after(task: ScheduledTask) = Scheduler.scheduleAfter(task, this)
//
//    /**
//     * Syntactic sugar for [Scheduler.scheduleDuring]
//     *
//     * Kotlin usage example:
//     * ```kotlin
//     * please schedule task2 during task1
//     * ```
//     *
//     * Java usage example:
//     * ```java
//     * Scheduler.schedule(task1).during(task2);
//     * ```
//     *
//     * __Note: See [Scheduler] documentation for important information__
//     *
//     * @param task The [Task] to be scheduled after this [ScheduledTask]
//     */
//    infix fun during(task: Task) = during(ScheduledTask(task))
//    infix fun during(task: ScheduledTask) = Scheduler.scheduleDuring(task, this)
//
//    /**
//     * Syntactic sugar for [Scheduler.scheduleNow]
//     *
//     * Kotlin usage example:
//     * ```kotlin
//     * please schedule task1 right now
//     * ```
//     *
//     * Java usage example:
//     * ```java
//     * Scheduler.schedule(task1).now();
//     * ```
//     *
//     * __Note: See [Scheduler] documentation for important information__
//     *
//     * @param task The [Task] to be scheduled after this [ScheduledTask]
//     */
//    infix fun right(now: now) = Scheduler.scheduleNow(this)
//    fun now() = Scheduler.scheduleNow(this)
//
//    /**
//     * Syntactic sugar for [Scheduler.scheduleOn]
//     *
//     * Kotlin usage example:
//     * ```kotlin
//     * val gamepad1_a = Trigger { gamepad1.a }
//     *
//     * please schedule task2 on gamepad1_a.risingEdge
//     * ```
//     *
//     * Java usage example:
//     * ```java
//     * Trigger gamepad1_a = new Trigger(() -> gamepad1.a);
//     *
//     * Scheduler.schedule(task2).on(gamepad1_a.risingEdge);
//     * //or
//     * Scheduler.schedule(task2).when(gamepad1_a.risingEdge);
//     * ```
//     *
//     * __Note: See [Scheduler] documentation for important information__
//     *
//     * @param task The [Task] to be scheduled after this [ScheduledTask]
//     */
//    infix fun on(predicate: () -> Boolean) = Scheduler.scheduleOn(this, predicate)
//    infix fun `when`(predicate: () -> Boolean) = on(predicate)
//}