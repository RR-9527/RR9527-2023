package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.taskchains.DepositChain;
import org.firstinspires.ftc.teamcode.components.taskchains.IntakeChain;
import org.firstinspires.ftc.teamcodekt.components.scheduler.TaskChain;

@TeleOp
public class RogueCompOp extends RougeBaseOp {
    TaskChain intakeChain, depositChain;

    @Override
    public void scheduleTasks() {
        codriver.dpad_up   .onRise(lift::goToHigh);
        codriver.dpad_down .onRise(lift::goToRest);
        codriver.dpad_right.onRise(lift::goToMid);
        codriver.dpad_left .onRise(lift::goToLow);

        intakeChain.invokeOn(codriver.left_bumper);
        depositChain.invokeOn(codriver.right_bumper);
    }

    @Override
    protected void initAdditionalHardware() {
        intakeChain = new IntakeChain(bot, 200);
        depositChain = new DepositChain(bot, 500);
    }
}
