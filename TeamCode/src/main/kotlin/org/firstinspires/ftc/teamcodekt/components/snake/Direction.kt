package org.firstinspires.ftc.teamcodekt.components.snake

enum class Direction {
    UP {
        override fun nextHeadPoint(head: Point) =
            Point(head.x, head.y - 1)
    },
    DOWN {
        override fun nextHeadPoint(head: Point) =
            Point(head.x, head.y + 1)
    },
    LEFT {
        override fun nextHeadPoint(head: Point) =
            Point(head.x - 1, head.y)
    },
    RIGHT {
        override fun nextHeadPoint(head: Point) =
            Point(head.x + 1, head.y)
    };

    abstract fun nextHeadPoint(head: Point): Point
}
