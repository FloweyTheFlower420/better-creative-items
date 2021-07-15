package com.floweytf.bettercreativeitems;

import com.floweytf.bettercreativeitems.gui.GuiHandler;
import com.floweytf.bettercreativeitems.network.PacketHandler;
import com.floweytf.bettercreativeitems.tileentity.EnergyTileEntity;
import com.floweytf.bettercreativeitems.tileentity.FluidTileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;

import static com.floweytf.bettercreativeitems.Constants.*;

@Mod(modid = Constants.MOD_ID, name = Constants.NAME, version = Constants.VERSION)
public class ModMain {
    @SidedProxy(clientSide = "com.floweytf.bettercreativeitems.CommonProxy",
        serverSide = "com.floweytf.bettercreativeitems.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static ModMain instance;
    public static Logger LOG;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOG = event.getModLog();
        GameRegistry.registerTileEntity(EnergyTileEntity.class, Constants.id("energy_tile_entity"));
        GameRegistry.registerTileEntity(FluidTileEntity.class, Constants.id("fluid_tile_entity"));
        PacketHandler.register();
        for(int i = 0; i < 100; i++ ) {
            FluidRegistry.addBucketForFluid(
                new Fluid(id("test_" + i).toString(),
                    id("test_" + i),
                    id("test" + i)
                ));
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        FLUIDS.addAll(FluidRegistry.getRegisteredFluids().values());
        FLUIDS.sort(Comparator.comparing(v -> v.getLocalizedName(new FluidStack(v, 1))));
        LOG.info("Scanning fluids done, with " + FLUIDS.size() + " fluids found!");
    }
}
