package org.firstinspires.ftc.teamcode.opmodes.auto;

import org.firstinspires.ftc.teamcodekt.components.scheduler.auto.deprecated.TuningAutoBuilder;

public class SuperExperimentalWIPTestAutoNov5ForMyTestingOnlyPleaseDoNotUseItWontWork extends RougeBaseAuto {
    @Override
    public void runOpMode() throws InterruptedException {
        schedulePaths();
    }

    public void schedulePaths() {
        new TuningAutoBuilder()
            .forward(10)
            .turn(90)
            .splineTo(10, 10, 0)

            .temporalMarker(0.5, () -> {
                System.out.println("Hello, world!");
            })

            .doTimes(3, (iteration, bu1lder) -> bu1lder
                .forward(10)
                .temporalMarker(() -> {
                    System.out.println("Hello, world at " + iteration + "!");
                }))

            .writeJsonToFile();
    }
}
