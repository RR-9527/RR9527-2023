package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.roadrunner.localization.Localizer;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.roadrunner.drive.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RobotCommon;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotors;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotorsKt;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveType;
import org.firstinspires.ftc.teamcodekt.components.schedulerv2.GamepadEx2;
import org.firstinspires.ftc.teamcodekt.components.schedulerv2.Listener;
import org.firstinspires.ftc.teamcodekt.components.schedulerv2.Scheduler;
import org.firstinspires.ftc.teamcode.util.RobotConstants.Arm;
import org.firstinspires.ftc.teamcode.util.RobotConstants.Claw;
import org.firstinspires.ftc.teamcode.util.RobotConstants.Lift;
import org.firstinspires.ftc.teamcode.util.RobotConstants.Wrist;
import org.firstinspires.ftc.teamcode.util.RobotConstants.LiftA;
import org.firstinspires.ftc.teamcode.util.RobotConstants.LiftB;
import org.firstinspires.ftc.teamcode.util.LiftState;
import org.firstinspires.ftc.teamcodekt.components.schedulerv2.SignalTrigger;

@TeleOp
public class BaseOpV2 extends RobotCommon {
    private DriveMotors driveMotors;
    private Localizer localizer;

    private ServoEx wrist, claw;
    private CRServo intake;
    private Motor arm, liftA, liftB;

    private PIDFController liftAPID, liftBPID, armPID;
    private LiftState liftState;

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        GamepadEx2 gamepadx1 = new GamepadEx2(gamepad1);
        liftState = new LiftState();

        waitForStart();



        // Lift: increment up and down with button presses
        gamepadx1.dpad_up  .onRise(() -> liftState.inc());
        gamepadx1.dpad_down.onRise(() -> liftState.dec());
        gamepadx1.dpad_right  .onRise(() -> liftState.maximum());
        gamepadx1.dpad_left.onRise(() -> liftState.minimum());


        // Intake chain:
        gamepadx1.right_bumper.whileHigh(() -> armCorrection = Arm.INTAKE_POS  );
        gamepadx1.right_bumper.whileHigh(() -> wristPosition = Wrist.INTAKE_POS);
        gamepadx1.right_bumper.onRise(enableIntake);
        gamepadx1.right_bumper.onRise(openClaw);

        gamepadx1.right_bumper.onFall(disableIntake);
        gamepadx1.right_bumper.onFall(closeClaw);


        // Deposit chain:
        ElapsedTime elapsedTime = new ElapsedTime();

        gamepadx1.left_bumper.whileHigh(() -> armCorrection = Arm.DEPOSIT_POS);
        gamepadx1.left_bumper.whileHigh(() -> wristPosition = Wrist.DEPOSIT_POS);

        gamepadx1.left_bumper.onFall(elapsedTime::reset);
        gamepadx1.left_bumper.onFall(reverseIntake);
        gamepadx1.left_bumper.onFall(openClawDeposit);

        Listener listener =
            Scheduler.getOrCreateListener("disableIntake", () -> elapsedTime.milliseconds() > 500);

        listener.subscribe(disableIntake, SignalTrigger.RISING_EDGE);
        listener.subscribe(closeClaw, SignalTrigger.RISING_EDGE);

        listener.subscribe(() -> armCorrection = Arm.DEPOSIT_POS, SignalTrigger.IS_LOW  );
        listener.subscribe(() -> wristPosition = Wrist.DEPOSIT_POS, SignalTrigger.IS_LOW);


        // Drive:
        gamepadx1.a.onRise(rotateDriveType);

        Scheduler.time(this, telemetry, () -> {
            updateArm();
            updateLift();
            updateWrist();
            drive();
//            logData();
        });
    }


    private final Runnable openClaw = () -> claw.setPosition(Claw.OPEN);
    private final Runnable openClawDeposit = () -> claw.setPosition(Claw.OPEN_DEPOSIT);
    private final Runnable closeClaw = () -> claw.setPosition(Claw.CLOSE);


    private final Runnable enableIntake = () -> intake.set(1);
    private final Runnable reverseIntake = () -> intake.set(-1);
    private final Runnable disableIntake = () -> intake.set(0);


    private double armCorrection = 0;

    private void updateArm() {
        double correction = armPID.calculate(arm.getCurrentPosition(), armCorrection);
        arm.set(correction);
        armCorrection = 0;
    }


    private int heightCounter = 1;

    private void updateLift() {
        double correctionA = liftAPID.calculate(liftA.getCurrentPosition(), liftState.get());
        double correctionB = liftBPID.calculate(liftA.getCurrentPosition(), liftState.get());
        liftA.set(correctionA);
        liftB.set(correctionB);
        telemetry.addData("Correction A", correctionA);
        telemetry.addData("Correction B", correctionB);
    }


    private double wristPosition = 0.5;

    private void updateWrist() {
        wrist.setPosition(wristPosition);
        wristPosition = 0.5;
    }


    private DriveType driveType = DriveType.NORMAL;

    private final Runnable rotateDriveType = () -> {
        if (driveType == DriveType.NORMAL) {
            driveType = DriveType.IMPROVED;
        } else if (driveType == DriveType.IMPROVED) {
            driveType = DriveType.FIELD_CENTRIC;
        } else {
            driveType = DriveType.NORMAL;
        }
    };

    private void drive() {
        driveMotors.drive(gamepad1, localizer, driveType);
    }


    private void logData() {
        telemetry.addData("Drive type", driveType.name());
        driveMotors.logData(telemetry, DcMotorEx::getPower);
    }


    @Override
    protected void initHardware() {
        arm = new Motor(hardwareMap, "AR", Motor.GoBILDA.RPM_84);
        arm.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        arm.setRunMode(Motor.RunMode.VelocityControl);
        arm.resetEncoder();

        liftA = new Motor(hardwareMap, "L1", Motor.GoBILDA.RPM_1150);
        liftA.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        liftA.setRunMode(Motor.RunMode.VelocityControl);
        liftA.resetEncoder();

        liftB = new Motor(hardwareMap, "L2", Motor.GoBILDA.RPM_1150);
        liftB.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        liftB.setRunMode(Motor.RunMode.VelocityControl);
        liftB.setInverted(true);
        liftB.resetEncoder();

        wrist = new SimpleServo(hardwareMap, "WR", 0, 180, AngleUnit.DEGREES);
        claw = new SimpleServo(hardwareMap, "CL", 0, 180, AngleUnit.DEGREES);
        intake = new CRServo(hardwareMap, "IN");

        liftAPID = new PIDFController(LiftA.P, LiftA.I, LiftA.D, LiftA.F);
        liftBPID = new PIDFController(LiftB.P, LiftB.I, LiftB.D, LiftB.F);
        armPID = new PIDFController(Arm.P, Arm.I, Arm.D, Arm.F);

        driveMotors = DriveMotorsKt.initializedDriveMotorsV2(hardwareMap);
        localizer = new StandardTrackingWheelLocalizer(hardwareMap);
    }
}
