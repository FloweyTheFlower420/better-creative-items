package com.floweytf.bettercreativeitems.utils;

import com.floweytf.bettercreativeitems.ModMain;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

import static com.floweytf.bettercreativeitems.Constants.MOD_ID;
import static com.floweytf.bettercreativeitems.Constants.id;

public class MyItemBlock extends ItemBlock {
    public MyItemBlock(Block block, String id) {
        super(block);
        setCreativeTab(CreativeTabs.MISC);
        setRegistryName(id(id));
        setUnlocalizedName(MOD_ID + '.' + id);
        ModMain.proxy.registerModel(this, 0);
    }
}
