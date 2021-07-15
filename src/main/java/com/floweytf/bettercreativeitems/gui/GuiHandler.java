package com.floweytf.bettercreativeitems.gui;

import static com.floweytf.bettercreativeitems.Constants.*;
import com.floweytf.bettercreativeitems.container.FluidContainer;
import com.floweytf.bettercreativeitems.tileentity.FluidTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if(ID == GUI_ID_FLUID)
            return new FluidContainer();
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if(ID == GUI_ID_FLUID)
            return new FluidGui((FluidTileEntity) world.getTileEntity(new BlockPos(x,y,z)));
        return null;
    }
}