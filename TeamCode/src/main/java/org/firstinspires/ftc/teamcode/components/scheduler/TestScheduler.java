package org.firstinspires.ftc.teamcode.components.scheduler;

import static org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler.schedule;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotors;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Trigger;

public class TestScheduler extends LinearOpMode {
    private DriveMotors motors;

    public static void main(String[] args) throws InterruptedException {
        new TestScheduler().runOpMode();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();

        Trigger gamepad_a = new Trigger(() -> gamepad1.a);

        schedule(print_a_10_times).on(gamepad_a.risingEdge);

        // or
        // schedule(print_a_10_times).when(gamepad_a.risingEdge);

        schedule(print_done).after(print_a_10_times);

        Scheduler.run(this);
    }

    private int print_a_10_times_counter = 0;

    private final Task print_a_10_times = (scheduledTask) -> {
        System.out.println("a" + print_a_10_times_counter++);

        if (print_a_10_times_counter == 10) {
            scheduledTask.flagAsDone();
        }
    };

    private final Task print_done = (scheduledTask) -> {
        System.out.println("done");
        print_a_10_times_counter = 0;
        scheduledTask.flagAsDone();
    };
}
