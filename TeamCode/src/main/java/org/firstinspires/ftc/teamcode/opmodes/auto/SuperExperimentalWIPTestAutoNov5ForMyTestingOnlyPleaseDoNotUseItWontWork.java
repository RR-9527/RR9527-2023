package org.firstinspires.ftc.teamcode.opmodes.auto;

import org.firstinspires.ftc.teamcodekt.components.scheduler.auto.AutoBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SuperExperimentalWIPTestAutoNov5ForMyTestingOnlyPleaseDoNotUseItWontWork extends RougeBaseAuto {
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

        builder.writeJsonToFile();
    }
}
