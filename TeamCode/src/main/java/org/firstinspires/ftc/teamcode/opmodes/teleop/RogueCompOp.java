package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.taskchains.BackwardsDepositChain;
import org.firstinspires.ftc.teamcode.components.taskchains.ForwardsDepositChain;
import org.firstinspires.ftc.teamcode.components.taskchains.IntakeChain;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Listener;
import org.firstinspires.ftc.teamcodekt.components.scheduler.TaskChain;

@TeleOp
public class RogueCompOp extends RougeBaseOp {
    TaskChain intakeChain, forwardsDepositChain, backwardsDepositChain;

    @Override
    public void scheduleTasks() {
        codriver.dpad_up   .onRise(lift::goToHigh);
        codriver.dpad_down .onRise(lift::goToZero);
        codriver.dpad_right.onRise(lift::goToMid);
        codriver.dpad_left .onRise(lift::goToLow);

        intakeChain.invokeOn(codriver.left_bumper);
        forwardsDepositChain.invokeOn(codriver.right_bumper);
        backwardsDepositChain.invokeOn(codriver.y);

        codriver.right_stick_y(.2).whileHigh(() -> {
            lift.setHeight(lift.getHeight() + (int) (13 * -gamepad2.right_stick_y));
        });

        driver.left_trigger.whileHigh(this::decreaseDriveSpeed);

        new Listener(() -> lift.getHeight() > 1000)
            .whileHigh(() -> powerMulti /= 2);
    }

    private void decreaseDriveSpeed() {
        powerMulti = Math.max(1.0 - gamepad1.left_trigger, .1);
    }

    @Override
    protected void initAdditionalHardware() {
        intakeChain = new IntakeChain(bot, 200);
        forwardsDepositChain = new ForwardsDepositChain(bot, 500);
        backwardsDepositChain = new BackwardsDepositChain(bot, 500);
    }
}
