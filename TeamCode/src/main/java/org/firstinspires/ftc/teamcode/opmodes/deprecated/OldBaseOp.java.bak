package org.firstinspires.ftc.teamcode.opmodes.deprecated;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RobotCommon;
import org.firstinspires.ftc.teamcode.util.RobotConstants.Arm;
import org.firstinspires.ftc.teamcode.util.RobotConstants.Lift;
import org.firstinspires.ftc.teamcode.util.RobotConstants.Wrist;

@Disabled
@TeleOp(name = "BaseOp")
public class OldBaseOp extends RobotCommon {
    private ServoEx wrist, claw;
    private CRServo intake;
    private Motor arm, liftA, liftB;


    private PIDFController liftAPID, liftBPID, armPID;

    /**
     * Override this method and place your code here.
     * <p>
     * Please do not swallow the InterruptedException, as it is used in cases
     * where the op mode needs to be terminated early.
     *
     * @throws InterruptedException
     */
    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        while (!isStopRequested() && opModeIsActive()) {
            updateDrivetrain();
            intake();
            updateWrist();
        }
    }

    /**
     * Method to init non-drivetrain hardware before entering the init phase.
     */
    @Override
    protected void initHardware() {
        arm = new Motor(hardwareMap, "AR", Motor.GoBILDA.RPM_84);
        arm.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        arm.setRunMode(Motor.RunMode.VelocityControl);

        liftA = new Motor(hardwareMap, "L1", Motor.GoBILDA.RPM_1150);
        liftA.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        liftA.setRunMode(Motor.RunMode.VelocityControl);

        liftB = new Motor(hardwareMap, "L2", Motor.GoBILDA.RPM_1150);
        liftB.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        liftB.setRunMode(Motor.RunMode.VelocityControl);
        liftB.setInverted(true);

        wrist = new SimpleServo(hardwareMap, "WR", 0, 180, AngleUnit.DEGREES);
        claw = new SimpleServo(hardwareMap, "WR", 0, 180, AngleUnit.DEGREES);
        intake = new CRServo(hardwareMap, "IN");

        // Lift PID's
        liftAPID = new PIDFController(Lift.P, Lift.I, Lift.D, Lift.F);
        liftBPID = new PIDFController(Lift.P, Lift.I, Lift.D, Lift.F);
        armPID = new PIDFController(Arm.P, Arm.I, Arm.D, Arm.F);
    }

    private void intake() {
//        if (game_pad1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > TriggerData.TRIGGER_THRESHOLD) {
//            claw.setPosition(Claw.INTAKE);
//        } else {
//            claw.setPosition(Claw.CLOSE);
//        }
//
//        if (game_pad1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > TriggerData.TRIGGER_THRESHOLD) {
//            intake.set(1);
//        } else {
//            intake.set(0);
//        }
    }

    private void updateArm() {
        double correction = 0;
        if (game_pad1.getButton(GamepadKeys.Button.X)) {
            correction = armPID.calculate(arm.getCurrentPosition(), Arm.BACKWARDS);
        } else if (game_pad1.getButton(GamepadKeys.Button.Y)) {
            correction = armPID.calculate(arm.getCurrentPosition(), Arm.VERTICAL);
        } else if (game_pad1.getButton(GamepadKeys.Button.B)) {
            correction = armPID.calculate(arm.getCurrentPosition(), Arm.FORWARDS);
        }
        arm.set(correction);
    }

    private void updateLift() {
//        double correctionA;
//        double correctionB;
//        if (game_pad1.getButton(GamepadKeys.Button.RIGHT_BUMPER)) {
//            correctionA = liftAPID.calculate(liftA.getCurrentPosition(), Lift.UP);
//            correctionB = liftBPID.calculate(liftB.getCurrentPosition(), Lift.UP);
//
//        } else {
//            correctionA = liftAPID.calculate(liftA.getCurrentPosition(), Lift.DOWN);
//            correctionB = liftBPID.calculate(liftB.getCurrentPosition(), Lift.DOWN);
//        }
//        liftA.set(correctionA);
//        liftB.set(correctionB);
    }

    private void updateWrist() {
        if (game_pad1.getButton(GamepadKeys.Button.DPAD_DOWN)) {
            wrist.setPosition(Wrist.BACKWARDS);
        }
        else{
            wrist.setPosition(Wrist.FORWARDS);
        }
    }
}
