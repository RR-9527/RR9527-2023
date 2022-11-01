package org.firstinspires.ftc.teamcode.experimental.bezier;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;

public class Point {
    public double x;
    public double y;
    public double angle;

    public Point(double x, double y) {
        this(x, y, -0.0);
    }

    public Point(double x, double y, double angleInDeg){
        this.x = x;
        this.y = y;
        this.angle = angleInDeg;
    }

    public Point add(Point other) {
        return new Point(x + other.x, y + other.y);
    }

    public Point mul(Point other) {
        return new Point(x * other.x, y * other.y);
    }

    public Point scale(double constant) {
        return new Point(x * constant, y * constant);
    }

    public Pose2d toPose(){
        return new Pose2d(x, y, Rotation2d.fromDegrees(0));
    }
}
