package com.floweytf.bettercreativeitems.block;

import com.floweytf.bettercreativeitems.ModMain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BlockBase extends Block {
    public BlockBase(Material materialIn, String id) {
        super(materialIn);
        setRegistryName(ModMain.id(id));
        setUnlocalizedName(ModMain.MODID + "." + id);
        setCreativeTab(CreativeTabs.MISC);
        ModMain.proxy.registerModel(Item.getItemFromBlock(this), 0);
    }
}
