package com.floweytf.bettercreativeitems;

import net.minecraftforge.common.config.Config;

@Config(modid = Constants.MOD_ID)
public class ModConfig {
    public static Energy energy = new Energy();

    public static class Energy {
        @Config.Comment("Creative energy export cycles")
        @Config.RangeInt(min = 0, max = 256)
        public int cyclesPerTick = 1;

        @Config.Comment("Radius for wireless from energy")
        @Config.RangeInt(min = 1, max = 100)
        public int radius = 3;
    }
}
