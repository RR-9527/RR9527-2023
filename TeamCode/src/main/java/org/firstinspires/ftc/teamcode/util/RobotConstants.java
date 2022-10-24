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
    	public static double INTAKE_POS = 480;
    	public static double VERTICAL = 0;
    	public static double DEPOSIT_POS = -480;
    	public static double P = 0.0015;
    	public static double I = 0.1;
    	public static double D = 0.00001;
    	public static double F = 0;
    }

    @Config
    public static class Wrist {
    	public static double INTAKE_POS = .835;
		public static double REST = .5;
    	public static double DEPOSIT_POS = .185;
    }

    @Config
    public static class Lift {
    	public static double UP = 0.8;
    	public static double DOWN = 0.0;

		public static double ZERO = 0;
		public static double GROUND = 100;
		public static double LOW = 200;
		public static double MID = 300;
		public static double HIGH = 400;
    }

	@Config
	public static class LiftA {
		public static double P = 0.002;
		public static double I = 0;
		public static double D = 0;
		public static double F = 0;
	}

	@Config
	public static class LiftB {
		public static double P = 0.002;
		public static double I = 0;
		public static double D = 0;
		public static double F = 0;
	}

    @Config
    public static class TriggerData {
        // Minimum necessary trigger press before activating a function
    	public static double TRIGGER_THRESHOLD = 0.5;
    }
}
