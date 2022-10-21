package org.firstinspires.ftc.teamcode.opmodes.teleop.deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.sun.tools.javac.util.List;

import org.firstinspires.ftc.teamcodekt.components.snake.Direction;
import org.firstinspires.ftc.teamcodekt.components.snake.Point;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Disabled
@SuppressWarnings({"SameParameterValue", "ConstantConditions"})
@TeleOp(name = "Snake")
public class SnakeOp extends LinearOpMode {
    public static final int ROWS = 10;
    public static final int COLS = 16;

    public static final double TARGET_FPS = 1.5;

    private Deque<Point> snake;
    private Point food;

    private Direction direction;
    private int score;

    @Override
    public void runOpMode() {
        snake = new ArrayDeque<>(List.of(
            new Point(1, 1),
            new Point(2, 1),
            new Point(3, 1),
            new Point(4, 1)
        ));
        direction = Direction.RIGHT;
        food = randPoint(ROWS, COLS);

        waitForStart();

        printBoard();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> game = executor.scheduleAtFixedRate(
            this::update, 0, (long) (1000 / TARGET_FPS), TimeUnit.MILLISECONDS
        );

        if (isStopRequested() || !opModeIsActive()) {
            game.cancel(true);
            executor.shutdown();
        }
    }

    private void update() {
        setDirectionFromDPad();

        snake.offer(direction.nextHeadPoint(snake.peekLast()));

        if (food.equals(snake.peekLast())) {
            food = randPoint(ROWS, COLS);
            score += 50;
        } else {
            snake.poll();
            score += 5;
        }

        printBoard();

        if (!inBounds() || !isDisjoint()) {
            System.out.println("Game over!");
            System.exit(0);
        }
    }

    private boolean inBounds() {
        return snake.stream()
            .allMatch(point ->
                point.getX() >= 0 && point.getX() < COLS &&
                point.getY() >= 0 && point.getY() < ROWS);
    }

    private boolean isDisjoint() {
        return snake.stream()
            .distinct()
            .count() == snake.size();
    }

    private void setDirectionFromDPad() {
        direction =
            gamepad1.dpad_up && direction != Direction.DOWN ? Direction.UP :
            gamepad1.dpad_down && direction != Direction.UP ? Direction.DOWN :
            gamepad1.dpad_left && direction != Direction.RIGHT ? Direction.LEFT :
            gamepad1.dpad_right && direction != Direction.LEFT ? Direction.RIGHT :
            direction;
    }

    private void printBoard() {
        for (int y = 0; y < ROWS; y++) {
            StringBuilder row = new StringBuilder();

            for (int x = 0; x < COLS; x++) {
                if (snake.contains(new Point(x, y))) {
                    row.append("O");
                } else if (Objects.equals(new Point(x, y), food)) {
                    row.append("X");
                } else {
                    row.append(".");
                }
            }

            telemetry.addLine(row.toString());
        }
        telemetry.update();
    }

    private Point randPoint(int rows, int cols) {
        Point p;
        do {
            p = new Point((int) (Math.random() * rows), (int) (Math.random() * cols));
        } while (snake.contains(p));
        return p;
    }
}
