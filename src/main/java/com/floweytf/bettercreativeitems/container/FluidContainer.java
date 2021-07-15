package com.floweytf.bettercreativeitems.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

@SuppressWarnings("NullableProblems")
public class FluidContainer extends Container {
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
