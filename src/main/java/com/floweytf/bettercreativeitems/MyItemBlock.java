package com.floweytf.bettercreativeitems;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

public class MyItemBlock extends ItemBlock {
    public MyItemBlock(Block block, String id) {
        super(block);
        setCreativeTab(CreativeTabs.MISC);
        setRegistryName(ModMain.id(id));
        setUnlocalizedName(ModMain.MODID + '.' + id);
        ModMain.proxy.registerModel(this, 0);
    }
}
