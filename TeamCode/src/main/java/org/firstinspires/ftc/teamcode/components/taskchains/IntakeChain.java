package org.firstinspires.ftc.teamcode.components.taskchains;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.components.bot.Bot;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Listener;
import org.firstinspires.ftc.teamcodekt.components.scheduler.TaskChain;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Timer;

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
            .onRise(bot.intake()::enable)
            .onRise(bot.claw()::openForIntake)
            .onRise(bot.lift()::goToZero)

            .whileHigh(bot.arm()::setToIntakePos)
            .whileHigh(bot.wrist()::setToIntakePos)

            .onFall(clawTimer::reset)
            .onFall(bot.claw()::close)
            .onFall(bot.intake()::disable);

        clawTimer
            .whileWaiting(bot.arm()::setToIntakePos)
            .whileWaiting(bot.wrist()::setToIntakePos)

            .onDone(bot.lift()::goToRest);
    }
}
