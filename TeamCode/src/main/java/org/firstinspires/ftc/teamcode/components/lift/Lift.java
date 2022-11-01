package org.firstinspires.ftc.teamcode.components.lift;

import static org.firstinspires.ftc.teamcode.util.RuntimeMode.DEBUG;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.RobotConstants;

@Config
public class Lift {
    private final Motor liftA, liftB;

    private int liftHeight;

    private PIDFController liftAPID, liftBPID;

    public Lift(HardwareMap hwMap) {
        liftA = new Motor(hwMap, "L1", Motor.GoBILDA.RPM_1150);
        liftA.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        liftA.setRunMode(Motor.RunMode.VelocityControl);
//        liftA.resetEncoder();

        liftB = new Motor(hwMap, "L2", Motor.GoBILDA.RPM_1150);
        liftB.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        liftB.setRunMode(Motor.RunMode.VelocityControl);
        liftB.setInverted(true);
//        liftB.resetEncoder();

//        liftBPID = new PIDFController(Lift.P, Lift.I, Lift.D, Lift.F);
        liftAPID = new PIDFController(RobotConstants.Lift.P, RobotConstants.Lift.I, RobotConstants.Lift.D, RobotConstants.Lift.F);
    }

    public void goToZero() {
        liftHeight = RobotConstants.Lift.ZERO;
    }

    public void goToLow() {
        liftHeight = RobotConstants.Lift.LOW;
    }

    public void goToMid() {
        liftHeight = RobotConstants.Lift.MID;
    }

    public void goToHigh() {
        liftHeight = RobotConstants.Lift.HIGH;
    }

    public void update(Telemetry telemetry) {
        // Allows hot reloading for PIDF and outputs some telemetry
        if(DEBUG) {
            liftAPID.setPIDF(RobotConstants.Lift.P, RobotConstants.Lift.I, RobotConstants.Lift.D, RobotConstants.Lift.F);
//            liftBPID.setPIDF(Lift.P, Lift.I, Lift.D, Lift.F);

            telemetry.addData("Motor position", liftA.getCurrentPosition());
        }

        double correctionA = liftAPID.calculate(liftA.getCurrentPosition(), liftHeight);
//        double correctionB = liftABPID.calculate(liftA.getCurrentPosition(), liftHeight);
        liftA.set(correctionA);
        liftB.set(correctionA);
    }

    public int getHeight() {
        return liftHeight;
    }

    public void setHeight(int height) {
        liftHeight = Math.max(0, Math.min(height, 3000));
    }
}
