package org.firstinspires.ftc.teamcode.opmodes.auto;

import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RobotCommon;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;

public abstract class ScheduledOpMode extends RobotCommon {
    /**
     * Varoble to determine whether or not to loop commands
     */
    protected boolean runCommandWhileLoop;

    public abstract void setupCommands();

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        setupCommands();

        do {
            Scheduler.run(this);
        } while (runCommandWhileLoop && opModeIsActive() && !isStopRequested());
    }
}
