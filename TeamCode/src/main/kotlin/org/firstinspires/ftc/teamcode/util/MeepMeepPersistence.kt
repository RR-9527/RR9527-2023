package org.firstinspires.ftc.teamcode.util

import com.noahbres.meepmeep.MeepMeep
import java.io.File
import java.util.*

const val DEFAULT_FILE_PATH = "TeamCode/src/main/res/raw/meepmeep.properties"

class MeepMeepPersistence(private val meepMeep: MeepMeep) {
    private val properties = Properties()

    init {
        reload()

        Runtime.getRuntime().addShutdownHook(Thread {
            save()
        })
    }

    @JvmOverloads fun reload(filePath: String = DEFAULT_FILE_PATH) {
        File(filePath).also { it.createNewFile() }.bufferedReader().use {
            properties.load(it)
        }
    }

    @JvmOverloads fun save(filePath: String = DEFAULT_FILE_PATH) {
        properties["window_x"] = meepMeep.windowFrame.x.toString()
        properties["window_y"] = meepMeep.windowFrame.y.toString()

        File(filePath).also { it.createNewFile() }.bufferedWriter().use {
            properties.store(it, null)
        }
    }

    fun restore() {
        meepMeep.windowFrame.setLocation(
            properties.getProperty("window_x", "0").toInt(),
            properties.getProperty("window_y", "0").toInt(),
        )
    }
}