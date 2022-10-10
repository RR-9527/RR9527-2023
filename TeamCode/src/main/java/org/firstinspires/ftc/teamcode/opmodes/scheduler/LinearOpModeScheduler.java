package org.firstinspires.ftc.teamcode.opmodes.scheduler;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.ArrayList;

public abstract class LinearOpModeScheduler extends LinearOpMode {
    private ArrayList<Command> methodsToRun = new ArrayList<Command>();

    public interface Command{
        void execute();
    }

    public void scheduleCommand(Command command){
        methodsToRun.add(command);
    }

    public void run(){
        while(opModeIsActive() && !isStopRequested()){
            for(Command cmd: methodsToRun){
                cmd.execute();
            }
        }
    }
}
