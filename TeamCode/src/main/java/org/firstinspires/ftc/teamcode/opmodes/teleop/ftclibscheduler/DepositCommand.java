package org.firstinspires.ftc.teamcode.opmodes.teleop.ftclibscheduler;

import com.arcrobotics.ftclib.command.CommandBase;

public class DepositCommand extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final IntakeSubsystem m_subsystem;

    /**
     * Creates a new ExampleCommand.
     *
     * @param subsystem The subsystem used by this command.
     */
    public DepositCommand(IntakeSubsystem subsystem) {
        m_subsystem = subsystem;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        m_subsystem.stopWheels();
        m_subsystem.closeClaw();
        m_subsystem.setWristDepositPos();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
