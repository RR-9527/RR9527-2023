package org.firstinspires.ftc.teamcode.components.distancesensor;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.text.DecimalFormat;

public class ShortRangeSensor {
    private final String name;
    private final AnalogInput sensor;
    private Telemetry telemetry;
    private static final DecimalFormat fmt;

    static {
        fmt = new DecimalFormat("###.00");
    }

    /**
     * Multiplied times the percentage of the maximum voltage to yield a distance IN CENTIMETERS
     * TODO: Empirically determine this value! Requires a tuning process.
     */

    public ShortRangeSensor(HardwareMap hwMap, String name) {
        this.name = name;
        sensor = hwMap.analogInput.get(name);
    }

    public ShortRangeSensor(HardwareMap hwMap, String name, Telemetry telemetry) {
        this.name = name;
        this.telemetry = telemetry;
        sensor = hwMap.analogInput.get(name);
    }

    public String getName(){
        return name;
    }

    public double getDistance() {
        double distance = model(sensor.getVoltage());
        if(telemetry != null)
            telemetry.addData("Sensor reading", distance);

        return distance;
    }

    public double model(double input){
        return 239.664*input-37.1034;
    }
}
