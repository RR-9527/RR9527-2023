package org.firstinspires.ftc.teamcode.opmodes.auto;

import org.firstinspires.ftc.teamcodekt.components.scheduler.ScheduledTask;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerUnit;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.SequenceWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.WrapperBuilder;


public class SchedulerRRPlusTest extends ScheduledOpMode {

    @Override
    public void setupCommands() {

    }

    public void move(ScheduledTask scheduledTask){
        RoadrunnerWrapper pathing = new RoadrunnerWrapper(hardwareMap, RoadrunnerUnit.CM);
        pathing.sequenceWrapper = new SequenceWrapper(new WrapperBuilder(pathing)
                .lineToLinearHeading(50, 20, 90)
                .strafeLeft(10)
                .forward(10)
                .lineToSplineHeading(50, 20, 0)
        );
        pathing.build();
        pathing.follow();

    }

    public void deployClaw(ScheduledTask scheduledTask) {

    }

    @Override
    protected void initHardware() {

    }
}
