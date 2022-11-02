package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.teleop.RougeBaseOp;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerUnit;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.SequenceWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.WrapperBuilder;
import org.firstinspires.ftc.teamcode.util.OpModeType;
import org.firstinspires.ftc.teamcode.util.RobotConstants;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;

@Autonomous
public class TestAutoNov2 extends RougeBaseOp {
    @Override
    protected void scheduleTasks() {
        opModeType = OpModeType.AUTO;
        runAuto();

    }

    public void runAuto(){
        RoadrunnerWrapper pathing = new RoadrunnerWrapper(hardwareMap, 91, -159, 90, RoadrunnerUnit.CM);
        pathing.sequenceWrapper = new SequenceWrapper(new WrapperBuilder(pathing)
            .forward(135)
            .turn(45)
            .turn(30)

            // Auto Cycle #1
            .setReversed(true)
            .splineTo(150, -30, 0)
            .setReversed(false)
            .splineTo(91, -24, 140)

            // Auto Cycle #2
            .setReversed(true)
            .splineTo(150, -30, 0)
            .setReversed(false)
            .splineTo(91, -24, 140)

            // Auto Cycle #3
            .setReversed(true)
            .splineTo(150, -30, 0)
            .setReversed(false)
            .splineTo(91, -24, 140)



            .turn(-50)
            .back(12)
            // Move to park, TODO: Implement parking in the correct zones


            // Temporal markers:
            .addTemporalMarker(0.5, () -> {
                wrist.setToBackwardsPos();
                wrist.update();

            })

            .addTemporalMarker(2.3, () -> {
                claw.openForDeposit();
            })

            .addTemporalMarker(2.4, () -> {
                claw.openForDeposit();
            })

//            .addTemporalMarker(4, () -> {
//                intake.disable();
//                wrist.setToRestingPos();
//                wrist.update();
//
//                claw.close();
//            })
        );

        pathing.build();
        pathing.follow();
    }

}
