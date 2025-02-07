package org.firstinspires.ftc.teamcode.opmodes.teleop;


import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.arm.Arm;
import org.firstinspires.ftc.teamcode.components.bot.Bot;
import org.firstinspires.ftc.teamcode.components.claw.Claw;
import org.firstinspires.ftc.teamcode.components.intake.Intake;
import org.firstinspires.ftc.teamcode.components.lift.Lift;
import org.firstinspires.ftc.teamcode.components.voltagescaler.VoltageScaler;
import org.firstinspires.ftc.teamcode.components.wrist.Wrist;
import org.firstinspires.ftc.teamcode.roadrunner.drive.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcode.util.RobotConstants;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotors;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;
import org.firstinspires.ftc.teamcodekt.components.scheduler.listeners.GamepadEx2;

@SuppressWarnings("SameParameterValue")
public abstract class RogueBaseTeleOp extends LinearOpMode {
    protected double powerMulti;
    protected DriveMotors driveMotors;
    protected Localizer localizer;

    protected Claw claw;
    protected Intake intake;
    protected Arm arm;
    protected Wrist wrist;
    protected Lift lift;
    protected VoltageScaler voltageScaler;
    protected Bot bot;

    protected GamepadEx2 driver;
    protected GamepadEx2 codriver;

    protected abstract void scheduleTasks();

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        waitForStart();

        Scheduler.beforeEach(() -> {
            arm.setToRestingPos();
            wrist.setToRestingPos();
            powerMulti = 1.0;
        });

        scheduleTasks();

        Scheduler.start(this, () -> {
            arm.update(telemetry);
            lift.update(telemetry, RobotConstants.Lift.USE_AGGRESSIVE_ASCENDANCE);
            wrist.update();

            driveMotors.drive(gamepad1, localizer, powerMulti);

            doEveryLoop();

            telemetry.update();
        });
    }

    protected void initAdditionalHardware() {}

    protected void doEveryLoop() {}

    private void initHardware() {
        driver   = new GamepadEx2(gamepad1);
        codriver = new GamepadEx2(gamepad2);

        driveMotors = new DriveMotors(hardwareMap);
        localizer   = new StandardTrackingWheelLocalizer(hardwareMap);

        voltageScaler = new VoltageScaler(hardwareMap);
        claw   = new Claw(hardwareMap);
        intake = new Intake(hardwareMap);
        arm    = new Arm(hardwareMap);
        wrist  = new Wrist(hardwareMap);
        lift   = new Lift(hardwareMap, voltageScaler);

        bot = new Bot(driveMotors, localizer, claw, intake, arm, wrist, lift);

        initAdditionalHardware();
    }

    protected void decreaseDriveSpeedABit() {
        powerMulti = Math.max(1.0 - gamepad1.left_trigger, .35);
    }

    protected void halveDriveSpeed() {
        powerMulti /= 2;
    }
}
