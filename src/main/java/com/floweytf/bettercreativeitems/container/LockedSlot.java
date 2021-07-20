package com.floweytf.bettercreativeitems.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class LockedSlot extends Slot {
    public LockedSlot(IInventory p_i47453_1_, int p_i47453_2_, int p_i47453_3_, int p_i47453_4_) {
        super(p_i47453_1_, p_i47453_2_, p_i47453_3_, p_i47453_4_);
    }

    public boolean canTakeStack(EntityPlayer playerIn) {
        if (super.canTakeStack(playerIn) && this.getHasStack()) {
            return this.getStack().getSubCompound("CustomCreativeLock") == null;
        }
        else {
            return !this.getHasStack();
        }
    }
}