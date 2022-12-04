package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;

public class RobotConstants {
	@Config
	public static class VoltagePID {
		public static double TARGET_VOLTAGE = 14;

		public static double P = 0.0;
		public static double I = 0;
		public static double D = 0.0;
		public static double F = 0.0;
	}

    @Config
    public static class Claw {
		public static double INTAKE_AUTO = 0.55;
    	public static double INTAKE_TELE = 0.48;
    	public static double DEPOSIT = 0.55;
    	public static double CLOSE = 0.35;
    }

    @Config
    public static class Arm {
    	public static double BACKWARDS_AUTO = 1390;
    	public static double BACKWARDS_TELE = 1500;
    	public static double VERTICAL = 300;
    	public static double FORWARDS_AUTO = -275;
    	public static double FORWARDS_TELE = -305;

		public static double VOLTAGE_BACKWARDS = 0.539;
		public static double VOLTAGE_FORWARDS = 2.07;

    	public static double P = 0.00085;
    	public static double I = 0.0;
    	public static double D = 0.00002;
    	public static double F = 0;

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
		public static int LOW = 1040 + 51;
		public static int MID = 1720 + 51;
		public static int HIGH = 2420 + 51;

		public static double MANUAL_ADJUSTMENT_MULT = 50;

		public static int AUTO_INTAKE_1 = 370 + 3;
		public static int AUTO_INTAKE_2 = 295 + 2;
		public static int AUTO_INTAKE_3 = 180 + 2;
		public static int AUTO_INTAKE_4 = 110 + 2;
		public static int AUTO_INTAKE_5 = 0;

		public static boolean USE_AGGRESSIVE_ASCENDANCE = false;

		public static double P = 0.0011;
		public static double I = 0.2;
		public static double D = 0.0001;
		public static double F = 0.00001;

		public static double INCREASING_P = .01;
		public static double INCREASING_I = 0;
		public static double INCREASING_D = 0;
		public static double INCREASING_F = 0;
    }
}
