package com.floweytf.bettercreativeitems;
import com.floweytf.bettercreativeitems.gui.GuiHandler;
import com.floweytf.bettercreativeitems.tileentity.EnergyTileEntity;
import com.floweytf.bettercreativeitems.tileentity.FluidTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Comparator;

@Mod(modid = ModMain.MODID, name = ModMain.NAME, version = ModMain.VERSION)
public class ModMain {
    @SidedProxy(clientSide = "com.floweytf.bettercreativeitems.CommonProxy",
        serverSide = "com.floweytf.bettercreativeitems.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static ModMain instance;

    public static final String MODID = "better_creative_items";
    public static final String NAME = "Better Creative Items";
    public static final String VERSION = "1.0";
    public static final ArrayList<Fluid> FLUIDS = new ArrayList<>();

    public static final int GUI_ID_FLUID = 1;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GameRegistry.registerTileEntity(EnergyTileEntity.class, id("energy_tile_entity"));
        GameRegistry.registerTileEntity(FluidTileEntity.class, id("fluid_tile_entity"));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
    }

    @Mod.EventHandler
    public void posInit(FMLPostInitializationEvent event) {
        FLUIDS.addAll(FluidRegistry.getRegisteredFluids().values());
        // STACK ISN'T EVEN USED???? Are forge devs high?!
        FLUIDS.sort(Comparator.comparing(v -> v.getLocalizedName(null)));
    }


    public static ResourceLocation id(String name) {
        return new ResourceLocation(MODID, name);
    }
}
