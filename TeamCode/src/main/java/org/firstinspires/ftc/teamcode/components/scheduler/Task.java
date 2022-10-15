package org.firstinspires.ftc.teamcode.components.scheduler;

import org.firstinspires.ftc.teamcodekt.components.scheduler.ScheduledTask;

import java.io.Serializable;

public interface Task extends Serializable {
    void invoke(ScheduledTask s);
}
