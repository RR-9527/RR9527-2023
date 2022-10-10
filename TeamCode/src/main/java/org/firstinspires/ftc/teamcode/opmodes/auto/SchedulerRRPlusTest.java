package org.firstinspires.ftc.teamcode.opmodes.auto;

import org.firstinspires.ftc.teamcode.opmodes.scheduler.ScheduledTask;
import org.firstinspires.ftc.teamcode.opmodes.scheduler.Scheduler;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerUnit;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RoadrunnerWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.RobotCommon;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.SequenceWrapper;
import org.firstinspires.ftc.teamcode.roadrunner.roadrunnerplus.WrapperBuilder;

import kotlin.Unit;

public class SchedulerRRPlusTest extends RobotCommon {
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
        Scheduler scheduler = Scheduler.INSTANCE;

        initialize();

        ScheduledTask t1 = scheduler.schedule(this::move);
        scheduler.scheduleDuring(t1, this::deployClaw);

        scheduler.run(this);

    }

    public Unit move(ScheduledTask scheduledTask){
        RoadrunnerWrapper pathing = new RoadrunnerWrapper(hardwareMap, RoadrunnerUnit.CM);
        pathing.sequenceWrapper = new SequenceWrapper(new WrapperBuilder(pathing)
                .lineToLinearHeading(50, 20, 90)
                .strafeLeft(10)
                .forward(10)
                .lineToSplineHeading(50, 20, 0)
        );
        pathing.build();
        pathing.follow();

        return null;
    }

    public Unit deployClaw(ScheduledTask scheduledTask){
        return null;
    }
}
