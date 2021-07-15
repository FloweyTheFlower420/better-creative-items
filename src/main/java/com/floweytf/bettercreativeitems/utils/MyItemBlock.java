package com.floweytf.bettercreativeitems.utils;

import com.floweytf.bettercreativeitems.Constants;
import com.floweytf.bettercreativeitems.ModMain;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

public class MyItemBlock extends ItemBlock {
    public MyItemBlock(Block block, String id) {
        super(block);
        setCreativeTab(CreativeTabs.MISC);
        setRegistryName(Constants.id(id));
        setUnlocalizedName(Constants.MOD_ID + '.' + id);
        ModMain.proxy.registerModel(this, 0);
    }
}
