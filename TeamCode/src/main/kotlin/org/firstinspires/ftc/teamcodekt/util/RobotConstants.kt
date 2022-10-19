package org.firstinspires.ftc.teamcodekt.util

object Claw {
    const val OPEN = 0.65
    const val CLOSE = 0.52
}

object Arm {
    const val INTAKE_POS = 480
    const val VERTICAL = 0
    const val DEPOSIT_POS = -480
    const val P = 0.1
    const val I = 0.1
    const val D = 0.1
    const val F = 0.1
}

object Wrist {
    const val INTAKE_POS = 0.21
    const val DEPOSIT_POS = 0.84
}

object Lift {
    const val UP = 0.8
    const val DOWN = 0.0

    object A {
        const val P = 0.5
        const val I = 0.5
        const val D = 0.5
        const val F = 0.5
    }
    object B {
        const val P = 0.5
        const val I = 0.5
        const val D = 0.5
        const val F = 0.5
    }
}

object TriggerData {
    // Minimum necessary trigger press before activating a function
    const val TRIGGER_THRESHOLD = 0.5
}


