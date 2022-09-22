package org.firstinspires.ftc.teamcode.nevergonnagiveyouupnevergonnaletyoudown

import com.outoftheboxrobotics.photoncore.PhotonCore
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "BetterTestOpWithPhotonKt")
class BetterTestOpWithPhoton : BetterTestOp() {
    override fun init() {
        PhotonCore.enable()
        super.init()
    }
}