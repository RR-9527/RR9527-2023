package org.firstinspires.ftc.teamcodekt.components.scheduler.auto

import com.acmerobotics.roadrunner.trajectory.MarkerCallback
import java.io.*

class Test1 {
    private class Hi {
        var hi = 1

        fun sayHello(): Int {
            println("Hello")
            return 3
        }
    }

    private fun interface SerializableMarkerCallback2 : MarkerCallback, Serializable

    fun test() {
        val a = SerializableMarkerCallback2 {
            val hi = Hi()
            println(hi.sayHello())
            hi.hi = hi.sayHello()
            hi.hi += 3
            println(hi.hi)
        }

        val file = File("test.txt")
        ObjectOutputStream(FileOutputStream(file)).use { oo ->
            oo.writeObject(a)
        }
    }
}

class Test2 {
    fun test() {
        ObjectInputStream(FileInputStream(File("test.txt"))).use { oi ->
            val a = oi.readObject() as MarkerCallback
            a.onMarkerReached()
        }
    }
}