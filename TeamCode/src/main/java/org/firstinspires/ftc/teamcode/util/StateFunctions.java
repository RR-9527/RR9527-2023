package org.firstinspires.ftc.teamcode.util;

public class StateFunctions {
    public static boolean InRange(double input, double target, double marginOfError){
        return (target - marginOfError <= input) && (input <= target + marginOfError);
    }
}
