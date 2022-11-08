package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;

public class RobotConstants {
	@Config
	public static class VoltagePID {
		public static int TARGET_VOLTAGE = 14;

		public static double P = 0.0;
		public static double I = 0;
		public static double D = 0.0;
		public static double F = 0.0;
	}

    @Config
    public static class Claw {
    	public static double INTAKE = 0.55;
    	public static double DEPOSIT = 0.8;
    	public static double CLOSE = 0.4;
    }

    @Config
    public static class Arm {
    	public static double BACKWARDS = 1500;
    	public static double VERTICAL = 300;
    	public static double FORWARDS = -320;

		public static double VOLTAGE_BACKWARDS = 0.539;
		public static double VOLTAGE_FORWARDS = 2.07;

    	public static double P = 0.001;
    	public static double I = 0.0;
    	public static double D = 0.00001;
    	public static double F = 0;

		public static boolean USE_ENC = true;

		public static double ENC_P = 0.004;
		public static double ENC_I = 0.0;
		public static double ENC_D = 0.00001;
		public static double ENC_F = 0;

		public static double ENC_BACKWARDS = 480;
		public static double ENC_VERTICAL = 0;
		public static double ENC_FORWARDS = -480;
    }

    @Config
    public static class Wrist {
    	public static double FORWARDS = .86;
		public static double REST = .5;
    	public static double BACKWARDS = .185;
    }

    @Config
    public static class Lift {
		public static int ZERO = 0;
		public static int LOW = 1350;
		public static int MID = 2100;
		public static int HIGH = 3000;

		public static double MANUAL_ADJUSTMENT_MULT = 4;

		public static int AUTO_INTAKE_1 = 420;
		public static int AUTO_INTAKE_2 = 320;
		public static int AUTO_INTAKE_3 = 240;
		public static int AUTO_INTAKE_4 = 160;

		public static double P = 0.0015;
		public static double I = 0.1;
		public static double D = 0.0001;
		public static double F = 0.00001;
    }
}
