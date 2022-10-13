package org.firstinspires.ftc.teamcode.TeleOPs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

// this is a test teleop class for testing. Do not use in competition. - Seb on may 7th, 2021.
@TeleOp(name="TestOP")
public class TestOP extends OpMode{
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx leftFront, leftBack, rightFront, rightBack, liftA, liftB, arm;
    private Servo wrist, claw;
    private CRServo intake;
    int armTarget = 0;



    @Override
    public void init(){
        leftFront = (DcMotorEx) hardwareMap.dcMotor.get("FL");
        leftBack = (DcMotorEx) hardwareMap.dcMotor.get("BL");
        rightFront = (DcMotorEx) hardwareMap.dcMotor.get("FR");
        rightBack = (DcMotorEx) hardwareMap.dcMotor.get("BR");

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setDirection(DcMotor.Direction.REVERSE);

        liftA = (DcMotorEx) hardwareMap.dcMotor.get("L1");
        liftB = (DcMotorEx) hardwareMap.dcMotor.get("L2");
        arm = (DcMotorEx) hardwareMap.dcMotor.get("AR");

        liftA.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        liftA.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        liftB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftB.setDirection(DcMotor.Direction.REVERSE);

        intake = hardwareMap.crservo.get("IN");
        claw = hardwareMap.servo.get("CL");
        wrist = hardwareMap.servo.get("WR");
    }
    public void start(){
        arm.setTargetPosition(armTarget);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(.5);
        wrist.setPosition(.82);
    }
    @Override
    public void loop() {
        drive();
        succ();
        armMove();
        lift();
    }

    public void drive() {
        if (Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1) {
            double FLP = gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x;
            double FRP = -gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x;
            double BLP = gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x;
            double BRP = -gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x;
            double max = Math.max(Math.max(Math.abs(FLP), Math.abs(FRP)), Math.max(Math.abs(BLP), Math.abs(BRP)));
            if (max > 1) {
                FLP /= max;
                FRP /= max;
                BLP /= max;
                BRP /= max;
            }
            if (gamepad1.left_trigger > .5) {
                leftFront.setPower(FLP * 0.35);
                rightFront.setPower(FRP * 0.35);
                leftBack.setPower(BLP * 0.35);
                rightBack.setPower(BRP * 0.35);
                telemetry.addData("FrontLeftPow:", FLP * 0.35);
                telemetry.addData("FrontRightPow:", FRP * 0.35);
                telemetry.addData("BackLeftPow:", BLP * 0.35);
                telemetry.addData("BackRightPow:", BRP * 0.35);
            } else {
                leftFront.setPower(FLP);
                rightFront.setPower(FRP);
                leftBack.setPower(BLP);
                rightBack.setPower(BRP);
            }
        } else {
            leftFront.setPower(0);
            rightFront.setPower(0);
            leftBack.setPower(0);
            rightBack.setPower(0);
        }
    }

    private void succ(){
        if (gamepad1.left_trigger > .5) {
            claw.setPosition(.4);
        } else {
            claw.setPosition(.36);
        }

        if (gamepad1.right_trigger > .5){
            intake.setPower(1);
            armTarget = -498;
        } else {
            intake.setPower(0);
            armTarget = 0;
        }

    }

    private void armMove(){
        if(gamepad1.x){
            armTarget = -498;
            arm.setTargetPosition(armTarget);
            arm.setPower(.5);
        } else if (gamepad1.y){
            armTarget = 0;
            arm.setTargetPosition(armTarget);
            arm.setPower(.5);
        } else if (gamepad1.b){
            armTarget = 498;
            arm.setTargetPosition(armTarget);
            arm.setPower(.5);
        }
    }

    private void lift(){
        if(gamepad1.right_bumper) {
            liftA.setPower(.5);
            liftB.setPower(.5);
        } else {
            liftA.setPower(0);
            liftB.setPower(0);
        }
    }

    private void wrist(){
        if(gamepad1.dpad_up){
            wrist.setPosition(.23);
        }
        if(gamepad1.dpad_down){
            wrist.setPosition(.82);
        }
    }

}