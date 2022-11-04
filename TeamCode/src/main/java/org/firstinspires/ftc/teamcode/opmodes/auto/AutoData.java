package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;

@Config
public class AutoData {
    public static double LOWER_OFFSET = 0.05;
    public static double DEPOSIT_OFFSET = 0.3;
    public static double RETRACT_OFFSET = 0.1;
    public static double INTAKE_OFFSET = 0.05;
    public static double INTAKE_LIFT_OFFSET = 0.125;


    public static double DEPOSIT_DELAY = 0.5;
    public static double INTAKE_DELAY = 0.25;


    // Positions in centimeters/degrees of where to intake/deposit
    public static double INTAKE_X = 157;
    public static double INTAKE_Y = -30;

    public static double DEPOSIT_X = 86;
    public static double DEPOSIT_Y = -22;
    public static double DEPOSIT_ANGLE = 132;

    // Cone stack intaking positions
    public static int[] INTAKING_POSITIONS = {520, 470, 420, 370, 320};
}
