package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.gamepad._GamepadKt;
import org.firstinspires.ftc.teamcode.components.motors.DriveMotors;
import org.firstinspires.ftc.teamcode.components.motors.DriveMotorsKt;
import org.firstinspires.ftc.teamcode.components.shooter.Shooter;
import org.firstinspires.ftc.teamcode.components.shooter.ShooterKt;
import org.firstinspires.ftc.teamcode.util.MU;

import java.util.stream.Stream;

@TeleOp(name = "TestOpJava")
public class TestOp extends OpMode {
    private DriveMotors motors;
    private Shooter shooter;

    @Override
    public void init() {
        motors = DriveMotorsKt.initializedDriveMotorsV2(hardwareMap);
        shooter = ShooterKt.initializedShooter(hardwareMap);
    }

    @Override
    public void loop() {
        drive();
        shoot();
    }

    private void drive() {
        double speed = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotation = gamepad1.right_stick_x;

        double direction = Math.atan2(speed, strafe);
        double power = Math.sqrt(speed * speed + strafe * strafe);

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

    private void shoot() {
        shooter.setPower(gamepad1.right_trigger, .6);
        shooter.setIndexerToggled(gamepad1.a);
    }
}
