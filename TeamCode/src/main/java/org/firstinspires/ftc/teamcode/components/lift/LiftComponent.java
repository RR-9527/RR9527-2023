package org.firstinspires.ftc.teamcode.components.lift;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.RobotConstants;
import org.firstinspires.ftc.teamcode.util.RobotConstants.LiftA;
import org.firstinspires.ftc.teamcode.util.RobotConstants.LiftB;

@Config
public class LiftComponent {
    private final Motor liftA, liftB;
//    private final PIDFController liftAPID, liftBPID;
    public static double kP;
    public static double kI;
    public static double kD;

    public static double setVal;

    private double liftHeight;

    private PIDFController liftAPID, liftBPID;

    public LiftComponent(HardwareMap hwMap) {
        setVal = 0.3;

        liftA = new Motor(hwMap, "L1", Motor.GoBILDA.RPM_1150);
        liftA.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        liftA.setRunMode(Motor.RunMode.VelocityControl);
        liftA.resetEncoder();

        liftB = new Motor(hwMap, "L2", Motor.GoBILDA.RPM_1150);
        liftB.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        liftB.setRunMode(Motor.RunMode.VelocityControl);
        liftB.setInverted(true);
        liftB.resetEncoder();

        liftBPID = new PIDFController(LiftB.P, LiftB.I, LiftB.D, LiftB.F);
        liftAPID = new PIDFController(LiftA.P, LiftA.I, LiftA.D, LiftA.F);
    }

    public void goToZero() {
        liftHeight = RobotConstants.Lift.ZERO;
    }

    public void goToGround() {
        liftHeight = RobotConstants.Lift.GROUND;
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
        double correctionA = liftAPID.calculate(liftA.getCurrentPosition(), liftHeight);
        double correctionB = liftBPID.calculate(liftA.getCurrentPosition(), liftHeight);
        liftA.set(correctionA);
        liftB.set(correctionB);

        telemetry.addData("Lift Height", liftHeight);
        telemetry.update();
    }
}
