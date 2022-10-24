package org.firstinspires.ftc.teamcodekt.opmodes.teleop

import com.outoftheboxrobotics.photoncore.PhotonCore
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@Disabled
@TeleOp(name = "BetterTestOpWithPhotonKt")
class BetterTestOpWithPhoton : BetterTestOp() {
    override fun init() {
        PhotonCore.enable()
        super.init()
    }
}