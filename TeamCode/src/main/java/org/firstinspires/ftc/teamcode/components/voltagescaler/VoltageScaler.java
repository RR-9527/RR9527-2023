package org.firstinspires.ftc.teamcode.components.voltagescaler;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.RobotConstants;

public class VoltageScaler {
    private HardwareMap hardwareMap;

    private PIDFController voltagePIDF;

    public VoltageScaler(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;

        voltagePIDF = new PIDFController(RobotConstants.VoltagePID.P, RobotConstants.VoltagePID.I,
            RobotConstants.VoltagePID.D,RobotConstants.VoltagePID.F);
    }

    public double getVoltageCorrection(){
        double voltage = hardwareMap.voltageSensor.iterator().next().getVoltage();
        return voltagePIDF.calculate(voltage, RobotConstants.VoltagePID.TARGET_VOLTAGE);
    }
}
