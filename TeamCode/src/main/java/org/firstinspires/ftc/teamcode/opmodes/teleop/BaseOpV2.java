package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.roadrunner.localization.Localizer;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.arm.Arm;
import org.firstinspires.ftc.teamcode.components.claw.Claw;
import org.firstinspires.ftc.teamcode.components.intake.Intake;
import org.firstinspires.ftc.teamcode.components.lift.Lift;
import org.firstinspires.ftc.teamcode.components.wrist.Wrist;
import org.firstinspires.ftc.teamcode.roadrunner.drive.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotors;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveType;
import org.firstinspires.ftc.teamcodekt.components.schedulerv2.GamepadEx2;
import org.firstinspires.ftc.teamcodekt.components.schedulerv2.Listener;
import org.firstinspires.ftc.teamcodekt.components.schedulerv2.Scheduler;
import org.firstinspires.ftc.teamcodekt.components.schedulerv2.Timer;

@TeleOp
public class BaseOpV2 extends LinearOpMode {
    private DriveMotors driveMotors;
    private Localizer localizer;

    private Claw claw;
    private Intake intake;
    private Arm arm;
    private Wrist wrist;

    private Lift lift;

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        waitForStart();

        GamepadEx2 gamepadx1 = new GamepadEx2(gamepad1);

        // Before each tick:
        Scheduler.beforeEach(() -> {
            arm.setToDefaultPos();
            wrist.setToRestingPos();
        });

        // Lift: increment up and down with button presses
        gamepadx1.dpad_up   .onRise(lift::goToHigh);
        gamepadx1.dpad_down .onRise(lift::goToGround);
        gamepadx1.dpad_right.onRise(lift::goToMid);
        gamepadx1.dpad_left .onRise(lift::goToLow);

        // Intake chain:
        intakeChain(gamepadx1.right_bumper);

        // Deposit chain:
        depositChain(gamepadx1.left_bumper, new Timer(500));

        // Drive:
        gamepadx1.a.onRise(rotateDriveType);

        // Start:
        Scheduler.time(this, telemetry, () -> {
            arm.update();
            lift.update(telemetry);
            wrist.update();
            drive();
        });
    }

    private void intakeChain(Listener listener) {
        listener
            .onRise(intake::enable)
            .onRise(claw::openForIntake)
            .onRise(lift::goToZero)

            .whileHigh(arm::setToIntakePos)
            .whileHigh(wrist::setToIntakePos)

            .onFall(intake::disable)
            .onFall(claw::close)
            .onRise(lift::goToGround);
    }

    private void depositChain(Listener listener, Timer timer) {
        listener
            .whileHigh(arm::setToDepositPos)
            .whileHigh(wrist::setToDepositPos)

            .onFall(timer::reset)
            .onFall(intake::reverse)
            .onFall(claw::openForDeposit);

        timer
            .whileWaiting(arm::setToDepositPos)
            .whileWaiting(wrist::setToDepositPos)

            .onDone(intake::disable)
            .onDone(claw::close);
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

    private void initHardware() {
        driveMotors = new DriveMotors(hardwareMap);
        localizer = new StandardTrackingWheelLocalizer(hardwareMap);

        claw = new Claw(hardwareMap);
        intake = new Intake(hardwareMap);
        arm = new Arm(hardwareMap);
        wrist = new Wrist(hardwareMap);

        lift = new Lift(hardwareMap);
    }
}
