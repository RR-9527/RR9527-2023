package org.firstinspires.ftc.teamcode.components.wrist;

import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.util.RobotConstants;

public class Wrist {
    private final ServoEx wristServo;

    private double wristPosition;

    public Wrist(HardwareMap hardwareMap) {
        wristServo =
            new SimpleServo(hardwareMap, "WR", 0, 180, AngleUnit.DEGREES);
    }

    public void setToRestingPos() {
        wristPosition = RobotConstants.Wrist.REST;
    }

    public void setToIntakePos() {
        wristPosition = RobotConstants.Wrist.INTAKE_POS;
    }

    public void setToDepositPos() {
        wristPosition = RobotConstants.Wrist.DEPOSIT_POS;
    }

    public void update() {
        wristServo.setPosition(wristPosition);
    }
}
