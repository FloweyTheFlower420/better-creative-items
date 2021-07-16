package com.floweytf.bettercreativeitems;

import net.minecraftforge.common.config.Config;

@Config(modid = Constants.MOD_ID)
public class ModConfig {
    public static Energy energy = new Energy();
    @Config.Comment({
        "Do **NOT** touch unless you know what you are doing!",
        "Doing so will cause the mod to register fluids for testing"}
    )
    public static boolean registerDebugFluids = false;

    @Config.Comment("Use this to disable spooky items from showing up!")
    public static String[] disabledSpookyItems = {"minecraft:air"};

    public static class Energy {
        @Config.Comment("Creative energy export cycles")
        @Config.RangeInt(min = 0, max = 256)
        public int cyclesPerTick = 1;

        @Config.Comment("Radius for wireless from energy")
        @Config.RangeInt(min = 1, max = 100)
        public int radius = 3;
    }
}