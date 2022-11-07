package org.firstinspires.ftc.teamcode.opmodes.auto;

import org.firstinspires.ftc.teamcodekt.components.scheduler.auto.TuningAutoBuilder;
import org.tunableautogen.annotations.GenerateTunableAuto;

public class SuperExperimentalWIPTestAutoNov5ForMyTestingOnlyPleaseDoNotUseItWontWork extends RougeBaseAuto {
    @Override
    public void runOpMode() throws InterruptedException {
        schedulePaths();
        scheduleAuto();
    }

    public void scheduleAuto() {
//        drive.followTrajectorySequenceAsync(new AutoProviderImpl().createAuto(drive));
    }

    @GenerateTunableAuto
    public void schedulePaths() {
        new TuningAutoBuilder()
            .forwardT(10)
            .back(10)
//            .turn(90)
//            .splineTo(10, 10, 0)
//
//            .temporalMarker(0.5, () -> {
//                System.out.println("Hello, world!");
//            })
//
//            .doTimes(3, (iteration, bu1lder) -> bu1lder
//                .forward(10)
//                .temporalMarkerT(() -> {
//                    System.out.println("Hello, world at " + iteration + "!");
//                }))

            .writeJsonToFile();
    }

    public void schedulePathsV2() {

    }
}
