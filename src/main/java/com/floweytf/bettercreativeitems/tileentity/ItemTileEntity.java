package com.floweytf.bettercreativeitems.tileentity;

import com.floweytf.bettercreativeitems.container.ItemContainer;
import com.floweytf.bettercreativeitems.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

import javax.annotation.Nonnull;

import static com.floweytf.bettercreativeitems.Constants.FLUID_ID_EMPTY;
import static com.floweytf.bettercreativeitems.Constants.id;

@SuppressWarnings("NullableProblems")
public class ItemTileEntity extends TileEntity implements IInteractionObject {
    private static final String TRANSLATION_KEY = Utils.translationKey("container", "item", "name");
    private static final String GUI_ID = id("item").toString();

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ItemContainer(playerIn);
    }

    @Override
    public String getGuiID() {
        return GUI_ID;
    }

    @Override
    public String getName() {
        return TRANSLATION_KEY;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(getName());
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

}
