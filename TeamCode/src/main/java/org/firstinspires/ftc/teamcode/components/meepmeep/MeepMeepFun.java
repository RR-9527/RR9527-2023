//package org.firstinspires.ftc.teamcode.util;
//
//import com.noahbres.meepmeep.MeepMeep;
//import com.noahbres.meepmeep.core.colorscheme.ColorScheme;
//import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
//import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueLight;
//import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
//import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedLight;
//import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
//
//import java.util.concurrent.TimeUnit;
//
//public class MeepMeepFun {
//    private final RoadRunnerBotEntity bot;
//    private final MeepMeep meepMeep;
//
//    public MeepMeepFun(MeepMeep meepMeep, RoadRunnerBotEntity bot) {
//        this.meepMeep = meepMeep;
//        this.bot = bot;
//
//        bot.setListenToSwitchThemeRequest(true);
//    }
//
//    public void launch() {
//        MeepMeep.Background[] backgrounds = {
//            MeepMeep.Background.GRID_GRAY,
//            MeepMeep.Background.GRID_BLUE,
//            MeepMeep.Background.GRID_GREEN,
//        };
//
//        ColorScheme[] colorSchemes = {
//            new ColorSchemeBlueDark(),
//            new ColorSchemeBlueLight(),
//            new ColorSchemeRedDark(),
//            new ColorSchemeRedLight(),
//        };
//
//        long[] i = {0};
//        ScheduledMeepMeepExecutor.executor.scheduleAtFixedRate(() -> {
//            meepMeep.setBackground(backgrounds[(int) (i[0] % backgrounds.length)]);
//            bot.switchScheme(colorSchemes[(int) (i[0] % colorSchemes.length)]);
//            meepMeep.setDarkMode((i[0]++ & 1) == 0);
//        }, 1, 1, TimeUnit.SECONDS);
//    }
//}
