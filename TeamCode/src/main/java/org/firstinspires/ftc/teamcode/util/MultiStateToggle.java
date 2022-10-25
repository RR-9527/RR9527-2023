package org.firstinspires.ftc.teamcode.util;

/**
 * State machine for general usage. Will index through multiple numerical values before returning
 * back to zero, or back to the max value.
 */
public class MultiStateToggle {
    private int currentState;
    private int numStates;
    public MultiStateToggle(int numStates, int currentState){
        this.numStates = numStates;
        this.currentState = currentState;
    }

    public void inc(){
        if(currentState+1 >= numStates){
            currentState = 0;
            return;
        }
        currentState++;
    }

    public void dec(){
        if(currentState-1 < 0){
            currentState = numStates-1;
            return;
        }
        currentState--;
    }

    public int get(){
        return currentState;
    }
}
