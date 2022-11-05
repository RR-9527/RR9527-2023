package org.firstinspires.ftc.teamcode.components.taskchains;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.components.bot.Bot;
import org.firstinspires.ftc.teamcodekt.components.scheduler.listeners.Listener;
import org.firstinspires.ftc.teamcodekt.components.scheduler.taskchains.TaskChain;
import org.firstinspires.ftc.teamcodekt.components.scheduler.listeners.Timer;

public class IntakeChain implements TaskChain {
    private final Bot bot;
    private final Timer clawTimer;

    public IntakeChain(Bot bot, int clawClosingTime) {
        this.bot = bot;
        this.clawTimer = new Timer(clawClosingTime);
    }

    @Override
    public void invokeOn(@NonNull Listener button) {
        button
            .onRise(() -> {
                bot.intake().enable();
                bot.claw().openForIntake();
                bot.lift().goToZero();
                clawTimer.setPending();
            })

            .onFall(() -> {
                bot.claw().close();
                bot.intake().disable();
                clawTimer.start();
            });

        clawTimer
            .whileWaiting(() -> {
                bot.arm().setToBackwardsPos();
                bot.wrist().setToBackwardsPos();
            })

            .onDone(bot.lift()::goToZero);
    }
}
