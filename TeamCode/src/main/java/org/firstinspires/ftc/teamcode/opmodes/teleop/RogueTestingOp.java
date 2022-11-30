package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.taskchains.BackwardsDepositChain;
import org.firstinspires.ftc.teamcode.components.taskchains.ForwardsDepositChain;
import org.firstinspires.ftc.teamcode.components.taskchains.IntakeChain;
import org.firstinspires.ftc.teamcode.util.RobotConstants;
import org.firstinspires.ftc.teamcode.util.StateRotator;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveType;
import org.firstinspires.ftc.teamcodekt.components.scheduler.taskchains.CancellableTaskChain;
import org.firstinspires.ftc.teamcodekt.components.scheduler.listeners.Listener;
import org.firstinspires.ftc.teamcodekt.components.scheduler.taskchains.TaskChain;

@TeleOp
public class RogueTestingOp extends RogueBaseTeleOp {
    private StateRotator<DriveType> driveTypes;
    private TaskChain intakeChain;
    private CancellableTaskChain forwardsDepositChain, backwardsDepositChain;

    @Override
    public void scheduleTasks() {
        driver.dpad_up   .onRise(lift::goToHigh);
        driver.dpad_down .onRise(lift::goToZero);
        driver.dpad_right.onRise(lift::goToMid);
        driver.dpad_left .onRise(lift::goToLow);

        intakeChain.invokeOn(driver.left_bumper);

        forwardsDepositChain.invokeOn(driver.right_bumper);
        forwardsDepositChain.cancelOn(driver.x);

        backwardsDepositChain.invokeOn(driver.y);
        backwardsDepositChain.cancelOn(driver.x);

        driver.right_stick_y(.2).and(driver.b).whileHigh(() -> {
            lift.setHeight(lift.getHeight() + (int) (RobotConstants.Lift.MANUAL_ADJUSTMENT_MULT * Math.pow(-gamepad2.right_stick_y, 3)));
        });

        driver.a.onRise(this::rotateDriveType);

        driver.left_trigger(.1).whileHigh(this::halveDriveSpeed);
        driver.right_trigger(.1).whileHigh(this::decreaseDriveSpeedABit);

        new Listener(() -> lift.getHeight() > 1525)
            .whileHigh(() -> powerMulti /= 2);
    }

    private void rotateDriveType() {
        driveMotors.setDriveType(driveTypes.next());
    }

    @Override
    protected void initAdditionalHardware() {
        driveTypes = new StateRotator<>(DriveType.NORMAL, DriveType.IMPROVED, DriveType.FIELD_CENTRIC);
        intakeChain = new IntakeChain(bot, 200);
        forwardsDepositChain = new ForwardsDepositChain(bot, 500);
        backwardsDepositChain = new BackwardsDepositChain(bot, 500);
    }
}
