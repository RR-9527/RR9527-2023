package org.firstinspires.ftc.teamcode.components.arm;

import static org.firstinspires.ftc.teamcode.util.RuntimeMode.DEBUG;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.RobotConstants;
import org.firstinspires.ftc.teamcodekt.util.MU;

public class Arm {
    private final Motor armMotor;
    private final PIDFController armPID, armEncoderPID;

    private final AnalogInput sensor;

    private double armCorrection;

    public boolean useEncoder;

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
        armEncoderPID = new PIDFController(
            RobotConstants.Arm.ENC_P,
            RobotConstants.Arm.ENC_I,
            RobotConstants.Arm.ENC_D,
            RobotConstants.Arm.ENC_F
        );

        useEncoder = false;

        sensor = hwMap.analogInput.get("ARM_ENC");
    }

    public void checkResetEncoder() {
        if (MU.inRange(getArmPosition(), RobotConstants.Arm.VERTICAL, 3))
            armMotor.resetEncoder();
    }

    public void setToRestingPos() {
        armCorrection = (useEncoder)
            ? RobotConstants.Arm.ENC_VERTICAL
            : RobotConstants.Arm.VERTICAL;
    }

    public void setToBackwardsAutoPos() {
        armCorrection = (useEncoder)
            ? RobotConstants.Arm.ENC_BACKWARDS
            : RobotConstants.Arm.BACKWARDS_AUTO;
    }

    public void setToForwardsAutoPos() {
        armCorrection = (useEncoder)
            ? RobotConstants.Arm.ENC_FORWARDS
            : RobotConstants.Arm.FORWARDS_AUTO;
    }

    public void setToBackwardsTelePos() {
        armCorrection = (useEncoder)
            ? RobotConstants.Arm.ENC_BACKWARDS
            : RobotConstants.Arm.BACKWARDS_TELE;
    }

    public void setToForwardsTelePos() {
        armCorrection = (useEncoder)
            ? RobotConstants.Arm.ENC_FORWARDS
            : RobotConstants.Arm.FORWARDS_TELE;
    }

    public double getArmRawPosition(){
        return sensor.getVoltage();
    }

    public double getArmPosition(){
        // Don't question this very sus math okay it works
        return 2.5 * 480 * ((getArmRawPosition() - RobotConstants.Arm.VOLTAGE_BACKWARDS / (RobotConstants.Arm.VOLTAGE_FORWARDS - RobotConstants.Arm.VOLTAGE_BACKWARDS)) - 0.5);
    }

    public void update(Telemetry telemetry) {
        update(telemetry, false);
    }

    public void update(Telemetry telemetry, boolean useEncoder) {
        this.useEncoder = useEncoder;
        telemetry.addData("Encoder position", armMotor.getCurrentPosition());

        if (DEBUG) {
            // Constantly set PIDF to allow for hot reloading, also some telemetry
            armPID.setPIDF(
                RobotConstants.Arm.P,
                RobotConstants.Arm.I,
                RobotConstants.Arm.D,
                RobotConstants.Arm.F
            );
        }

        double correction = (useEncoder)
            ? armEncoderPID.calculate(armMotor.getCurrentPosition(), armCorrection)
            : armPID.calculate(getArmPosition(), armCorrection);

        armMotor.set(correction);

        telemetry.addData("Arm target pos", armCorrection);
    }
}
