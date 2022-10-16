package org.firstinspires.ftc.teamcode.opmodes;

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

        if(!runCommandWhileLoop)
            Scheduler.run(this);
        else if(runCommandWhileLoop)
            while(opModeIsActive() && !isStopRequested()){
                Scheduler.run(this);
            }
    }
}
