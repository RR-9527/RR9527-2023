package org.firstinspires.ftc.teamcode.components.taskchains;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.components.bot.Bot;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Listener;
import org.firstinspires.ftc.teamcodekt.components.scheduler.TaskChain;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Timer;

public class BackwardsDepositChain implements TaskChain {
    private final Bot bot;
    private final Timer depositTimer;

    public BackwardsDepositChain(Bot bot, int clawOpeningTime) {
        this.bot = bot;
        this.depositTimer = new Timer(clawOpeningTime);
    }

    @Override
    public void invokeOn(@NonNull Listener button) {
        button
            .onRise(depositTimer::setPending)

            .onFall(() -> {
                depositTimer.setPending();
                bot.claw().openForDeposit();
            });

        depositTimer
            .whileWaiting(() -> {
                bot.arm().setToBackwardsPos();
                bot.wrist().setToBackwardsPos();
            })

            .onDone(bot.claw()::close);
    }
}
