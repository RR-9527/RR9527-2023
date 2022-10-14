package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opmodes.ScheduledOpMode;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;

@Disabled
@TeleOp
public class BaseOp extends ScheduledOpMode {
    @Override
    public void setupCommands() {
//        Scheduler.scheduleNow(this::updateDrivetrain);
    }

    /**
     * Used for teleop to drive in robot-centric or field-centric mode (default is robot-centric)
     */
    protected void updateDrivetrain(){
        if (!FIELD_CENTRIC) {
            teleopDrivebase.driveRobotCentric(
                    game_pad1.getLeftX(),
                    game_pad1.getLeftY(),
                    game_pad1.getRightX(),
                    false
            );
        } else {
            teleopDrivebase.driveFieldCentric(
                    game_pad1.getLeftX(),
                    game_pad1.getLeftY(),
                    game_pad1.getRightX(),
                    imu.getRotation2d().getDegrees(),   // gyro value passed in here must be in degrees
                    false
            );
        }
    }
}
