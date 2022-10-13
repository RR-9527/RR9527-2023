package org.firstinspires.ftc.teamcode.components.scheduler;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.ArrayList;

public abstract class LinearOpModeScheduler extends LinearOpMode {
    private final ArrayList<Command> methodsToRun = new ArrayList<Command>();

    public interface Command{
        void execute();
    }

    public abstract void initialize();

    public void scheduleCommand(Command command){
        methodsToRun.add(command);
    }

    public void runOpMode() throws InterruptedException{
        initialize();
        while(opModeIsActive() && !isStopRequested()){
            for(Command cmd: methodsToRun){
                cmd.execute();
            }
        }
    }
}
