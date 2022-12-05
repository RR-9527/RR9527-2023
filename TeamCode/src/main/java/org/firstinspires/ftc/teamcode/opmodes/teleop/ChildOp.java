package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.taskchains.BackwardsDepositChain;
import org.firstinspires.ftc.teamcode.components.taskchains.ForwardsDepositChain;
import org.firstinspires.ftc.teamcode.components.taskchains.IntakeChain;
import org.firstinspires.ftc.teamcode.util.RobotConstants;
import org.firstinspires.ftc.teamcodekt.components.scheduler.listeners.Listener;
import org.firstinspires.ftc.teamcodekt.components.scheduler.taskchains.CancellableTaskChain;
import org.firstinspires.ftc.teamcodekt.components.scheduler.taskchains.TaskChain;

@TeleOp
public class ChildOp extends RogueBaseTeleOp {
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

        driver.left_trigger(.1).whileHigh(this::halveDriveSpeed);
        driver.right_trigger(.1).whileHigh(this::decreaseDriveSpeedABit);

        new Listener(() -> lift.getHeight() > RobotConstants.Lift.HIGH)
            .whileHigh(() -> powerMulti /= 2);

        // -- CODRIVER OVERRIDES --

        codriver.a.onRise(this::stop); // Kill switch

        new Listener(() -> true).whileHigh(() -> {
            driveMotors.drive(gamepad2, localizer, powerMulti);
        });

        codriver.dpad_up   .onRise(lift::goToHigh);
        codriver.dpad_down .onRise(lift::goToZero);
        codriver.dpad_right.onRise(lift::goToMid);
        codriver.dpad_left .onRise(lift::goToLow);

        forwardsDepositChain .cancelOn(codriver.x);
        backwardsDepositChain.cancelOn(codriver.x);

        codriver.left_trigger(.1).whileHigh(this::halveDriveSpeed);
        codriver.right_trigger(.1).whileHigh(() -> powerMulti = 0.0);
    }

    @Override
    protected void initAdditionalHardware() {
        intakeChain = new IntakeChain(bot, 200);
        forwardsDepositChain = new ForwardsDepositChain(bot, 500);
        backwardsDepositChain = new BackwardsDepositChain(bot, 500);
    }
}
