package org.firstinspires.ftc.teamcode.components.scheduler;

import static org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler.schedule;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotors;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotorsKt;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;
import org.firstinspires.ftc.teamcodekt.components.scheduler.TaskState;

@SuppressWarnings("FieldCanBeLocal")
public class TestKtScheduler extends LinearOpMode {
    private DriveMotors motors;

    public static void main(String[] args) throws InterruptedException {
        new TestKtScheduler().runOpMode();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        motors = DriveMotorsKt.initializedDriveMotorsV2(hardwareMap);

        waitForStart();

        schedule(print_a_10_times).now();
        schedule(print_done).after(print_a_10_times);

        Scheduler.run(this);
    }

    private int print_a_10_times_counter = 0;

    private final Task print_a_10_times = (scheduledTask) -> {
        System.out.println("a" + print_a_10_times_counter++);

        if (print_a_10_times_counter == 10) {
            scheduledTask.setState(TaskState.FINISHED);
            print_a_10_times_counter = 0;
        }
    };

    private final Task print_done = (scheduledTask) -> {
        System.out.println("done");
        scheduledTask.setState(TaskState.FINISHED);
    };
}
