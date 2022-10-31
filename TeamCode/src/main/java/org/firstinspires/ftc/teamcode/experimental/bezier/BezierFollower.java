package org.firstinspires.ftc.teamcode.experimental.bezier;

import java.util.ArrayList;

// TODO: Implement path generation functionality later
public class BezierFollower {
    private final Point[] controlPoints;

//    private ArrayList<Point>

    public BezierFollower(Point... controlPoints) {
        this.controlPoints = controlPoints;
    }

    public void generatePath(double resolution) {

    }

    private static Point burp(Point p0, Point p1, double time) {
        return p0.scale(1 - time).add(p1.scale(time));
    }

    private static Point recursiveLerp(Point[] controlPoints, double time) {
        if (controlPoints.length == 2) {
            return burp(controlPoints[0], controlPoints[1], time);
        }

        int idx = 0;
        ArrayList<Point> listOfLerps = new ArrayList<Point>();
        while (idx < controlPoints.length - 1) {
            Point p1 = burp(controlPoints[idx], controlPoints[idx + 1], time);
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
