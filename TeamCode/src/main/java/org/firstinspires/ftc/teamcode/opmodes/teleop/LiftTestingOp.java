package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.arm.Arm;

import org.firstinspires.ftc.teamcode.components.lift.Lift;
import org.firstinspires.ftc.teamcode.components.voltagescaler.VoltageScaler;

import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;
import org.firstinspires.ftc.teamcodekt.components.scheduler.listeners.GamepadEx2;

/**
 * OpMode for testing lift.
 * <strong>CONTROLS:</strong>
 * All on gamepad 1
 * left/right stick y: control left/right lift motor - up moves lift up, and same for down
 * button a: set lift zero power behavior to floating
 * button b: set lift zero power behavior to brake
 */
@TeleOp
public class LiftTestingOp extends LinearOpMode {
    private Arm arm;
    private Lift lift;
    private GamepadEx2 driver;

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        lift.setFloating();
        waitForStart();

        Scheduler.beforeEach(() -> {
            arm.setToRestingPos();
        });

        driver.a.onRise(lift::setFloating);
        driver.b.onRise(lift::setBrake);

        Scheduler.start(this, () -> {
            // Update arm to keep at resting pos during testing
            arm.update(telemetry);

            double leftPower = driver.getGamepad().left_stick_y;
            double rightPower = driver.getGamepad().right_stick_y;
            lift.manualLiftControl(leftPower, leftPower);

            telemetry.addLine("Left stick is motor A, right stick is motor B, motor A is on left and motor B is on right side of bot");

            telemetry.addData("Left stick power:", leftPower);
            telemetry.addData("Right stick power:", rightPower);
            telemetry.addData("Lift A current draw:", lift.getACurrent());
            telemetry.addData("Lift B current draw:", lift.getBCurrent());
            telemetry.addData("Lift C current draw:", lift.getCCurrent());
            telemetry.addData("Lift A power:", lift.getCPower());
            telemetry.addData("Lift B power:", lift.getCPower());
            telemetry.addData("Lift C power:", lift.getCPower());
            telemetry.update();
        });
    }

    private void initHardware() {
        driver  = new GamepadEx2(gamepad1);
        VoltageScaler voltageScaler = new VoltageScaler(hardwareMap);
        arm    = new Arm(hardwareMap);
        lift   = new Lift(hardwareMap, voltageScaler);
    }
}
