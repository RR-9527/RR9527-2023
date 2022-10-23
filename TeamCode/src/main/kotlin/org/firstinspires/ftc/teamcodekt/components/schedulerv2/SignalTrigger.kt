package org.firstinspires.ftc.teamcodekt.components.schedulerv2

/**
 * Represents the four possible (non-mutually-exclusive) states of a signal. For use with the
 * [Trigger] class.
 *
 * @author KG
 *
 * @see SignalEdgeDetector
 * @see Trigger
 */
enum class SignalTrigger {
    RISING_EDGE, FALLING_EDGE, IS_HIGH, IS_LOW
}