package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.teleop.RougeBaseOp;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerUnit;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.SequenceWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.WrapperBuilder;
import org.firstinspires.ftc.teamcode.util.OpModeType;

@Autonomous
public class TestAutoNov2 extends RougeBaseOp {
    @Override
    protected void scheduleTasks() {
        opModeType = OpModeType.AUTO;
        runAuto();
    }

    public void runAuto(){
        RoadrunnerWrapper pathing = new RoadrunnerWrapper(hardwareMap, 91, -159, -90, RoadrunnerUnit.CM);
        pathing.sequenceWrapper = new SequenceWrapper(new WrapperBuilder(pathing)
            .back(135)

            // TODO: Uncomment the temporal marker blocks to test intaking and depositing
//            .UNSTABLE_addTemporalMarkerOffset(0, () -> {
//                // TODO: Find out if these markers run at the beginning of the previous command,
//                //  or at the end. I think it's at the end
//                // Extend lift and arm
//                wrist.setToBackwardsPos();
//                lift.goToHigh();
//                arm.setToBackwardsPos();
//            })

//            .UNSTABLE_addTemporalMarkerOffset(0.5, () -> { // TODO: Tune the delay here, or put after turn command
//                // Deposit, then wait a tenth of a second before bringing lift down
//                claw.openForDeposit();
//            })
            .turn(-135)
            // Actually deposits while at the end of the turn

//            .UNSTABLE_addTemporalMarkerOffset(0.25, () -> { // TODO: Tune delay here
//                // Bring lift down and reset for intaking
//                claw.openForIntake();
//                lift.goToZero();
//                arm.setToForwardsPos();
//                wrist.setToForwardsPos();
//            })

            // Auto Cycle #1
            .setReversed(true)
//            .UNSTABLE_addTemporalMarkerOffset(0.5, () -> { // TODO: Tune delay here
//                // Intake - TODO: Implement setHeight for the lift to intake at the right position
//                claw.openForIntake();
//                intake.enable();
//            })
            .splineTo(145, -30.5, 0)
            // Actually intakes around here

//            .UNSTABLE_addTemporalMarkerOffset(0.25, () -> { // TODO: Tune delay here
//                // Extend lift and arm
//                wrist.setToBackwardsPos();
//                lift.goToHigh();
//                arm.setToBackwardsPos();
//            })
            .setReversed(false)

//            .UNSTABLE_addTemporalMarkerOffset(0.5, () -> { // TODO: Tune delay here
//                // Deposit, then wait a tenth of a second before bringing lift down
//                claw.openForDeposit();
//            })

            .splineTo(91, -24, 140)
            // Actually deposits around here

//            .UNSTABLE_addTemporalMarkerOffset(0.1, () -> {
//                // Bring lift down and reset for intaking
//                claw.openForIntake();
//                lift.goToZero();
//                arm.setToForwardsPos();
//                wrist.setToRestingPos();
//            })



            .turn(-50)
            .back(70)
            // Move to park, TODO: Implement parking in the correct zones

        );

        pathing.build();
        pathing.follow();
    }

}
