package org.firstinspires.ftc.teamcode.components.taskchains;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.components.bot.Bot;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Listener;
import org.firstinspires.ftc.teamcodekt.components.scheduler.TaskChain;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Timer;

import kotlin.jvm.functions.Function0;

public class ForwardsDepositChain implements TaskChain {
    private final Bot bot;
    private final Timer depositTimer;
    private final Timer liftTimer;

    public ForwardsDepositChain(Bot bot, int clawOpeningTime) {
        this.bot = bot;
        this.depositTimer = new Timer(clawOpeningTime);
        this.liftTimer = new Timer(200);
    }

    @Override
    public void invokeOn(@NonNull Listener button) {
        Function0<Boolean> liftIsHighEnough = () -> bot.lift().getHeight() > 500;

        button.and(liftIsHighEnough)
            .onRise(depositTimer::setPending)

            .onFall(() -> {
                bot.claw().openForDeposit();
                depositTimer.start();
            });

        depositTimer
            .whileWaiting(() -> {
                bot.arm().setToForwardsPos();
                bot.wrist().setToForwardsPos();
            })

            .onDone(() -> {
                bot.claw().close();
                liftTimer.start();
            });

        liftTimer
            .onDone(bot.lift()::goToZero);
    }
}
