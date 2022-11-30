package org.firstinspires.ftc.teamcode.components.lift;

import static org.firstinspires.ftc.teamcode.util.RuntimeMode.DEBUG;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.components.voltagescaler.VoltageScaler;
import org.firstinspires.ftc.teamcode.util.RobotConstants;
import org.firstinspires.ftc.teamcodekt.util.MU;

public class Lift {
    private final Motor liftA, liftB, liftC;

    private int liftHeight;
    private int prevLiftHeight;

    private final PIDFController liftPID;
    private final PIDFController liftIncreasingPID;

    private final VoltageScaler voltageScaler;

    public Lift(HardwareMap hwMap, VoltageScaler voltageScaler) {
        this.voltageScaler = voltageScaler;

        liftA = new Motor(hwMap, "L1", Motor.GoBILDA.RPM_435);
        liftA.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        liftA.setRunMode(Motor.RunMode.VelocityControl);
        liftA.resetEncoder();

        liftB = new Motor(hwMap, "L2", Motor.GoBILDA.RPM_435);
        liftB.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        liftB.setRunMode(Motor.RunMode.VelocityControl);
        liftB.setInverted(true);
        liftB.resetEncoder();

        liftC = new Motor(hwMap, "L3", Motor.GoBILDA.RPM_435);
        liftC.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        liftC.setRunMode(Motor.RunMode.VelocityControl);
        liftC.resetEncoder();

        liftPID = new PIDFController(RobotConstants.Lift.P, RobotConstants.Lift.I, RobotConstants.Lift.D, RobotConstants.Lift.F);
        liftIncreasingPID = new PIDFController(
            RobotConstants.Lift.INCREASING_P, RobotConstants.Lift.INCREASING_I,
            RobotConstants.Lift.INCREASING_D, RobotConstants.Lift.INCREASING_F);
    }

    public void setFloating() {
        liftA.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);
        liftB.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);
        liftC.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);
    }

    public void setBrake() {
        liftA.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        liftB.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        liftC.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
    }

    public void goToZero() {
        prevLiftHeight = liftHeight;
        liftHeight = RobotConstants.Lift.ZERO;
    }

    public void goToLow() {
        prevLiftHeight = liftHeight;
        liftHeight = RobotConstants.Lift.LOW;
    }

    public void goToMid() {
        prevLiftHeight = liftHeight;
        liftHeight = RobotConstants.Lift.MID;
    }

    public void goToHigh() {
        prevLiftHeight = liftHeight;
        liftHeight = RobotConstants.Lift.HIGH;
    }

    public int getCurrentPos() {
        return liftA.getCurrentPosition();
    }


    public void update(Telemetry telemetry) {
        // Default is not to use aggressive ascendance
        update(telemetry, false);
    }

    public void manualLiftControl(double powerA, double powerB) {
        liftA.setRunMode(Motor.RunMode.RawPower);
        liftA.set(powerA);
        liftB.setRunMode(Motor.RunMode.RawPower);
        liftB.set(powerB);
        liftC.setRunMode(Motor.RunMode.RawPower);
        liftC.set(powerA);
    }

    public double getACurrent() {
        DcMotorEx motorObj = (DcMotorEx) liftA.motor;
        return motorObj.getCurrent(CurrentUnit.AMPS);
    }

    public double getBCurrent() {
        DcMotorEx motorObj = (DcMotorEx) liftB.motor;
        return motorObj.getCurrent(CurrentUnit.AMPS);
    }

    public double getCCurrent() {
        DcMotorEx motorObj = (DcMotorEx) liftC.motor;
        return motorObj.getCurrent(CurrentUnit.AMPS);
    }

    public double getAPower() {
        DcMotorEx motorObj = (DcMotorEx) liftA.motor;
        return motorObj.getPower();
    }

    public double getBPower() {
        DcMotorEx motorObj = (DcMotorEx) liftB.motor;
        return motorObj.getPower();
    }

    public double getCPower() {
        DcMotorEx motorObj = (DcMotorEx) liftC.motor;
        return motorObj.getPower();
    }

    public void update(Telemetry telemetry, boolean aggressiveAscendance) {
        double voltageCorrection = voltageScaler.getVoltageCorrection();
        telemetry.addData("Voltage PIDF correction for lift", voltageCorrection);

        // Allows hot reloading for PIDF and outputs some telemetry
        if (DEBUG) {
            liftPID.setPIDF(RobotConstants.Lift.P, RobotConstants.Lift.I, RobotConstants.Lift.D, RobotConstants.Lift.F);

            telemetry.addData("Motor position", liftA.getCurrentPosition());
        }

        double correction;

        // If you want to increase lift height aggressively,
        // and the previous height the lift was set to was below the current target,
        // and the lift height is not within +/- 50 ticks of the target, use aggressive ascendance
        if (aggressiveAscendance && prevLiftHeight < liftHeight && !MU.inRange(getCurrentPos(), liftHeight, 50))
            correction = liftIncreasingPID.calculate(liftA.getCurrentPosition(), liftHeight + voltageCorrection);
            // In any other case, use default PIDF
        else
            correction = liftPID.calculate(liftA.getCurrentPosition(), liftHeight + voltageCorrection);

        telemetry.addData("Correction amount", correction);

        liftA.set(correction);
        liftB.set(correction);
        liftC.set(correction);

        telemetry.addData("Lift set position", liftHeight);
    }

    public int getHeight() {
        return liftHeight;
    }

    public void setHeight(int height) {
        liftHeight = Math.max(0, Math.min(height, 3000));
    }
}
