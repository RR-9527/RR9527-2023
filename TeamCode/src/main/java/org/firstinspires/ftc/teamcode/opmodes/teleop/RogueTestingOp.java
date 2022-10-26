package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.StateRotator;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveType;

@TeleOp
public class RogueTestingOp extends RougeBaseOp {
    StateRotator<DriveType> driveTypes;

    @Override
    public void scheduleTasks() {
        // Lift: increment up and down with button presses
        driver.dpad_up   .onRise(lift::goToHigh);
        driver.dpad_down .onRise(lift::goToRest);
        driver.dpad_right.onRise(lift::goToMid);
        driver.dpad_left .onRise(lift::goToLow);

        // Intake chain:
        intakeChain(driver.left_bumper, 200);

        // Deposit chain:
        depositChain(driver.right_bumper, 400);

        // Drive:
        driver.a.onRise(rotateDriveType);

        driver.joysticks(.1).whileHigh(this::drive);
    }

    protected final Runnable rotateDriveType = () -> {
        driveType = driveTypes.next();
    };

    @Override
    protected void initHardware() {
        super.initHardware();
        driveTypes = new StateRotator<>(DriveType.NORMAL, DriveType.IMPROVED, DriveType.FIELD_CENTRIC);
    }
}
