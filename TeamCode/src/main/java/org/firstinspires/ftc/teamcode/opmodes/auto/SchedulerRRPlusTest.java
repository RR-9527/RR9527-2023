package org.firstinspires.ftc.teamcode.opmodes.auto;

import org.firstinspires.ftc.teamcode.opmodes.ScheduleSetup;
import org.firstinspires.ftc.teamcodekt.components.scheduler.ScheduledTask;
import org.firstinspires.ftc.teamcodekt.components.scheduler.Scheduler;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerUnit;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RobotCommon;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.SequenceWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.WrapperBuilder;


public class SchedulerRRPlusTest extends ScheduleSetup {

    @Override
    public void setupCommands() {
        ScheduledTask t1 = Scheduler.scheduleNow(this::move);
        Scheduler.scheduleDuring(t1, this::deployClaw);
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
}
