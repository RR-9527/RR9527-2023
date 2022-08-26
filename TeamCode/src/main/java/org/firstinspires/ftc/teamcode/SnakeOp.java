package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.sun.tools.javac.util.List;

import org.firstinspires.ftc.teamcode.components.snake.Direction;
import org.firstinspires.ftc.teamcode.components.snake.Point;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"SameParameterValue", "SpellCheckingInspection", "ConstantConditions"})
@TeleOp(name = "Snake")
public class SnakeOp extends LinearOpMode {
    public static final int ROWS = 10;
    public static final int COLS = 16;

    public static final int TARGET_FPS = 2;

    private Deque<Point> snake;
    private Direction direction;
    private Point food;

    private boolean boostEnabled;

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

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> game = executor.scheduleAtFixedRate(
            this::update, 0, 1000 / TARGET_FPS, TimeUnit.MILLISECONDS
        );

        if (isStopRequested() || !opModeIsActive()) {
            game.cancel(true);
            executor.shutdown();
        }
    }

    private void update() {
        printBoard();
        setDirectionFromDPad();

        snake.offer(direction.nextHeadPoint(snake.peekLast()));

        if (Objects.equals(snake.peekLast(), food)) {
            food = randPoint(ROWS, COLS);
        } else {
            snake.poll();
        }
    }

    private void setDirectionFromDPad() {
        direction =
            gamepad1.dpad_up ? Direction.UP :
            gamepad1.dpad_down ? Direction.DOWN :
            gamepad1.dpad_left ? Direction.LEFT :
            gamepad1.dpad_right ? Direction.RIGHT :
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

            System.out.println(row);
        }
        System.out.println();
    }

    private Point randPoint(int rows, int cols) {
        return new Point((int) (Math.random() * rows), (int) (Math.random() * cols));
    }
}
