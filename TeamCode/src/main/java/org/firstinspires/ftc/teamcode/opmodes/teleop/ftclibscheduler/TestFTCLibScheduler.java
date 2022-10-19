package org.firstinspires.ftc.teamcode.opmodes.teleop.ftclibscheduler;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.button.Button;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TestFTCLibScheduler extends CommandOpMode {
    private IntakeSubsystem intakeSubsystem;
    private DepositCommand deposit;
    private IntakeCommand intake;
    private Button m_grabButton, m_releaseButton;
    private GamepadEx m_driverOp;

    @Override
    public void initialize() {
        m_driverOp = new GamepadEx(gamepad1);
        intakeSubsystem = new IntakeSubsystem(hardwareMap, "intakeSubsystem");
        deposit = new DepositCommand(intakeSubsystem);
        intake = new IntakeCommand(intakeSubsystem);

        m_grabButton = (new GamepadButton(m_driverOp, GamepadKeys.Button.A)).whenPressed(intake);
        m_releaseButton = (new GamepadButton(m_driverOp, GamepadKeys.Button.B)).whenPressed(deposit);
    }
}