package com.floweytf.bettercreativeitems.container;

import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class FluidContainer extends Container {
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }
}
