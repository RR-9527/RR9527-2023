package org.firstinspires.ftc.teamcode.components.arm;

import static org.firstinspires.ftc.teamcode.util.RuntimeMode.DEBUG;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.RobotConstants;

public class Arm {
    private final Motor armMotor;
    private final PIDFController armPID;

    private double armCorrection;

    public Arm(HardwareMap hwMap) {
        armMotor = new Motor(hwMap, "AR", Motor.GoBILDA.RPM_84);
        armMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        armMotor.setRunMode(Motor.RunMode.VelocityControl);
        armMotor.resetEncoder();

        armPID = new PIDFController(
            RobotConstants.Arm.P,
            RobotConstants.Arm.I,
            RobotConstants.Arm.D,
            RobotConstants.Arm.F
        );
    }

    public void setToDefaultPos() {
        armCorrection = RobotConstants.Arm.VERTICAL;
    }

    public void setToIntakePos() {
        armCorrection = RobotConstants.Arm.INTAKE_POS;
    }

    public void setToDepositPos() {
        armCorrection = RobotConstants.Arm.DEPOSIT_POS;
    }

    public void update(Telemetry telemetry) {
        if (DEBUG) {
            // Constantly set PIDF to allow for hot reloading, also some telemetry
            armPID.setPIDF(
                RobotConstants.Arm.P,
                RobotConstants.Arm.I,
                RobotConstants.Arm.D,
                RobotConstants.Arm.F);
            telemetry.addData("Arm position", armMotor.getCurrentPosition());
        }

        double correction = armPID.calculate(armMotor.getCurrentPosition(), armCorrection);
        armMotor.set(correction);
    }
}
