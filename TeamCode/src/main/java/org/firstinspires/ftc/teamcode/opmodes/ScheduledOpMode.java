package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RobotCommon;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;

public abstract class ScheduledOpMode extends RobotCommon {
    public abstract void setupCommands();
    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        setupCommands();
        Scheduler.run(this);
    }
}
