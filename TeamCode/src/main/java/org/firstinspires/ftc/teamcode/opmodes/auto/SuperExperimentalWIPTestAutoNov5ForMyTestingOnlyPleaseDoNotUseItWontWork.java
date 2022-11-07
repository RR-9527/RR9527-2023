package org.firstinspires.ftc.teamcode.opmodes.auto;

import org.tunableautogen.builder.TuningAutoBuilder;
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

            .temporalMarker(0.5, () -> {
                System.out.println("Hello, world!");
            })

            .finish();
    }
}
