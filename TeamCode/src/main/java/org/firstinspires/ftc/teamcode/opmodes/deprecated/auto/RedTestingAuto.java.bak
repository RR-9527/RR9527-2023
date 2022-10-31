package org.firstinspires.ftc.teamcode.opmodes.deprecated.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerUnit;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RobotCommon;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.SequenceWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.WrapperBuilder;

@Autonomous
public class RedTestingAuto extends RobotCommon {
    @Override
    public void runOpMode() throws InterruptedException {
        RoadrunnerWrapper pathing = new RoadrunnerWrapper(hardwareMap, 91, -159, 90, RoadrunnerUnit.CM);
        pathing.sequenceWrapper = new SequenceWrapper(new WrapperBuilder(pathing)
            .splineTo(91, -30, 90)
            .turn(-135)
            // Deposit
            .turn(30)

            // First auto cycle
            .splineToSplineHeading(140, -33, 0, 0)

            .setReversed(true)
            .splineTo(91, -30, 160)
            .setReversed(false)
            .turn(-25)
            // Deposit

        );
        pathing.build();
        pathing.follow();
    }

    @Override
    protected void initHardware() {

    }
}
