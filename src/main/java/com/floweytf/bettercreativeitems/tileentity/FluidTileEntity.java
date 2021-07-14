package com.floweytf.bettercreativeitems.tileentity;

import com.floweytf.bettercreativeitems.ModMain;
import com.floweytf.bettercreativeitems.caps.CreativeFluidHandler;
import com.floweytf.bettercreativeitems.container.FluidContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public class FluidTileEntity extends TileEntity implements IInteractionObject {
    final CreativeFluidHandler cap = new CreativeFluidHandler();

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) cap; // ur capping

        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        cap.fluid = FluidRegistry.getFluid(compound.getString("fluid"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("fluid", cap.fluid == null ? "better_creative_items:empty" : FluidRegistry.getFluidName(cap.fluid));
        return compound;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new FluidContainer();
    }

    @Override
    public String getGuiID() {
        return ModMain.id("fluid").toString();
    }

    @Override
    public String getName() {
        return "container.better_creative_items.fluid.name";
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(getName());
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
