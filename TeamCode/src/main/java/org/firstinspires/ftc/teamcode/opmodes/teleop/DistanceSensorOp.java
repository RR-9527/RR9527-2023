package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.distancesensor.ShortRangeSensor;

@TeleOp
public class DistanceSensorOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        ShortRangeSensor front = new ShortRangeSensor(hardwareMap, "F_USDS");

        waitForStart();

        while(!isStopRequested() && opModeIsActive()){
            telemetry.addData("front distance sensor", front.getDistance());
            telemetry.update();
        }
    }
}
