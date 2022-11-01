package org.firstinspires.ftc.teamcode.experimental.bezier;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.OdometrySubsystem;
import com.arcrobotics.ftclib.command.PurePursuitCommand;
import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.purepursuit.Waypoint;
import com.arcrobotics.ftclib.purepursuit.waypoints.EndWaypoint;
import com.arcrobotics.ftclib.purepursuit.waypoints.GeneralWaypoint;
import com.arcrobotics.ftclib.purepursuit.waypoints.StartWaypoint;

import java.util.ArrayList;

import kotlin.jvm.functions.Function2;

public abstract class BezierFollower extends CommandOpMode {
    private Point[] controlPoints;
    private Function2<Double, Double, Double> velocityFunction;
    private Function2<Double, Double, Double> angularFunction;

    private ArrayList<Waypoint> pathPoints;

    private OdometrySubsystem odometrySubsystem;
    private MecanumDrive drive;

    private PurePursuitCommand command;

    /**
     * Create a follower object using FTCLib's Pure Pursuit library
     * @param controlPoints
     */
    public void init_bezier(final OdometrySubsystem odometrySubsystem, final MecanumDrive drive, final Function2<Double, Double, Double> velocityFunction, final Function2<Double, Double, Double> angularFunction, final Point... controlPoints) {
        this.odometrySubsystem = odometrySubsystem;
        this.drive = drive;


        this.velocityFunction = velocityFunction;
        this.angularFunction = angularFunction;
        this.controlPoints = controlPoints;

        pathPoints = new ArrayList<>();
    }



    public void generatePath(final double numPoints, final double maxV, final double maxAngV) {
        for(double t=0;t<=1;t+=1/numPoints){
            Point p = recursiveLerp(controlPoints, t);

            double mvmtSpeed = velocityFunction.invoke(t, maxV);
            double angSpeed = velocityFunction.invoke(t, maxAngV);

            if(t == 0)
                pathPoints.add(new StartWaypoint(p.toPose()));
            else if(t == 1)
                pathPoints.add(new EndWaypoint(p.toPose(), 0, 0, 0, 0.05, 0.01));
            else
                pathPoints.add(new GeneralWaypoint(p.toPose(), mvmtSpeed, angSpeed, -0.0));
        }
        Waypoint[] pointsInArray = new Waypoint[pathPoints.size()];
        for(int i=0;i<pointsInArray.length;i++)
            pointsInArray[i] = pathPoints.get(i);

        command = new PurePursuitCommand(drive, odometrySubsystem, pointsInArray);
    }

    public void run(){
        schedule(command);
    }

    private static Point lerp(final Point p0, final Point p1, final double time) {
        return p0.scale(1 - time).add(p1.scale(time));
    }

    private static Point recursiveLerp(Point[] controlPoints, final double time) {
        if (controlPoints.length == 2) {
            return lerp(controlPoints[0], controlPoints[1], time);
        }

        int idx = 0;
        ArrayList<Point> listOfLerps = new ArrayList<Point>();
        while (idx < controlPoints.length - 1) {
            Point p1 = lerp(controlPoints[idx], controlPoints[idx + 1], time);
            listOfLerps.add(p1);
            idx += 1;
        }

        Point[] retPoints = new Point[listOfLerps.size()];
        for (int i = 0; i < retPoints.length; i++) {
            retPoints[i] = listOfLerps.get(i);
        }

        return recursiveLerp(retPoints, time);
    }
}
