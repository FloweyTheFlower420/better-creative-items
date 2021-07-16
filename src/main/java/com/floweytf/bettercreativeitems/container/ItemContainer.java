package com.floweytf.bettercreativeitems.container;

import com.floweytf.bettercreativeitems.gui.ItemGui;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

// Code yoinked from MC, just w/ a small extra thing.
@SuppressWarnings("NullableProblems")
public class ItemContainer extends Container {
    public NonNullList<ItemStack> itemList = NonNullList.create();

    public ItemContainer(EntityPlayer player) {
        InventoryPlayer inventoryplayer = player.inventory;

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new GuiContainerCreative.LockedSlot(ItemGui.basicInventory, i * 9 + j, 9 + j * 18, 18 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(inventoryplayer, k, 9 + k * 18, 112));
        }

        this.scrollTo(0.0F);
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    public void scrollTo(float pos) {
        int i = (this.itemList.size() + 9 - 1) / 9 - 5;
        int j = (int) ((double) (pos * (float) i) + 0.5D);

        if (j < 0) {
            j = 0;
        }

        for (int k = 0; k < 5; ++k) {
            for (int l = 0; l < 9; ++l) {
                int i1 = l + (k + j) * 9;

                if (i1 >= 0 && i1 < this.itemList.size()) {
                    ItemGui.basicInventory.setInventorySlotContents(l + k * 9, this.itemList.get(i1));
                }
                else {
                    ItemGui.basicInventory.setInventorySlotContents(l + k * 9, ItemStack.EMPTY);
                }
            }
        }
    }

    public boolean canScroll() {
        return this.itemList.size() > 45;
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size()) {
            Slot slot = this.inventorySlots.get(index);

            if (slot != null && slot.getHasStack()) {
                slot.putStack(ItemStack.EMPTY);
            }
        }

        return ItemStack.EMPTY;
    }

    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.yPos > 90;
    }

    public boolean canDragIntoSlot(Slot slotIn) {
        return slotIn.inventory instanceof InventoryPlayer || slotIn.yPos > 90 && slotIn.xPos <= 162;
    }
}