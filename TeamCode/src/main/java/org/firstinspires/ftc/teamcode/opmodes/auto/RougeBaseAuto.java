package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.arm.Arm;
import org.firstinspires.ftc.teamcode.components.claw.Claw;
import org.firstinspires.ftc.teamcode.components.intake.Intake;
import org.firstinspires.ftc.teamcode.components.lift.Lift;
import org.firstinspires.ftc.teamcode.components.voltagescaler.VoltageScaler;
import org.firstinspires.ftc.teamcode.components.wrist.Wrist;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

public abstract class RougeBaseAuto extends LinearOpMode {
    protected SampleMecanumDrive drive;

    protected Claw claw;
    protected Intake intake;
    protected Arm arm;
    protected Wrist wrist;
    protected Lift lift;
    protected VoltageScaler voltageScaler;

    protected void initHardware() {
        drive = new SampleMecanumDrive(hardwareMap);

        voltageScaler = new VoltageScaler(hardwareMap);
        claw   = new Claw(hardwareMap);
        intake = new Intake(hardwareMap);
        arm    = new Arm(hardwareMap);
        wrist  = new Wrist(hardwareMap);
        lift   = new Lift(hardwareMap, voltageScaler);
    }
}
