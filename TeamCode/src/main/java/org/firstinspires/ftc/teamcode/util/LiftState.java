package org.firstinspires.ftc.teamcode.util;

public class LiftState{
    private double[] positions;
    private int liftIndex;
    public LiftState(){
        positions = new double[]{RobotConstants.Lift.ZERO, RobotConstants.Lift.LOW, RobotConstants.Lift.MID, RobotConstants.Lift.HIGH};
        liftIndex = 0;
    }

    public void inc(){
        if(liftIndex+1 < positions.length)
            liftIndex++;
    }
    public void dec(){
        if(liftIndex-1 > -1)
            liftIndex--;
    }
    public double get(){
        return -positions[liftIndex];
    }
}