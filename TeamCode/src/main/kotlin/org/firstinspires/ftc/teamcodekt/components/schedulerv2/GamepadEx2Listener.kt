package org.firstinspires.ftc.teamcodekt.components.schedulerv2

import org.firstinspires.ftc.teamcode.components.scheduler.Action

interface GamepadEx2Listener {
    fun onTriggered(action: Action)
    fun onUntriggered(action: Action)
    fun whileTriggered(action: Action)
}
