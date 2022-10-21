package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.roadrunner.drive.Drive;
import com.acmerobotics.roadrunner.localization.Localizer;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.roadrunner.drive.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RobotCommon;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotors;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotorsKt;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveType;
import org.firstinspires.ftc.teamcodekt.components.schedulerv2.GamepadEx2;
import org.firstinspires.ftc.teamcodekt.components.schedulerv2.Scheduler;
import org.firstinspires.ftc.teamcodekt.util.Arm;
import org.firstinspires.ftc.teamcodekt.util.Claw;
import org.firstinspires.ftc.teamcodekt.util.Lift;
import org.firstinspires.ftc.teamcodekt.util.Wrist;

import java.util.Deque;
import java.util.function.Consumer;

@TeleOp
public class BaseOpV2 extends RobotCommon {
    private DriveMotors driveMotors;
    private Localizer localizer;

    private ServoEx wrist, claw;
    private CRServo intake;
    private Motor arm, liftA, liftB;

    private PIDFController liftAPID, liftBPID, armPID;

    @Override
    public void runOpMode() throws InterruptedException {
        GamepadEx2 gamepadx1 = new GamepadEx2(gamepad1);

        //Intake:
        gamepadx1.left_trigger.onRise(openClaw)
                              .onFall(closeClaw);

        gamepadx1.left_bumper.onRise(enableIntake)
                             .onFall(disableIntake);

        //Arm:
        gamepadx1.x.onHigh(() -> armCorrection = Arm.INTAKE_POS );
        gamepadx1.y.onHigh(() -> armCorrection = Arm.VERTICAL   );
        gamepadx1.b.onHigh(() -> armCorrection = Arm.DEPOSIT_POS);

        //Lift:
        gamepadx1.right_bumper.onHigh(() -> liftCorrection = Lift.UP);

        //Wrist:
        gamepadx1.dpad_up  .onRise(() -> wristPosition = Wrist.INTAKE_POS );
        gamepadx1.dpad_down.onRise(() -> wristPosition = Wrist.DEPOSIT_POS);

        //Drive:
        gamepadx1.a.onRise(rotateDriveType);

        Scheduler.time(this, telemetry, () -> {
            updateArm();
            updateLift();
            updateWrist();
            drive();
            logData();
        });
    }


    private final Runnable openClaw = () -> claw.setPosition(Claw.OPEN);
    private final Runnable closeClaw = () -> claw.setPosition(Claw.CLOSE);


    private final Runnable enableIntake = () -> intake.set(1);
    private final Runnable disableIntake = () -> intake.set(0);


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

        driveMotors = DriveMotorsKt.initializedDriveMotorsV2(hardwareMap);
        localizer = new StandardTrackingWheelLocalizer(hardwareMap);
    }
}
