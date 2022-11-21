package org.firstinspires.ftc.teamcode.components.distancesensor;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ShortRangeSensor {
    private String name;
    private AnalogInput sensor;

    /**
     * Multiplied times the percentage of the maximum voltage to yield a distance IN CENTIMETERS
     * TODO: Empirically determine this value! Requires a tuning process.
     */
    private static final double DISTANCE_MULTIPLIER = 5.5;

    public ShortRangeSensor(HardwareMap hwMap, String name) {
        this.name = name;
        sensor = hwMap.analogInput.get(name);
    }

    public double getDistance() {
        return DISTANCE_MULTIPLIER * sensor.getVoltage() / sensor.getMaxVoltage();
    }
}
