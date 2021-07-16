package com.floweytf.bettercreativeitems.block;

import com.floweytf.bettercreativeitems.ModMain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import static com.floweytf.bettercreativeitems.Constants.MOD_ID;
import static com.floweytf.bettercreativeitems.Constants.id;

/**
 * Base block, handles: registry ID, naming, creative tab, model register
 */
public class BlockBase extends Block {
    public BlockBase(Material materialIn, String id) {
        super(materialIn);
        setRegistryName(id(id));
        setUnlocalizedName(MOD_ID + "." + id);
        setCreativeTab(CreativeTabs.MISC);
        ModMain.proxy.registerModel(Item.getItemFromBlock(this), 0);
    }
}
