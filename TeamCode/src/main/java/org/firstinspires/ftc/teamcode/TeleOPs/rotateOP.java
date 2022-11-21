package org.firstinspires.ftc.teamcode.TeleOPs;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


// this is a test teleop class for testing. Do not use in competition. - Seb on may 7th, 2021.
@TeleOp(name="DebugOp")
public class rotateOP extends OpMode{
    private Servo wrist;




    @Override
    public void init(){

        wrist = hardwareMap.servo.get("WR");





    }

    @Override
    public void loop() {
       wrist();
    }

    private void wrist(){
    if(gamepad1.dpad_up){
            wrist.setPosition(.21);
        }
        if(gamepad1.dpad_down){
           wrist.setPosition(.84);
        }
    }
}