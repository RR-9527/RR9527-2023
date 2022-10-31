package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;

public class RobotConstants {
    @Config
    public static class Claw {
    	public static double INTAKE = 0.2;
    	public static double DEPOSIT = 0.1;
    	public static double CLOSE = 0.4;
    }

    @Config
    public static class Arm {
    	public static double BACKWARDS = 480;
    	public static double VERTICAL = 0;
    	public static double FORWARDS = -480;

    	public static double P = 0.002;
    	public static double I = 0.085;
    	public static double D = 0.00001;
    	public static double F = 0;
    }

    @Config
    public static class Wrist {
    	public static double FORWARDS = .835;
		public static double REST = .5;
    	public static double BACKWARDS = .185;
    }

    @Config
    public static class Lift {
		public static int ZERO = 0;
		public static int REST = 0;
		public static int LOW = 1500;
		public static int MID = 2250;
		public static int HIGH = 3000;

		public static double P = 0.0015;
		public static double I = 0.1;
		public static double D = 0.0001;
		public static double F = 0.00001;
    }
}
