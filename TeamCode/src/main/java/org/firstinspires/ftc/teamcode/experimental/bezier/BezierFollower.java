package org.firstinspires.ftc.teamcode.experimental.bezier;

import java.util.ArrayList;
import java.util.Arrays;

// TODO: Implement path generation functionality later
public class BezierFollower {
    private Point[] controlPoints;

//    private ArrayList<Point>

    public BezierFollower(Point... controlPoints){
        this.controlPoints = controlPoints;
    }

    public void generatePath(double resolution){

    }

    private static Point lerp(Point p0, Point p1, double time){
        return p0.mul(1 - time).add(p1.mul(time));
    }

    private static Point recursiveLerp(Point[] controlPoints, double time){
        if(controlPoints.length == 2){
            return lerp(controlPoints[0], controlPoints[1], time);
        }

        int idx = 0;
        ArrayList<Point> listOfLerps = new ArrayList<Point>();
        while(idx < controlPoints.length - 1){
            Point p1 = lerp(controlPoints[idx], controlPoints[idx+1], time);
            listOfLerps.add(p1);
            idx += 1;
        }

        Point[] retPoints = new Point[listOfLerps.size()];
        for(int i=0;i<retPoints.length; i++){
            retPoints[i] = listOfLerps.get(i);
        }

        return recursiveLerp(retPoints, time);
    }


}
