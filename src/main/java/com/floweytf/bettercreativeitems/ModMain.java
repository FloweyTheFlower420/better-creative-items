package com.floweytf.bettercreativeitems;

import com.floweytf.bettercreativeitems.gui.GuiHandler;
import com.floweytf.bettercreativeitems.network.PacketHandler;
import com.floweytf.bettercreativeitems.plugin.FluidRenderer;
import com.floweytf.bettercreativeitems.plugin.FluidRendererRegistry;
import com.floweytf.bettercreativeitems.proxy.CommonProxy;
import com.floweytf.bettercreativeitems.registry.Registry;
import com.floweytf.bettercreativeitems.tileentity.EnergyTileEntity;
import com.floweytf.bettercreativeitems.tileentity.FluidTileEntity;
import com.floweytf.bettercreativeitems.tileentity.ItemTileEntity;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.floweytf.bettercreativeitems.Constants.*;

@Mod(modid = Constants.MOD_ID, name = Constants.NAME, version = Constants.VERSION)
public class ModMain {
    @SidedProxy(clientSide = "com.floweytf.bettercreativeitems.proxy.ClientProxy",
        serverSide = "com.floweytf.bettercreativeitems.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static ModMain instance;
    public static Logger LOG;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOG = event.getModLog();
        GameRegistry.registerTileEntity(EnergyTileEntity.class, Constants.id("energy_tile_entity"));
        GameRegistry.registerTileEntity(FluidTileEntity.class, Constants.id("fluid_tile_entity"));
        GameRegistry.registerTileEntity(ItemTileEntity.class, Constants.id("item_tile_entity"));

        PacketHandler.register();
        if (ModConfig.registerDebugFluids) {
            for (int i = 0; i < 100; i++) {
                ResourceLocation id = id("test_" + i);
                FluidRegistry.addBucketForFluid(new Fluid(id.toString(), id, id));
            }
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        initFluids();
        initItems();
    }

    private void initFluids() {
        LOG.info("Scanning fluids");
        FluidRegistry.getRegisteredFluids().forEach((k, v) -> {
            FluidRendererRegistry.register(id(k), FluidRenderer.getFromFluid(v));
        });
        FluidRendererRegistry.sort();
        LOG.info("Scanning fluids done, with " + FluidRendererRegistry.size() + " fluids found!");
    }

    private void initItems() {
        LOG.info("Scanning items");
        CREATIVE_TABS = new CreativeTabs[CreativeTabs.CREATIVE_TAB_ARRAY.length];

        // copy over MC tabs, skipping inventory tab, and replacing w/ my mod tag
        for (int i = 0; i < CreativeTabs.CREATIVE_TAB_ARRAY.length; i++) {
            if (CreativeTabs.CREATIVE_TAB_ARRAY[i] == CreativeTabs.INVENTORY) {
                TAB = CREATIVE_TABS[i] = new CreativeTabs(i, "inaccessible") {
                    @Override
                    public ItemStack getTabIconItem() {
                        return new ItemStack(Registry.BARRIER, 1);
                    }
                };
                CreativeTabs.CREATIVE_TAB_ARRAY[i] = CreativeTabs.INVENTORY;
            }
            else {
                CREATIVE_TABS[i] = CreativeTabs.CREATIVE_TAB_ARRAY[i];
            }
        }

        Set<String> spookyIDs = new HashSet<>(Arrays.asList(ModConfig.disabledSpookyItems));

        int itemCount = 0;
        for (Item item : Item.REGISTRY) {
            if (item.getRegistryName() == null) {
                LOG.warn("scary item with null registry name found, skipping");
            }
            else if (item.getCreativeTab() == null) {
                if (spookyIDs.contains(item.getRegistryName().toString())) {
                    LOG.debug("skipping spooky item: " + item.getRegistryName().toString());
                }
                else {
                    ITEMS.add(item);
                    LOG.debug("found item: " + item.getRegistryName().toString());
                    itemCount++;
                }
            }
        }

        LOG.info("Found " + itemCount + " items belonging to no creative tab");
    }
}