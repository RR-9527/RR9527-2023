package org.firstinspires.ftc.teamcode.opmodes.deprecated;

import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.arm.Arm;
import org.firstinspires.ftc.teamcode.components.claw.Claw;
import org.firstinspires.ftc.teamcode.components.intake.Intake;
import org.firstinspires.ftc.teamcode.components.lift.LiftComponent;
import org.firstinspires.ftc.teamcode.components.wrist.Wrist;
import org.firstinspires.ftc.teamcode.roadrunner.drive.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcode.util.StateRotator;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotors;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveType;
import org.firstinspires.ftc.teamcodekt.components.scheduler.GamepadEx2;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Listener;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Timer;

@TeleOp
public class RogueTestingOpBackup extends LinearOpMode {
    private DriveMotors driveMotors;
    private Localizer localizer;
    StateRotator<DriveType> driveTypes;

    private Claw claw;
    private Intake intake;
    private Arm arm;
    private Wrist wrist;
    private LiftComponent lift;

    private GamepadEx2 driver;

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
        driver.dpad_up   .onRise(lift::goToHigh);
        driver.dpad_down .onRise(lift::goToRest);
        driver.dpad_right.onRise(lift::goToMid);
        driver.dpad_left .onRise(lift::goToLow);

        // Intake chain:
        intakeChain(driver.left_bumper, new Timer(200));

        // Deposit chain:
        depositChain(driver.right_bumper, new Timer(500));

        // Drive:
        driver.a.onRise(rotateDriveType);

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

            .onDone(lift::goToRest);
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
        driveType = driveTypes.next();
    };

    private void drive() {
        driveMotors.drive(gamepad1, localizer, driveType);
    }

    private void initHardware() {
        driver = new GamepadEx2(gamepad1);

        driveMotors = new DriveMotors(hardwareMap);
        localizer = new StandardTrackingWheelLocalizer(hardwareMap);
        driveTypes = new StateRotator<>(DriveType.NORMAL, DriveType.IMPROVED, DriveType.FIELD_CENTRIC);

        claw = new Claw(hardwareMap);
        intake = new Intake(hardwareMap);
        arm = new Arm(hardwareMap);
        wrist = new Wrist(hardwareMap);

        lift = new LiftComponent(hardwareMap);
    }
}
