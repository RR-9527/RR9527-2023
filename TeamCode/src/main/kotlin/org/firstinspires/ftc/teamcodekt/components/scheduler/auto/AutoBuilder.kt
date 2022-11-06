package org.firstinspires.ftc.teamcodekt.components.scheduler.auto

import android.os.Build
import androidx.annotation.RequiresApi
import com.acmerobotics.roadrunner.trajectory.MarkerCallback
import org.firstinspires.ftc.teamcode.opmodes.auto.SuperExperimentalWIPTestAutoNov5ForMyTestingOnlyPleaseDoNotUseItWontWork
import org.firstinspires.ftc.teamcodekt.util.toIn
import org.firstinspires.ftc.teamcodekt.util.toRad
import java.io.*
import java.nio.file.Files

const val FILE_PATH = "../test_autobuilder.json"

class Action(
    val index: Int,
    val methodName: String,
    val tag: String,
    val args: Array<out Var>
)

open class Var(
    val name: String,
    val type: String,
    val data: Any?,
) {
    override fun toString() = buildString { 
        append('{')
        append("\"name:\": $name,")
        append("\"type:\": $type,")
        append("\"data:\": $data")
        append('}')
    }
}

class SerializableVar(
    name: String,
    type: String,
    val filePath: String,
) : Var(name, type, null) {
    override fun toString() = buildString {
        append('{')
        append("\"name:\": $name,")
        append("\"type:\": $type,")
        append("\"serialized:\": true,")
        append("\"filePath:\" $filePath")
        append('}')
    }
}

class AutoBuilder {
    private var variables = mutableListOf<Action>()
    private var methodCalls = mutableMapOf<String, Int>()

    @JvmOverloads
    fun forward(distance: Double, tag: String? = null) = this.apply {
        val param1 = Var("distance", "double", distance.toIn())
        variables.addAction("forward", tag, param1)
    }

    @JvmOverloads
    fun back(distance: Double, tag: String? = null) = this.apply {
        val param1 = Var("distance", "double", distance.toIn())
        variables.addAction("back", tag, param1)
    }

    @JvmOverloads
    fun turn(angle: Double, tag: String? = null) = this.apply {
        val param1 = Var("angle", "double", angle.toRad())
        variables.addAction("turn", tag, param1)
    }

    @JvmOverloads
    fun splineTo(x: Double, y: Double, heading: Double, tag: String? = null) = this.apply {
        val param1 = Var("x", "double", x.toIn())
        val param2 = Var("y", "double", y.toIn())
        val param3 = Var("heading", "double", heading.toRad())
        variables.addAction("splineTo", tag, param1, param2, param3)
    }

    @JvmOverloads
    fun waitSeconds(time: Double, tag: String? = null) = this.apply {
        val param1 = Var("time", "double", time)
        variables.addAction("waitSeconds", tag, param1)
    }

    @JvmOverloads
    fun setReversed(reversed: Boolean, tag: String? = null) = this.apply {
        val param1 = Var("reversed", "boolean", reversed)
        variables.addAction("setReversed", tag, param1)
    }

    interface SerializableMarkerCallback : MarkerCallback, Serializable

    @RequiresApi(Build.VERSION_CODES.O)
    @JvmOverloads
    fun temporalMarker(offset: Double = 0.0, action: SerializableMarkerCallback, tag: String? = null) = this.apply {
        val lambdaType = "com.acmerobotics.roadrunner.trajectory.MarkerCallback"

        val file: File = Files.createTempFile("autobuilder_temporalMarker", "ser").toFile()
        ObjectOutputStream(FileOutputStream(file)).use { oo ->
            oo.writeObject(action)
        }

        val param1 = Var("offset", "double", offset)
        val param2 = SerializableVar("offset", lambdaType, file.absolutePath)

        variables.addAction("temporalMarker", tag, param1, param2)
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
            append("\"method_name\": \"${action.methodName}\",")
            append("\"tag\": \"${action.tag}\",")
            append("\"args\": [")
            action.args.joinToString(",", transform = Var::toString)
            deleteCharAt(lastIndex)
            append("]")
            append("}")
            if (index != variables.size - 1) {
                append(',')
            }
        }
        append(']')
    }

    @JvmOverloads
    fun writeJsonToFile(filePath: String = FILE_PATH) {
        print(toJSON())

        File(filePath).bufferedWriter().use { out ->
            out.write(toJSON())
        }
    }

    private fun MutableList<Action>.addAction(methodName: String, tag: String?, vararg args: Var) {
        incrementMethodCallsFor(methodName)

        val defaultTag = methodName + "_" + methodCalls[methodName]

        this.add(Action(size, methodName, tag?:defaultTag, args))
    }

    private fun incrementMethodCallsFor(methodName: String) =
        methodCalls.compute(methodName) { _, value -> value?.let { it + 1 } ?: 0 }
}

fun main() {
    SuperExperimentalWIPTestAutoNov5ForMyTestingOnlyPleaseDoNotUseItWontWork().also {
        it.schedulePaths()
    }
}