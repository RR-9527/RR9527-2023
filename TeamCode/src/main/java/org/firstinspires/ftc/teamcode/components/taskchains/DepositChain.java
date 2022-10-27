package org.firstinspires.ftc.teamcode.components.taskchains;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.components.bot.Bot;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Listener;
import org.firstinspires.ftc.teamcodekt.components.scheduler.TaskChain;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Timer;

public class DepositChain implements TaskChain {
    private final Bot bot;
    private final Timer depositTimer;

    public DepositChain(Bot bot, int clawOpeningTime) {
        this.bot = bot;
        this.depositTimer = new Timer(clawOpeningTime);
    }

    @Override
    public void invokeOn(@NonNull Listener button) {
        button
            .whileHigh(bot.arm()::setToDepositPos)
            .whileHigh(bot.wrist()::setToDepositPos)

            .onFall(depositTimer::reset)
            .onFall(bot.claw()::openForDeposit);

        depositTimer
            .whileWaiting(bot.arm()::setToDepositPos)
            .whileWaiting(bot.wrist()::setToDepositPos)

            .onDone(bot.claw()::close);
    }
}
