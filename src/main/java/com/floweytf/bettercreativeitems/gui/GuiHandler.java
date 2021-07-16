package com.floweytf.bettercreativeitems.gui;

import com.floweytf.bettercreativeitems.container.FluidContainer;
import com.floweytf.bettercreativeitems.container.ItemContainer;
import com.floweytf.bettercreativeitems.tileentity.FluidTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import static com.floweytf.bettercreativeitems.Constants.GUI_ID_FLUID;
import static com.floweytf.bettercreativeitems.Constants.GUI_ID_ITEM;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case GUI_ID_FLUID:
                return new FluidContainer();
            case GUI_ID_ITEM:
                return new ItemContainer(player);
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case GUI_ID_FLUID:
                return new FluidGui((FluidTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
            case GUI_ID_ITEM:
                return new ItemGui(player);
            default:
                return null;
        }
    }
}