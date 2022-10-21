package org.firstinspires.ftc.teamcode.opmodes.teleop.ftclibscheduler;

import com.arcrobotics.ftclib.command.CommandBase;

public class IntakeCommand extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final IntakeSubsystem m_subsystem;

    /**
     * Creates a new ExampleCommand.
     *
     * @param subsystem The subsystem used by this command.
     */
    public IntakeCommand(IntakeSubsystem subsystem) {
        m_subsystem = subsystem;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        m_subsystem.rollWheels();
        m_subsystem.openClaw();
        m_subsystem.setWristIntakePos();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
