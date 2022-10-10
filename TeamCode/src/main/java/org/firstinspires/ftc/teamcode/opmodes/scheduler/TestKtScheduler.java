package org.firstinspires.ftc.teamcode.opmodes.scheduler;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotors;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotorsKt;

import kotlin.Unit;

public class TestKtScheduler extends LinearOpMode {
    private DriveMotors motors;
    private Scheduler scheduler;

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
        scheduler = Scheduler.INSTANCE;

        waitForStart();

        ScheduledTask t1 = scheduler.schedule(this::drive10in);
        scheduler.scheduleDuring(t1, this::openClaw);

        scheduler.run(this);
    }

    private Unit drive10in(ScheduledTask scheduledTask) {
        return null;
    }

    private Unit openClaw(ScheduledTask scheduledTask) {
        return null;
    }

}
