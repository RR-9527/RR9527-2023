package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.arm.Arm;
import org.firstinspires.ftc.teamcode.components.claw.Claw;
import org.firstinspires.ftc.teamcode.components.intake.Intake;
import org.firstinspires.ftc.teamcode.components.lift.LiftComponent;
import org.firstinspires.ftc.teamcode.components.wrist.Wrist;
import org.firstinspires.ftc.teamcode.roadrunner.drive.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcode.util.MultiStateToggle;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotors;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveType;
import org.firstinspires.ftc.teamcodekt.components.scheduler.GamepadEx2;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Listener;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Timer;

@TeleOp
public class RogueOp extends LinearOpMode {
    private DriveMotors driveMotors;
    private Localizer localizer;

    private Claw claw;
    private Intake intake;
    private Arm arm;
    private Wrist wrist;

    private LiftComponent lift;

    private GamepadEx2 gamepadx1;
    // TODO: After VRHS Trunk or Treat, implement two-driver control using gamepadx2
    private GamepadEx2 gamepadx2;

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        waitForStart();


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
        intakeChain(gamepadx1.right_bumper, new Timer(200));

        // Deposit chain:
        depositChain(gamepadx1.left_bumper, new Timer(500));
//        gamepadx1.right_bumper.onRise()

        // Drive:
        gamepadx1.a.onRise(rotateDriveType);

        // Start:
        Scheduler.time(this, telemetry, () -> {
            arm.update(telemetry);
            lift.update(telemetry);
            wrist.update();
            drive();

        });
    }

    private void intakeChain(Listener listener, Timer timer) {
        listener
            .onRise(intake::enable)
            .onRise(claw::openForIntake)
            .onRise(lift::goToZero)

            .whileHigh(arm::setToIntakePos)
            .whileHigh(wrist::setToIntakePos)

            .onFall(timer::reset)
            .onFall(claw::close)
            .onFall(intake::disable);

        timer
            .whileWaiting(arm::setToIntakePos)
            .whileWaiting(wrist::setToIntakePos)

            .onDone(lift::goToGround);
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
        gamepadx1 = new GamepadEx2(gamepad1);
        gamepadx2 = new GamepadEx2(gamepad2);

        driveMotors = new DriveMotors(hardwareMap);
        localizer = new StandardTrackingWheelLocalizer(hardwareMap);

        claw = new Claw(hardwareMap);
        intake = new Intake(hardwareMap);
        arm = new Arm(hardwareMap);
        wrist = new Wrist(hardwareMap);

        lift = new LiftComponent(hardwareMap);
    }

    
}
