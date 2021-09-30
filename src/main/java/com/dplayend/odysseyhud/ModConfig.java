package com.dplayend.odysseyhud;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = "odysseyhud")
@Config.LangKey("OdysseyHUD Config")
public class ModConfig {
    @Config.Comment({"When enabled, will only show the game time when a clock is present in the inventory."})
    @Config.Name("Must have clock in inventory")
    public static boolean Clock = true;

    public enum posX {MIDDLE, LEFT, RIGHT,}
    @Config.Comment({"Horizontal Position of Clock"})
    @Config.Name("Horizontal Position")
    public static posX hPos = posX.MIDDLE;

    @Config.Comment({"xSetOff Position of Clock"})
    @Config.Name("xSetOff Position")
    @Config.RangeInt( min = -4096, max = 4096 )
    public static int xOff = 93;

    public enum posY {TOP, BOTTOM,}
    @Config.Comment({"Vertical Position of Clock"})
    @Config.Name("Vertical Position")
    public static posY vPos = posY.BOTTOM;

    @Config.Comment({"ySetOff Position of Clock"})
    @Config.Name("ySetOff Position")
    @Config.RangeInt( min = -4096, max = 4096 )
    public static int yOff = -18;

    public ModConfig() {}

    @Mod.EventBusSubscriber(modid = "odysseyhud")
    private static class EventHandler {
        private EventHandler() {
        }

        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals("odysseyhud")) {
                ConfigManager.sync("odysseyhud", Config.Type.INSTANCE);
            }

        }
    }
}
