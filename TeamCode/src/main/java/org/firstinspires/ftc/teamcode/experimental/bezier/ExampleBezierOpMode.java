package org.firstinspires.ftc.teamcode.experimental.bezier;

import com.arcrobotics.ftclib.command.OdometrySubsystem;
import com.arcrobotics.ftclib.command.PurePursuitCommand;
import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.kinematics.HolonomicOdometry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@SuppressWarnings("FieldCanBeLocal")
@Autonomous
@Disabled
public class ExampleBezierOpMode extends BezierFollower {

    // define our constants
    static final double TRACKWIDTH = 12.63;
    static final double WHEEL_DIAMETER = 1.8898 * 2;    // inches
    static double TICKS_TO_INCHES;
    static final double CENTER_WHEEL_OFFSET = (15.25 / 2) - 3;

    private HolonomicOdometry m_robotOdometry;
    private OdometrySubsystem m_odometry;
    private MecanumDrive m_robotDrive;
    private Motor fL, fR, bL, bR;
    private MotorEx leftEncoder, rightEncoder, centerEncoder;


    @Override
    public void initialize() {
        fL = new Motor(hardwareMap, "FL");
        fR = new Motor(hardwareMap, "FR");
        bL = new Motor(hardwareMap, "BL");
        bR = new Motor(hardwareMap, "BR");

        // create our drive object
        m_robotDrive = new MecanumDrive(fL, fR, bL, bR);

        leftEncoder = new MotorEx(hardwareMap, "FL");
        rightEncoder = new MotorEx(hardwareMap, "BR");
        centerEncoder = new MotorEx(hardwareMap, "BL");

        // calculate multiplier
        TICKS_TO_INCHES = WHEEL_DIAMETER * Math.PI / 8192;

        // create our odometry object and subsystem
        m_robotOdometry = new HolonomicOdometry(
            () -> leftEncoder.getCurrentPosition() * TICKS_TO_INCHES,
            () -> rightEncoder.getCurrentPosition() * TICKS_TO_INCHES,
            () -> centerEncoder.getCurrentPosition() * TICKS_TO_INCHES,
            TRACKWIDTH, CENTER_WHEEL_OFFSET
        );
        m_odometry = new OdometrySubsystem(m_robotOdometry);

        init_bezier(m_odometry, m_robotDrive, SpeedFunctions.sin, SpeedFunctions.quartic);
        run();
    }
}
