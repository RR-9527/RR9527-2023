package org.firstinspires.ftc.teamcode.components.claw;

import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.util.RobotConstants;

public class Claw {
    private final ServoEx clawServo;

    public Claw(HardwareMap hwMap) {
        clawServo = new SimpleServo(hwMap, "CL", 0, 180, AngleUnit.DEGREES);
    }

    public void openForIntake() {
//        clawServo.setPosition(RobotConstants.Claw.INTAKE);
    }

    public void openForDeposit() {
//        clawServo.setPosition(RobotConstants.Claw.DEPOSIT);
    }

    public void close() {
//        clawServo.setPosition(RobotConstants.Claw.CLOSE);
    }
}
