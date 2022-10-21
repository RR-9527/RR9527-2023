package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.components.scheduler.Action;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RobotCommon;
import org.firstinspires.ftc.teamcodekt.components.schedulerv2.GamepadEx2;
import org.firstinspires.ftc.teamcodekt.components.schedulerv2.Scheduler;
import org.firstinspires.ftc.teamcodekt.util.Arm;
import org.firstinspires.ftc.teamcodekt.util.Claw;
import org.firstinspires.ftc.teamcodekt.util.Lift;
import org.firstinspires.ftc.teamcodekt.util.Wrist;

@TeleOp
public class BaseOpV2 extends RobotCommon {
    private ServoEx wrist, claw;
    private CRServo intake;
    private Motor arm, liftA, liftB;

    private PIDFController liftAPID, liftBPID, armPID;

    @Override
    public void runOpMode() throws InterruptedException {
        GamepadEx2 gamepad_x1 = new GamepadEx2(gamepad1);

        //Intake:
        gamepad_x1.left_trigger.onTriggered(openClaw);
        gamepad_x1.left_trigger.onUntriggered(closeClaw);

        gamepad_x1.right_trigger.onTriggered(enableIntake);
        gamepad_x1.right_trigger.onUntriggered(disableIntake);

        //Arm:
        gamepad_x1.x.whileTriggered(() -> armCorrection = Arm.INTAKE_POS );
        gamepad_x1.y.whileTriggered(() -> armCorrection = Arm.VERTICAL   );
        gamepad_x1.b.whileTriggered(() -> armCorrection = Arm.DEPOSIT_POS);

        //Lift:
        gamepad_x1.right_bumper.whileTriggered(() -> liftCorrection = Lift.UP);

        //Wrist:
        gamepad_x1.dpad_up  .onTriggered(() -> wristPosition = Wrist.INTAKE_POS );
        gamepad_x1.dpad_down.onTriggered(() -> wristPosition = Wrist.DEPOSIT_POS);

        Scheduler.start(this, () -> {
            updateArm();
            updateLift();
            updateWrist();
        });
    }


    private final Action openClaw = () -> claw.setPosition(Claw.OPEN);
    private final Action closeClaw = () -> claw.setPosition(Claw.CLOSE);


    private final Action enableIntake = () -> intake.set(1);
    private final Action disableIntake = () -> intake.set(0);


    private double armCorrection = 0;

    private void updateArm() {
        double correction = armPID.calculate(arm.getCurrentPosition(), armCorrection);
        arm.set(correction);
        armCorrection = 0;
    }


    private double liftCorrection = 0;

    private void updateLift() {
        double correctionA = liftAPID.calculate(liftA.getCurrentPosition(), liftCorrection);
        double correctionB = liftBPID.calculate(liftA.getCurrentPosition(), liftCorrection);
        liftA.set(correctionA);
        liftB.set(correctionB);
        liftCorrection = Lift.DOWN;
    }


    private double wristPosition = 0;

    private void updateWrist() {
        wrist.setPosition(wristPosition);
    }


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

        liftAPID = new PIDFController(Lift.A.P, Lift.A.I, Lift.A.D, Lift.A.F);
        liftBPID = new PIDFController(Lift.B.P, Lift.B.I, Lift.B.D, Lift.B.F);
        armPID = new PIDFController(Arm.P, Arm.I, Arm.D, Arm.F);
    }
}
