package com.floweytf.bettercreativeitems.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class FluidContainer extends Container {
    public FluidContainer() {

    }
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
