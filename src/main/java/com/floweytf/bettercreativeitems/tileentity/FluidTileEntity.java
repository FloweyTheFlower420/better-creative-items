package com.floweytf.bettercreativeitems.tileentity;

import com.floweytf.bettercreativeitems.capabilities.CreativeFluidHandler;
import com.floweytf.bettercreativeitems.container.FluidContainer;
import com.floweytf.bettercreativeitems.utils.FluidRenderer;
import com.floweytf.bettercreativeitems.utils.Utils;
import net.minecraft.block.state.IBlockState;
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

import static com.floweytf.bettercreativeitems.Constants.*;

@SuppressWarnings("NullableProblems")
public class FluidTileEntity extends TileEntity implements IInteractionObject {
    private static final String TRANSLATION_KEY = Utils.translationKey("container", "fluid", "name");
    private static final String GUI_ID = id("fluid").toString();

    private final CreativeFluidHandler cap = new CreativeFluidHandler();

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) cap; // ur capping
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        cap.fluid.readFrom(compound.getTag(NBT_TAG_NAME));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag(NBT_TAG_NAME, cap.fluid.getWriteTag());
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



    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = super.getUpdateTag();
        tag.setTag(NBT_TAG_NAME, cap.fluid.getWriteTag());
        return tag;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        SPacketUpdateTileEntity packet = super.getUpdatePacket();
        if (packet == null) {
            packet = new SPacketUpdateTileEntity(pos, 1, new NBTTagCompound());
        }
        packet.getNbtCompound().setTag(NBT_TAG_NAME, cap.fluid.getWriteTag());
        return packet;
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        cap.fluid.readFrom(pkt.getNbtCompound().getTag(NBT_TAG_NAME));
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        cap.fluid.readFrom(tag.getTag(NBT_TAG_NAME));
    }

    public FluidRenderer getFluidRenderer() {
        return cap.fluid;
    }

    public void setFluidRenderer(FluidRenderer renderer) {
        cap.fluid = renderer;
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(getPos(), state, state, 2);
    }
}
