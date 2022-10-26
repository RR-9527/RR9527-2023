package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.RuntimeMode;
import org.firstinspires.ftc.teamcode.components.arm.Arm;
import org.firstinspires.ftc.teamcode.components.claw.Claw;
import org.firstinspires.ftc.teamcode.components.intake.Intake;
import org.firstinspires.ftc.teamcode.components.lift.LiftComponent;
import org.firstinspires.ftc.teamcode.components.wrist.Wrist;
import org.firstinspires.ftc.teamcode.roadrunner.drive.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotors;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveType;
import org.firstinspires.ftc.teamcodekt.components.scheduler.GamepadEx2;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Listener;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Timer;

@SuppressWarnings("SameParameterValue")
public abstract class RougeBaseOp extends LinearOpMode {
    private DriveMotors driveMotors;
    private Localizer localizer;

    protected Claw claw;
    protected Intake intake;
    protected Arm arm;
    protected Wrist wrist;
    protected LiftComponent lift;

    protected GamepadEx2 driver;
    protected GamepadEx2 codriver;

    protected abstract void scheduleTasks();
    
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        waitForStart();

        // Before each tick:
        Scheduler.beforeEach(() -> {
            arm.setToDefaultPos();
            wrist.setToRestingPos();
        });

        scheduleTasks();

        // After each tick:
        Runnable afterEach = () -> {
            arm.update(telemetry);
            lift.update(telemetry);
            wrist.update();
        };

        if (RuntimeMode.TIME_LOOP) {
            Scheduler.time(this, telemetry, afterEach);
        } else {
            Scheduler.start(this, afterEach);
        }
    }

    protected void intakeChain(Listener button, int clawClosingTime) {
        Timer clawTimer = new Timer(clawClosingTime);

        button
            .onRise(intake::enable)
            .onRise(claw::openForIntake)
            .onRise(lift::goToZero)

            .whileHigh(arm::setToIntakePos)
            .whileHigh(wrist::setToIntakePos)

            .onFall(clawTimer::reset)
            .onFall(claw::close)
            .onFall(intake::disable);

        clawTimer
            .whileWaiting(arm::setToIntakePos)
            .whileWaiting(wrist::setToIntakePos)

            .onDone(lift::goToRest);
    }

    protected void depositChain(Listener button, int depositTime) {
        Timer depositTimer = new Timer(depositTime);

        button
            .whileHigh(arm::setToDepositPos)
            .whileHigh(wrist::setToDepositPos)

            .onFall(depositTimer::reset)
            .onFall(intake::reverse)
            .onFall(claw::openForDeposit);

        depositTimer
            .whileWaiting(arm::setToDepositPos)
            .whileWaiting(wrist::setToDepositPos)

            .onDone(intake::disable)
            .onDone(claw::close);
    }

    protected DriveType driveType = DriveType.NORMAL;

    protected void drive() {
        driveMotors.drive(gamepad1, localizer, driveType);
    }

    protected void initHardware() {
        driver = new GamepadEx2(gamepad1);
        codriver = new GamepadEx2(gamepad2);

        driveMotors = new DriveMotors(hardwareMap);
        localizer = new StandardTrackingWheelLocalizer(hardwareMap);

        claw = new Claw(hardwareMap);
        intake = new Intake(hardwareMap);
        arm = new Arm(hardwareMap);
        wrist = new Wrist(hardwareMap);

        lift = new LiftComponent(hardwareMap);
    }
}
