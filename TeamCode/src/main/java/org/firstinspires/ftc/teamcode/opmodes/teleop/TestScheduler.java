package org.firstinspires.ftc.teamcode.opmodes.teleop;

import org.firstinspires.ftc.teamcode.opmodes.scheduler.LinearOpModeScheduler;
import org.firstinspires.ftc.teamcodekt.components.gamepad._GamepadKt;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotors;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotorsKt;
import org.firstinspires.ftc.teamcodekt.components.shooter.Shooter;
import org.firstinspires.ftc.teamcodekt.components.shooter.ShooterKt;
import org.firstinspires.ftc.teamcodekt.util.MU;

import java.util.stream.Stream;

public class TestScheduler extends LinearOpModeScheduler {
    private DriveMotors motors;
    private Shooter shooter;

    public void initialize(){
        scheduleCommand(this::drive);
        scheduleCommand(this::updateTelemetry);
    }

    private void drive() {
        double speed = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotation = gamepad1.right_stick_x;

        double direction = Math.atan2(speed, strafe);
        double power = Math.sqrt(speed * speed + strafe * strafe);

        telemetry.addData("Power", power);

        double xComponent = power * Math.sin(direction - Math.PI / 4);
        double yComponent = power * Math.cos(direction - Math.PI / 4);

        double max = Math.max(Math.abs(xComponent), Math.abs(yComponent));

        double flp = MU.ifNaN(yComponent / max, 0) + rotation;
        double frp = MU.ifNaN(xComponent / max, 0) - rotation;
        double blp = MU.ifNaN(xComponent / max, 0) + rotation;
        double brp = MU.ifNaN(yComponent / max, 0) - rotation;

        boolean triggered = _GamepadKt.isJoystickTriggered(gamepad1);
        boolean slowMode = gamepad1.left_trigger > 0.8;

        double powerMulti;

        if (!triggered)
            powerMulti = 0.0;
        else if (slowMode)
            powerMulti = Math.max(1 - gamepad1.left_trigger, 1);
        else
            powerMulti = 1.0;

        double powerScale = Stream.of(flp, frp, blp, brp, 1.0)
                .map(Math::abs)
                .max(Double::compareTo)
                .get();

        motors.setPowers(flp, frp, blp, brp);
        motors.transformPowers(pow -> pow * powerMulti / powerScale);
    }

    public void updateTelemetry(){
        telemetry.update();
    }
}
