package com.floweytf.bettercreativeitems.tileentity;

import com.floweytf.bettercreativeitems.api.IFluidRenderer;
import com.floweytf.bettercreativeitems.capabilities.CreativeFluidHandler;
import com.floweytf.bettercreativeitems.container.FluidContainer;
import com.floweytf.bettercreativeitems.plugin.FluidRendererRegistry;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.floweytf.bettercreativeitems.Constants.NBT_TAG_NAME;
import static com.floweytf.bettercreativeitems.Constants.id;

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
        cap.fluidRenderer = FluidRendererRegistry.get(compound.getString(NBT_TAG_NAME));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString(NBT_TAG_NAME, FluidRendererRegistry.get(cap.fluidRenderer).toString());
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
        tag.setString(NBT_TAG_NAME, FluidRendererRegistry.get(cap.fluidRenderer).toString());
        return tag;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        SPacketUpdateTileEntity packet = super.getUpdatePacket();
        if (packet == null) {
            packet = new SPacketUpdateTileEntity(pos, 1, new NBTTagCompound());
        }
        packet.getNbtCompound().setString(NBT_TAG_NAME, FluidRendererRegistry.get(cap.fluidRenderer).toString());
        return packet;
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        cap.fluidRenderer = FluidRendererRegistry.get(pkt.getNbtCompound().getString(NBT_TAG_NAME));
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        cap.fluidRenderer = FluidRendererRegistry.get(tag.getString(NBT_TAG_NAME));
    }

    public IFluidRenderer getFluidRenderer() {
        return cap.fluidRenderer;
    }

    public void setFluidRenderer(IFluidRenderer renderer) {
        cap.fluidRenderer = renderer;
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(getPos(), state, state, 2);
    }
}
