package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RobotCommon;
import org.firstinspires.ftc.teamcodekt.components.easytoggle.EasyToggle;


@TeleOp
public class BaseOp extends RobotCommon {
    private ServoEx wrist, claw;
    private CRServo intake;
    private Motor arm, liftA, liftB;

    private EasyToggle toggleA;
    private EasyToggle toggleB;

    /**
     * Override this method and place your code here.
     * <p>
     * Please do not swallow the InterruptedException, as it is used in cases
     * where the op mode needs to be terminated early.
     *
     * @throws InterruptedException
     */
    @Override
    public void runOpMode() throws InterruptedException {
        while(!isStopRequested() && opModeIsActive()){
            updateDrivetrain();
            intake();
            updateArm();
            updateLift();
            updateWrist();
        }
    }

    /**
     * Method to init non-drivetrain hardware before entering the init phase.
     */
    @Override
    protected void initHardware() {
        arm = new Motor(hardwareMap, "AR", Motor.GoBILDA.RPM_84);
        arm.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        arm.setRunMode(Motor.RunMode.PositionControl);

        liftA = new Motor(hardwareMap, "L1", Motor.GoBILDA.RPM_1150);
        liftA.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        liftA.setRunMode(Motor.RunMode.PositionControl);

        liftB = new Motor(hardwareMap, "L2", Motor.GoBILDA.RPM_1150);
        liftB.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        liftB.setRunMode(Motor.RunMode.PositionControl);
        liftB.setInverted(true);

        wrist = new SimpleServo(hardwareMap, "WR", 0, 180, AngleUnit.DEGREES);
        claw = new SimpleServo(hardwareMap, "WR", 0, 180, AngleUnit.DEGREES);
        intake = new CRServo(hardwareMap, "IN");
        toggleA = new EasyToggle(false);
        toggleB = new EasyToggle(false);
    }

    private void intake(){
        if (gamepad1.left_trigger > 0.5) {
            claw.setPosition(Claw.OPEN);
        } else {
            claw.setPosition(Claw.CLOSE);
        }

        if (gamepad1.right_trigger > 0.5){
            intake.set(1);
        } else {
            intake.set(0);
        }
    }

    private void updateArm(){
        if(game_pad1.getButton(GamepadKeys.Button.X)){
            arm.setTargetPosition(Arm.INTAKE_POS);
            arm.set(.7);
        } else if (game_pad1.getButton(GamepadKeys.Button.Y)){
            arm.setTargetPosition(Arm.VERTICAL);
            arm.set(.7);
        } else if (game_pad1.getButton(GamepadKeys.Button.B)){
            arm.setTargetPosition(Arm.DEPOSIT_POS);
            arm.set(.7);
        }
    }

    private void updateLift(){
        if(game_pad1.getButton(GamepadKeys.Button.RIGHT_BUMPER)) {
            liftA.set(Lift.UP);
            liftB.set(Lift.UP);
        } else {
            liftA.set(Lift.DOWN);
            liftB.set(Lift.DOWN);
        }
    }

    private void updateWrist(){
        if(game_pad1.getButton(GamepadKeys.Button.DPAD_UP)){
            wrist.setPosition(Wrist.INTAKE_POS);
        }
        if(game_pad1.getButton(GamepadKeys.Button.DPAD_DOWN)){
            wrist.setPosition(Wrist.DEPOSIT_POS);
        }
    }
}
