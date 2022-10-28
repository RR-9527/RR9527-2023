package org.firstinspires.ftc.teamcode.components.bot;

import com.acmerobotics.roadrunner.localization.Localizer;

import org.firstinspires.ftc.teamcode.components.arm.Arm;
import org.firstinspires.ftc.teamcode.components.claw.Claw;
import org.firstinspires.ftc.teamcode.components.intake.Intake;
import org.firstinspires.ftc.teamcode.components.lift.Lift;
import org.firstinspires.ftc.teamcode.components.wrist.Wrist;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveMotors;

public class Bot {
    private final DriveMotors driveMotors;
    private final Localizer localizer;
    private final Claw claw;
    private final Intake intake;
    private final Arm arm;
    private final Wrist wrist;
    private final Lift lift;

    public Bot(DriveMotors driveMotors, Localizer localizer, Claw claw, Intake intake, Arm arm, Wrist wrist, Lift lift) {
        this.driveMotors = driveMotors;
        this.localizer = localizer;
        this.claw = claw;
        this.intake = intake;
        this.arm = arm;
        this.wrist = wrist;
        this.lift = lift;
    }

    public DriveMotors driveMotors() {
        return driveMotors;
    }

    public Localizer localizer() {
        return localizer;
    }

    public Claw claw() {
        return claw;
    }

    public Intake intake() {
        return intake;
    }

    public Arm arm() {
        return arm;
    }

    public Wrist wrist() {
        return wrist;
    }

    public Lift lift() {
        return lift;
    }
}
