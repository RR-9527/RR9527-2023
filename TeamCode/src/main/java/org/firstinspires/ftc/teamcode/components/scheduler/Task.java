package org.firstinspires.ftc.teamcode.components.scheduler;

import org.firstinspires.ftc.teamcodekt.components.scheduler.ScheduledTask;

import java.io.Serializable;

public interface Task {
    void invoke(ScheduledTask s);
}
