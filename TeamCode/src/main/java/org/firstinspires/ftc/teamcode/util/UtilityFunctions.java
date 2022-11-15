package org.firstinspires.ftc.teamcode.util;

public class UtilityFunctions {

    public static boolean inRange(double input, double target, double marginOfError){
        return (target - marginOfError <= input) && (input <= target + marginOfError);
    }

    public static double avg(double... inputs){
        double sum = 0;
        for(double item: inputs)
            sum += item;
        return sum / inputs.length;
    }

}
