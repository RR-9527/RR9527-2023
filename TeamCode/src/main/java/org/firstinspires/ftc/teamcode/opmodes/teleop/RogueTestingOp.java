package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.taskchains.BackwardsDepositChain;
import org.firstinspires.ftc.teamcode.components.taskchains.ForwardsDepositChain;
import org.firstinspires.ftc.teamcode.components.taskchains.IntakeChain;
import org.firstinspires.ftc.teamcode.util.StateRotator;
import org.firstinspires.ftc.teamcodekt.components.motors.DriveType;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Listener;
import org.firstinspires.ftc.teamcodekt.components.scheduler.TaskChain;

@TeleOp
public class RogueTestingOp extends RougeBaseOp {
    StateRotator<DriveType> driveTypes;
    TaskChain intakeChain, forwardsDepositChain, backwardsDepositChain;

    @Override
    public void scheduleTasks() {
        driver.dpad_up   .onRise(lift::goToHigh);
        driver.dpad_down .onRise(lift::goToRest);
        driver.dpad_right.onRise(lift::goToMid);
        driver.dpad_left .onRise(lift::goToLow);

        intakeChain.invokeOn(driver.left_bumper);
        forwardsDepositChain.invokeOn(driver.right_bumper);
        backwardsDepositChain.invokeOn(driver.y);

        codriver.right_stick_x(.1).whileHigh(() -> {
            if (gamepad2.right_stick_x > 0) {
                lift.setHeight(lift.getHeight() + 10);
            } else {
                lift.setHeight(lift.getHeight() - 10);
            }
        });

        driver.a.onRise(this::rotateDriveType);

        driver.left_trigger.whileHigh(this::decreaseDriveSpeed);

        new Listener(() -> lift.getHeight() > 1000)
            .whileHigh(() -> powerMulti = .1);
    }

    private void rotateDriveType() {
        driveMotors.setDriveType(driveTypes.next());
    }

    private void decreaseDriveSpeed() {
        powerMulti = Math.max(1.0 - gamepad1.left_trigger, .1);
    }

    @Override
    protected void initAdditionalHardware() {
        driveTypes = new StateRotator<>(DriveType.NORMAL, DriveType.IMPROVED, DriveType.FIELD_CENTRIC);
        intakeChain = new IntakeChain(bot, 200);
        forwardsDepositChain = new ForwardsDepositChain(bot, 500);
        backwardsDepositChain = new BackwardsDepositChain(bot, 500);
    }
}
