package org.firstinspires.ftc.teamcode.opmodes.teleop;

public class MechanismPositions {}

final class Claw {
    public static final double OPEN = 0.65;
    public static final double CLOSE = 0.52;
}

final class Arm {
    public static final int INTAKE_POS = 480;
    public static final int VERTICAL = 0;
    public static final int DEPOSIT_POS = -480;

    public static final double ARM_P = 0.1;
    public static final double ARM_I = 0.1;
    public static final double ARM_D = 0.1;
    public static final double ARM_F = 0.1;
}

final class Lift {
    public static final double UP = 0.8;
    public static final double DOWN = 0;

    public static final double A_P = 0.5;
    public static final double A_I = 0.5;
    public static final double A_D = 0.5;
    public static final double A_F = 0.5;

    public static final double B_P = 0.5;
    public static final double B_I = 0.5;
    public static final double B_D = 0.5;
    public static final double B_F = 0.5;
}

final class Wrist {
    public static final double INTAKE_POS = 0.21;
    public static final double DEPOSIT_POS = 0.84;
}

final class TriggerData{
    // Minimum necessary trigger press before activating a function
    public static final double TRIGGER_THRESHOLD = 0.5;
}
