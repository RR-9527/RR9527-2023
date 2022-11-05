package org.firstinspires.ftc.teamcode.opmodes.auto;

import org.firstinspires.ftc.teamcodekt.components.scheduler.auto.AutoBuilder;

public class SuperExperimentalWIPTestAutoNov5ForMyTestingOnlyPleaseDoNotUseItWontWork extends RougeBaseAuto {
    public static void main(String[] args) {
        new SuperExperimentalWIPTestAutoNov5ForMyTestingOnlyPleaseDoNotUseItWontWork().schedulePaths();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        schedulePaths();
    }

    public void schedulePaths() {
        AutoBuilder builder = new AutoBuilder();
        builder
            .forward(10)
            .turn(90)
            .splineTo(10, 10, 0);

        System.out.println(builder.toJSON());
    }
}
