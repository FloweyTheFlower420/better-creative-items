package com.floweytf.bettercreativeitems.utils;

import com.floweytf.bettercreativeitems.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.floweytf.bettercreativeitems.Constants.FLUID_ID_EMPTY;

public class FluidRenderer {
    @Nullable
    protected Fluid fluid = null;

    public FluidRenderer() {

    }

    public FluidRenderer(@NotNull Fluid fluid) {
        this.fluid = fluid;
    }

    public String getName() {
        return fluid.getLocalizedName(new FluidStack(fluid, 1));
    }

    public void draw(int x, int y, GuiContainer container) {
        container.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite sprite = container.mc.getTextureMapBlocks().getAtlasSprite(fluid.getStill().toString());
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        container.drawTexturedModalRect(x + 1, y + 1, sprite, 16, 16);
    }

    public boolean isEmpty() {
        return fluid == null;
    }

    public FluidStack getAsStack(int amount) {
        return new FluidStack(fluid, amount);
    }

    public boolean canDrainFluidType(FluidStack other) {
        return other.getFluid() == fluid;
    }

    public NBTBase getWriteTag() {
        if(fluid == null)
            return new NBTTagString(FLUID_ID_EMPTY);
        return new NBTTagString(FluidRegistry.getFluidName(fluid));
    }

    public void readFrom(NBTBase tag) {
        this.fluid = FluidRegistry.getFluid(tag.toString());
    }

    public void writeToByteBuf(PacketBuffer buffer) {
        buffer.writeString(FluidRegistry.getFluidName(fluid));
    }

    public void readFromByteBuf(PacketBuffer buffer) {
        fluid = FluidRegistry.getFluid(buffer.readString(65535));
    }
}
