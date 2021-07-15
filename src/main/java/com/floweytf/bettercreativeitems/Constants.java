package com.floweytf.bettercreativeitems;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;

public class Constants {
    public static final String MOD_ID = "better_creative_items";
    public static final String NAME = "Better Creative Items";
    public static final String VERSION = "0.2.2";
    public static final ArrayList<Fluid> FLUIDS = new ArrayList<>();
    public static final int GUI_ID_FLUID = 1;
    public static final String NBT_TAG_NAME = "fluid";
    public static final String FLUID_ID_EMPTY = id("empty").toString();


    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static String translationKey(String key, String id, String name) {
        return key + '.' + MOD_ID + '.' + id + '.' + name;
    }
}
