package org.firstinspires.ftc.teamcode.opmodes.teleop.ftclibscheduler;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.util.RobotConstants.Claw;
import org.firstinspires.ftc.teamcode.util.RobotConstants.Wrist;

public class IntakeSubsystem extends SubsystemBase {
    private ServoEx wrist, claw;
    private CRServo intake;

    public IntakeSubsystem(final HardwareMap hardwareMap, final String name){
        wrist = new SimpleServo(hardwareMap, "WR", 0, 180, AngleUnit.DEGREES);
        claw = new SimpleServo(hardwareMap, "WR", 0, 180, AngleUnit.DEGREES);
        intake = new CRServo(hardwareMap, "IN");
    }

    public void rollWheels(){
        intake.set(1);
    }

    public void stopWheels(){
        intake.set(0);
    }

    public void openClaw(){
        claw.setPosition(Claw.OPEN);
    }

    public void closeClaw(){
        claw.setPosition(Claw.CLOSE);
    }

    public void setWristIntakePos(){
        wrist.setPosition(Wrist.INTAKE_POS);
    }

    public void setWristDepositPos(){
        wrist.setPosition(Wrist.DEPOSIT_POS);
    }
}
