package com.floweytf.bettercreativeitems.registry;

import com.floweytf.bettercreativeitems.block.ItemBlock;
import com.floweytf.bettercreativeitems.utils.MyItemBlock;
import com.floweytf.bettercreativeitems.block.EnergyBlock;
import com.floweytf.bettercreativeitems.block.FluidBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RegistryEventHandler {
    @SubscribeEvent
    public static void onRegistryItem(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new MyItemBlock(Registry.ENERGY_BLOCK, "energy"));
        event.getRegistry().register(new MyItemBlock(Registry.FLUID_BLOCK, "fluid"));
        event.getRegistry().register(new MyItemBlock(Registry.ITEM_BLOCK, "item"));
    }

    @SubscribeEvent
    public static void onRegisterBlock(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new EnergyBlock());
        event.getRegistry().register(new FluidBlock());
        event.getRegistry().register(new ItemBlock()); // not to be confused with the NM ItemBlock
    }
}
