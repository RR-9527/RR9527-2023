package org.firstinspires.ftc.teamcodekt.components.scheduler.auto

import android.os.Build
import androidx.annotation.RequiresApi
import com.acmerobotics.roadrunner.trajectory.MarkerCallback
import org.firstinspires.ftc.teamcode.opmodes.auto.SuperExperimentalWIPTestAutoNov5ForMyTestingOnlyPleaseDoNotUseItWontWork
import java.io.*
import java.nio.file.Files

const val DEFAULT_CONFIG_FILE = "tunableautoconfig.json"

class MethodCall(
    val index: Int,
    val methodName: String,
    val tag: String,
    val args: Array<out Var>
)

open class Var(val name: String, val type: String, val data: Any?) {
    override fun toString() = """
    {
         "name": "$name",
         "type": "$type",
         "data": "$data"
    }
    """.trimIndent()
}

class SerializableVar(name: String, type: String, val filePath: String) : Var(name, type, null) {
    override fun toString() = """
    {
         "name": "$name",
         "type": "$type",
         "is_serialized": true,
         "file_path": "$filePath"
    }
    """.trimIndent()
}

class TuningAutoBuilder(_configFile: String = DEFAULT_CONFIG_FILE) {
    private val jsonOutputPath: String

    init {
        val configFile = File(_configFile)

        val config = configFile.readText()

        jsonOutputPath = config
            .substringAfter("\"json_output_path\":")
            .substringAfter('"')
            .substringBefore('"')
    }

    private var variables = mutableListOf<MethodCall>()
    private var methodCalls = mutableMapOf<String, Int>()

    @JvmOverloads
    fun forward(distance: Double, tag: String? = null) = this.apply {
        val param1 = Var("distance", "double", distance)
        variables.addAction("forward", tag, param1)
    }

    @JvmOverloads
    fun back(distance: Double, tag: String? = null) = this.apply {
        val param1 = Var("distance", "double", distance)
        variables.addAction("back", tag, param1)
    }

    @JvmOverloads
    fun turn(angle: Double, tag: String? = null) = this.apply {
        val param1 = Var("angle", "double", angle)
        variables.addAction("turn", tag, param1)
    }

    @JvmOverloads
    fun splineTo(x: Double, y: Double, heading: Double, tag: String? = null) = this.apply {
        val param1 = Var("x", "double", x)
        val param2 = Var("y", "double", y)
        val param3 = Var("heading", "double", heading)
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
        val param2 = SerializableVar("callback", lambdaType, file.absolutePath.replace("\\", "\\\\"))

        variables.addAction("temporalMarker", tag, param1, param2)
    }

    interface AutoBuilderRepeatCallback {
        fun callback(iterationNum: Int, builder: TuningAutoBuilder)
    }

    fun doTimes(times: Int, callback: AutoBuilderRepeatCallback) = this.apply {
        for (i in 0 until times) {
            callback.callback(i, this)
        }
    }

    fun toJSON() = """
    {
        "method_calls":[${
            variables.joinToString(",") {"""
            {
                "index":${it.index},
                "method_name":"${it.methodName}",
                "tag":"${it.tag}",
                "args":[
                    ${it.args.joinToString(",")}
                ]
            }"""}}
        ]
    }
    """.trimIndent()

    fun writeJsonToFile() {
//        print(toJSON())

        File(jsonOutputPath).bufferedWriter().use { out ->
            out.write(toJSON())
        }
    }

    private fun MutableList<MethodCall>.addAction(methodName: String, tag: String?, vararg args: Var) {
        incrementMethodCallsFor(methodName)

        val defaultTag = methodName + methodCalls[methodName]

        add(MethodCall(size, methodName, tag?:defaultTag, args))
    }

    private fun incrementMethodCallsFor(methodName: String) =
        methodCalls.compute(methodName) { _, value -> value?.let { it + 1 } ?: 0 }
}

fun main() {
    SuperExperimentalWIPTestAutoNov5ForMyTestingOnlyPleaseDoNotUseItWontWork().also {
        it.schedulePaths()
    }
}
