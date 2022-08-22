package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.sun.tools.javac.util.List;

import org.firstinspires.ftc.teamcode.components.deadwheels.Deadwheels;
import org.firstinspires.ftc.teamcode.components.deadwheels.DeadwheelsKt;
import org.firstinspires.ftc.teamcode.components.motors.DriveMotors;
import org.firstinspires.ftc.teamcode.components.motors.DriveMotorsKt;
import org.firstinspires.ftc.teamcode.components.shooter.Shooter;
import org.firstinspires.ftc.teamcode.components.shooter.ShooterKt;

import java.util.stream.Stream;

@TeleOp(name = "TestOpJava")
public class TestOp extends OpMode {
    private DriveMotors motors;
    private Deadwheels deadwheels;
    private Shooter shooter;

    @Override
    public void init() {
        motors = DriveMotorsKt.initializedDriveMotors(hardwareMap);
        deadwheels = DeadwheelsKt.initializedDeadwheels(motors);
        shooter = ShooterKt.initializedShooter(hardwareMap);
    }

    @Override
    public void loop() {
        drive();
        shoot();

        deadwheels.snapshotTicks();
        updateTelemetry(telemetry);
    }

    private void drive() {
        final boolean triggered = List.of(
            gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x
        ).stream().anyMatch(x -> Math.abs(x) > 0.1);

        float flp = gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x;
        float frp = -gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x;
        float blp = gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x;
        float brp = -gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x;

        float max = Stream.of(flp, frp, blp, brp)
            .map(Math::abs)
            .max(Float::compare)
            .get();

        if (max > 1) {
            flp /= max;
            frp /= max;
            blp /= max;
            brp /= max;
        }

        double powerMulti =
            !triggered ? 0 : gamepad1.left_trigger > .5 ? .35 : 1;

        motors.getFrontLeft().setPower(flp * powerMulti);
        motors.getFrontRight().setPower(frp * powerMulti);
        motors.getBackLeft().setPower(blp * powerMulti);
        motors.getBackRight().setPower(brp * powerMulti);

        motors.logData(telemetry, DcMotorEx::getPower);
    }

    private void shoot() {
        double power = gamepad1.left_trigger > .6
            ? gamepad1.left_trigger
            : 0;
        shooter.getMotor().setPower(power);

        shooter.setIndexerToggled(gamepad1.a);
    }
}
