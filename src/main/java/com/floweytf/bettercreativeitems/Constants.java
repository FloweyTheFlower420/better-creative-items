package com.floweytf.bettercreativeitems;

import com.floweytf.bettercreativeitems.utils.FluidRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;

public class Constants {
    public static final String MOD_ID = "better_creative_items";
    public static final String NAME = "Better Creative Items";
    public static final String VERSION = "1.1.2";
    public static final ArrayList<FluidRenderer> FLUIDS = new ArrayList<>();
    public static final ArrayList<Item> ITEMS = new ArrayList<>();

    public static final int GUI_ID_FLUID = 1;
    public static final int GUI_ID_ITEM = 2;

    public static final String NBT_TAG_NAME = "fluid";
    public static final String FLUID_ID_EMPTY = id("empty").toString();

    public static CreativeTabs[] CREATIVE_TABS;
    public static CreativeTabs TAB;

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }
}