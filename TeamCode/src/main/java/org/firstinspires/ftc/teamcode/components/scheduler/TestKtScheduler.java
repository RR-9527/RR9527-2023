package org.firstinspires.ftc.teamcode.components.scheduler;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotors;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotorsKt;
import org.firstinspires.ftc.teamcodekt.components.scheduler.ScheduledTask;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;

@SuppressWarnings("FieldCanBeLocal")
public class TestKtScheduler extends LinearOpMode {
    private DriveMotors motors;

    /**
     * Override this method and place your code here.
     * <p>
     * Please do not swallow the InterruptedException, as it is used in cases
     * where the op mode needs to be terminated early.
     *
     * @throws InterruptedException
     */
    @Override
    public void runOpMode() throws InterruptedException {
        motors = DriveMotorsKt.initializedDriveMotorsV2(hardwareMap);

        waitForStart();

        ScheduledTask drive10in = Scheduler.scheduleNow(this::drive10in);
        Scheduler.scheduleDuring(drive10in, this::openClaw);

        Scheduler.run(this);
    }

    private void drive10in(ScheduledTask scheduledTask) {

    }

    private void openClaw(ScheduledTask scheduledTask) {

    }
}
