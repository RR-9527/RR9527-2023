package org.firstinspires.ftc.teamcode.experimental.bezier;

public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
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
}
