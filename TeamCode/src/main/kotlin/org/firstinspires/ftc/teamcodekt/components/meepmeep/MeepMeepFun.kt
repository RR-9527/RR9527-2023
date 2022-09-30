//package org.firstinspires.ftc.teamcode.components.meepmeep
//
//import com.noahbres.meepmeep.MeepMeep
//import com.noahbres.meepmeep.MeepMeep.Background
//import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark
//import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueLight
//import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark
//import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedLight
//import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity
//import org.firstinspires.ftc.teamcodekt.components.meepmeep.ScheduledMeepMeepExecutor
//import java.util.concurrent.TimeUnit
//
//class MeepMeepFun(private val meepMeep: MeepMeep, private val bot: RoadRunnerBotEntity) {
//    fun launch() {
//        val backgrounds = arrayOf(Background.GRID_GRAY, Background.GRID_BLUE, Background.GRID_GREEN)
//        val colorSchemes = arrayOf(
//            ColorSchemeBlueDark(),
//            ColorSchemeBlueLight(),
//            ColorSchemeRedDark(),
//            ColorSchemeRedLight()
//        )
//        var i = 0
//        ScheduledMeepMeepExecutor.EXECUTOR.scheduleAtFixedRate({
//            meepMeep.setBackground(backgrounds[i % backgrounds.size])
//            bot.switchScheme(colorSchemes[i % colorSchemes.size])
//            meepMeep.setDarkMode(i++ % 2 == 0)
//        }, 1, 1, TimeUnit.SECONDS)
//    }
//
//    init {
//        bot.listenToSwitchThemeRequest = true
//    }
//}
//
