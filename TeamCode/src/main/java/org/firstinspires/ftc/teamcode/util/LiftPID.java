package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

public class LiftPID {
    private double kp, ki, kd;
    private double totalError, lastError, lastTime;
    private ArrayList<Double> errors;

    public LiftPID(double kp, double ki, double kd) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        lastTime = 0;
        errors = new ArrayList<Double>();
    }

    public double getP(double error) {
        return kp * error;
    }

    public double getI(double error) {
        if (Math.abs(error) < 1)
            totalError = 0;
        return ki * totalError;
    }

    public double getD(double currentError) {
        return kd * (currentError - lastError);
    }

    public boolean checkCorrection(double error){

        if(Math.abs(errors.get(errors.size()-1)) < Math.abs(error))
            return true;
        else
            return false;

    }
    public void setPD(double p, double d){
        kp = p;
        kd = d;
    }

    public double getCorrection(double error) {
       /* if(errors.size() != 0) {
            totalError += error;
            double potentialError = getP(error) + getI(error) + getD(error, runtime);
            if (Math.abs(error) < 0.001 || (checkCorrection(potentialError))) {
                totalError -= error;
                return 0;
            }
            totalError -= error;
        }*/

        if (Math.abs(error) < 0.001) {
            return 0;
        }

        totalError += error;

        double output = getP(error) + getI(error) + getD(error);

        lastError = error;
        errors.add(output);


        return output;
    }

    public ArrayList<Double> aman(){
        return errors;
    }
}