package org.firstinspires.ftc.teamcode.components.arm;

import static org.firstinspires.ftc.teamcode.util.RuntimeMode.DEBUG;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoData;
import org.firstinspires.ftc.teamcode.util.RobotConstants;

import kotlin.ranges.RangesKt;

public class Arm {
    private final Motor armMotor;
    private final PIDFController armPID;

    private HardwareMap hardwareMap;

    private double armCorrection;

    public Arm(HardwareMap hwMap) {
        hardwareMap = hwMap;

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

    public void setToRestingPos() {
        armCorrection = RobotConstants.Arm.VERTICAL;
    }

    public void setToBackwardsPos() {
        armCorrection = RobotConstants.Arm.BACKWARDS;
    }

    public void setToForwardsPos() {
        armCorrection = RobotConstants.Arm.FORWARDS;
    }

    public double getArmRawPosition(){
        return hardwareMap.analogInput.get("ARM_ENC").getVoltage();
    }

    public double getArmPosition(){
        // Don't question this very sus math okay it works
        return 2.5 * 480 * ((getArmRawPosition() - RobotConstants.Arm.VOLTAGE_BACKWARDS / (RobotConstants.Arm.VOLTAGE_FORWARDS - RobotConstants.Arm.VOLTAGE_BACKWARDS)) - 0.5);
    }

    public void update(Telemetry telemetry) {
        if (DEBUG) {
            // Constantly set PIDF to allow for hot reloading, also some telemetry
            armPID.setPIDF(
                RobotConstants.Arm.P,
                RobotConstants.Arm.I,
                RobotConstants.Arm.D,
                RobotConstants.Arm.F);
        }

        // Old code - uses builtin encoder
//        double correction = armPID.calculate(armMotor.getCurrentPosition(), armCorrection);
        double correction = armPID.calculate(getArmPosition(), armCorrection);
        armMotor.set(correction);

        telemetry.addData("Arm target pos", armCorrection);
    }
}
