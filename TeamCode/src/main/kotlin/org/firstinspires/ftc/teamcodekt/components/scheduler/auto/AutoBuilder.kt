package org.firstinspires.ftc.teamcodekt.components.scheduler.auto

import com.acmerobotics.roadrunner.trajectory.MarkerCallback
import org.firstinspires.ftc.teamcodekt.util.toIn
import org.firstinspires.ftc.teamcodekt.util.toRad

data class Action(
    val index: Int,
    val method_name: String,
    val tag: Any,
    val args: Map<String, Any>
)

class AutoBuilder {
    private var variables = mutableListOf<Action>()


    private var forwardCalls = 0

    @JvmOverloads
    fun forward(distance: Double, tag: Any? = null) = this.apply {
        forwardCalls++
        variables.addAction("forward", tag ?: forwardCalls, mapOf("distance" to distance.toIn()))
    }


    private var backCalls = 0

    @JvmOverloads
    fun back(distance: Double, tag: Any? = null) = this.apply {
        backCalls++
        variables.addAction("back", tag ?: backCalls, mapOf("distance" to distance.toIn()))
    }


    private var turnCalls = 0

    @JvmOverloads
    fun turn(angle: Double, tag: Any? = null) = this.apply {
        turnCalls++
        variables.addAction("turn", tag ?: turnCalls, mapOf("angle" to angle.toRad()))
    }


    private var splineToCalls = 0

    @JvmOverloads
    fun splineTo(x: Double, y: Double, heading: Double, tag: Any? = null) = this.apply {
        val vars = mapOf("x" to x.toIn(), "y" to y.toIn(), "heading" to heading.toRad())

        splineToCalls++
        variables.addAction("splineTo", tag ?: splineToCalls, vars)
    }


    private var waitSecondsCalls = 0

    @JvmOverloads
    fun waitSeconds(time: Double, tag: Any? = null) = this.apply {
        waitSecondsCalls++
        variables.addAction("waitSeconds", tag ?: waitSecondsCalls, mapOf("time" to time))
    }


    private var setReversedCalls = 0

    @JvmOverloads
    fun setReversed(reversed: Boolean, tag: Any? = null) = this.apply {
        setReversedCalls++
        variables.addAction("setReversed", tag ?: setReversedCalls, mapOf("reversed" to reversed))
    }


    private var temporalMarkerCalls = 0

    @JvmOverloads
    fun temporalMarker(offset: Double = 0.0, action: MarkerCallback, tag: Any? = null) = this.apply {
        val vars = mapOf("offset" to offset, "action" to action)

        temporalMarkerCalls++
        variables.addAction("temporalMarker", tag ?: temporalMarkerCalls, vars)
    }


    interface AutoBuilderRepeatCallback {
        fun callback(iterationNum: Int, builder: AutoBuilder)
    }

    fun doTimes(times: Int, callback: AutoBuilderRepeatCallback) = this.apply {
        for (i in 0 until times) {
            callback.callback(i, this)
        }
    }

    fun toJSON() = buildString {
        append('[')
        variables.forEachIndexed { index, action ->
            append("{")
            append("\"index\": ${action.index},")
            append("\"method_name\": \"${action.method_name}\",")
            append("\"tag\": \"${action.tag}\",")
            append("\"args\": {")
            action.args.forEach { (key, value) ->
                append("\"$key\": $value,")
            }
            deleteCharAt(lastIndex)
            append("}")
            append("}")
            if (index != variables.size - 1) {
                append(',')
            }
        }
        append(']')
    }

    fun toPrettyJSON() = buildString {
        appendLine('[')
        variables.forEachIndexed { index, action ->
            appendLine("""
                {
                    "index": ${action.index},
                    "method_name": "${action.method_name}",
                    "tag": "${action.tag}",
                    "args": {
                        ${action.args.map { (key, value) -> "\"$key\": $value" }.joinToString(",\n                        ")}
                    }
                }${if (index != variables.size - 1) "," else ""}
            """.replaceIndent("    "))
        }
        appendLine(']')
    }

    private fun MutableList<Action>.addAction(
        method_name: String,
        tag: Any,
        args: Map<String, Any>
    ) {
        this.add(Action(size, method_name, tag, args))
    }
}
