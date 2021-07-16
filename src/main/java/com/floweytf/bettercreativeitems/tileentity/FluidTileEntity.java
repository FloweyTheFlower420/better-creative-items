package com.floweytf.bettercreativeitems.tileentity;

import static com.floweytf.bettercreativeitems.Constants.*;

import com.floweytf.bettercreativeitems.capabilities.CreativeFluidHandler;
import com.floweytf.bettercreativeitems.container.FluidContainer;
import com.floweytf.bettercreativeitems.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("NullableProblems")
public class FluidTileEntity extends TileEntity implements IInteractionObject {
    private static final String TRANSLATION_KEY = Utils.translationKey("container", "fluid", "name");
    private static final String GUI_ID = id("fluid").toString();

    private final CreativeFluidHandler cap = new CreativeFluidHandler();

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) cap; // ur capping

        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        setFluid(compound.getString(NBT_TAG_NAME));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString(NBT_TAG_NAME, getFluidID());
        return compound;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new FluidContainer();
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

    public void setFluid(String id) {
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
        cap.fluid = FluidRegistry.getFluid(id);
    }

    public Fluid getFluid() {
        return cap.fluid;
    }

    public String getFluidID() {
        if (cap.fluid == null)
            return emptyFluid();
        return FluidRegistry.getFluidName(cap.fluid);
    }

    public static String emptyFluid() {
        return FLUID_ID_EMPTY;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = super.getUpdateTag();
        tag.setString(NBT_TAG_NAME, getFluidID());
        return tag;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        SPacketUpdateTileEntity packet = super.getUpdatePacket();
        if (packet == null)
            packet = new SPacketUpdateTileEntity(pos, 1, new NBTTagCompound());
        packet.getNbtCompound().setString(NBT_TAG_NAME, getFluidID());
        return packet;
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        setFluid(pkt.getNbtCompound().getString(NBT_TAG_NAME));
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        setFluid(tag.getString(NBT_TAG_NAME));
    }
}
