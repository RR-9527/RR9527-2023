package org.firstinspires.ftc.teamcode.components.taskchains;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.components.bot.Bot;
import org.firstinspires.ftc.teamcodekt.components.scheduler.taskchains.CancellableTaskChain;
import org.firstinspires.ftc.teamcodekt.components.scheduler.listeners.Listener;
import org.firstinspires.ftc.teamcodekt.components.scheduler.listeners.Timer;

public class BackwardsDepositChain implements CancellableTaskChain {
    private final Bot bot;
    private final Timer depositTimer;
    private final Timer liftTimer;

    private boolean isCancelled = false;

    public BackwardsDepositChain(Bot bot, int clawOpeningTime) {
        this.bot = bot;
        this.depositTimer = new Timer(clawOpeningTime);
        this.liftTimer = new Timer(200);
    }

    @Override
    public void invokeOn(@NonNull Listener button) {
        button
            .onRise(depositTimer::setPending)
            .onRise(() -> isCancelled = false)

            .onFall(() -> {
                if (!isCancelled) {
                    bot.claw().openForDeposit();
                    depositTimer.start();
                } else {
                    depositTimer.finishPrematurely();
                }
            });

        depositTimer
            .whileWaiting(() -> {
                if (!isCancelled) {
                    bot.arm().setToBackwardsPos();
                    bot.wrist().setToBackwardsPos();
                }
            })

            .onDone(() -> {
                bot.claw().close();
                liftTimer.start();
            });

        liftTimer
            .onDone(bot.lift()::goToZero);
    }

    @Override
    public void cancelOn(@NonNull Listener button) {
        button.onRise(() -> isCancelled = true);
    }
}
