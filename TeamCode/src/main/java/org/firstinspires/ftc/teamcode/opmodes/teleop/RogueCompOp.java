package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class RogueCompOp extends RougeBaseOp {
    @Override
    public void scheduleTasks() {
        // Lift: increment up and down with button presses
        codriver.dpad_up   .onRise(lift::goToHigh);
        codriver.dpad_down .onRise(lift::goToRest);
        codriver.dpad_right.onRise(lift::goToMid);
        codriver.dpad_left .onRise(lift::goToLow);

        // Intake chain:
        intakeChain(codriver.left_bumper, 200);

        // Deposit chain:
        depositChain(codriver.right_bumper, 400);

        driver.joysticks(.1).whileHigh(this::drive);
    }
}
