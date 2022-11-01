package org.firstinspires.ftc.teamcode.experimental.bezier;


import kotlin.jvm.functions.Function2;

public class SpeedFunctions {
    private static final double AMP_CONST = Math.pow(0.5, 4);

    public static Function2<Double, Double, Double> sin = (t, maxV) -> maxV * Math.sin(Math.PI * t);
    public static Function2<Double, Double, Double> quadratic = (t, maxV) -> -(maxV / (4 * AMP_CONST)) * Math.pow(t - 0.5, 2) + maxV;
    public static Function2<Double, Double, Double> quartic = (t, maxV) -> -(maxV / AMP_CONST) * Math.pow(t - 0.5, 4) + 1;
}
