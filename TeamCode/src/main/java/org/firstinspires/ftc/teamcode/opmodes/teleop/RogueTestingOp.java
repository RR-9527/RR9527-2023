package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.taskchains.DepositChain;
import org.firstinspires.ftc.teamcode.components.taskchains.IntakeChain;
import org.firstinspires.ftc.teamcode.util.StateRotator;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveType;
import org.firstinspires.ftc.teamcodekt.components.scheduler.TaskChain;

@TeleOp
public class RogueTestingOp extends RougeBaseOp {
    StateRotator<DriveType> driveTypes;

    TaskChain intakeChain, depositChain;

    @Override
    public void scheduleTasks() {
        driver.dpad_up   .onRise(lift::goToHigh);
        driver.dpad_down .onRise(lift::goToRest);
        driver.dpad_right.onRise(lift::goToMid);
        driver.dpad_left .onRise(lift::goToLow);

        intakeChain.invokeOn(driver.left_bumper);

        depositChain.invokeOn(driver.right_bumper);

        driver.a.onRise(rotateDriveType);
    }

    protected final Runnable rotateDriveType = () -> {
        driveType = driveTypes.next();
    };

    @Override
    protected void initAdditionalHardware() {
        driveTypes = new StateRotator<>(DriveType.NORMAL, DriveType.IMPROVED, DriveType.FIELD_CENTRIC);
        intakeChain = new IntakeChain(bot, 200);
        depositChain = new DepositChain(bot, 500);
    }
}
