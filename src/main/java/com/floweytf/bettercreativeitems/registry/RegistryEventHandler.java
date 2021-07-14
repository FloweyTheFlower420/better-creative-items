package com.floweytf.bettercreativeitems.registry;

import com.floweytf.bettercreativeitems.ModMain;
import com.floweytf.bettercreativeitems.MyItemBlock;
import com.floweytf.bettercreativeitems.block.EnergyBlock;
import com.floweytf.bettercreativeitems.block.FluidBlock;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.floweytf.bettercreativeitems.ModMain.MODID;
import static com.floweytf.bettercreativeitems.ModMain.id;

@Mod.EventBusSubscriber
public class RegistryEventHandler {
    @SubscribeEvent
    public static void onRegistryItem(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new MyItemBlock(Registry.ENERGY_BLOCK,"energy"));
        event.getRegistry().register(new MyItemBlock(Registry.FLUID_BLOCK,"fluid"));
    }

    @SubscribeEvent
    public static void onRegisterBlock(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new EnergyBlock());
        event.getRegistry().register(new FluidBlock());
    }
}
