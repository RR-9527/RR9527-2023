package org.firstinspires.ftc.teamcode.components.intake;

import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private final CRServo intakeServo;

    public Intake(HardwareMap hwMap) {
        intakeServo = new CRServo(hwMap, "IN");
    }

    public void enable()  {
        intakeServo.set(1);
    }

    public void disable() {
        intakeServo.set(0);
    }

    public void reverse() {
        intakeServo.set(-1);
    }
}
