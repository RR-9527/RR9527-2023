package org.firstinspires.ftc.teamcode.components.gamepad

import com.qualcomm.robotcore.hardware.Gamepad
import kotlin.math.abs

fun Gamepad.isJoystickTriggered() = listOf(left_stick_y, left_stick_x, right_stick_x, right_stick_y)
    .any { abs(it) > 0.1 }

fun Gamepad.getDriveSticks(): List<Float> = listOf(-left_stick_y, left_stick_x, right_stick_x)