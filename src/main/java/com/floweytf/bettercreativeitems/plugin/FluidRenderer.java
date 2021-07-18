package com.floweytf.bettercreativeitems.plugin;

import com.floweytf.bettercreativeitems.api.IFluidRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidRenderer implements IFluidRenderer {
    @Nullable
    protected Fluid fluid = null;

    private FluidRenderer() {
    }

    private FluidRenderer(@NotNull Fluid fluid) {
        this.fluid = fluid;
    }

    public static FluidRenderer getFromFluid(Fluid fluid) {
        return new FluidRenderer(fluid);
    }

    public static final FluidRenderer EMPTY = new FluidRenderer();


    public String getName() {
        return fluid.getLocalizedName(new FluidStack(fluid, 1));
    }

    public void draw(int x, int y, GuiContainer container) {
        container.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite sprite = container.mc.getTextureMapBlocks().getAtlasSprite(fluid.getStill().toString());
        int rgb = 0xff000000 | fluid.getColor();
        GlStateManager.color(((rgb >> 16) & 0xFF) / 255F, ((rgb >> 8) & 0xFF) / 255F, ((rgb >> 0) & 0xFF) / 255F, 1);
        container.drawTexturedModalRect(x + 1, y + 1, sprite, 16, 16);
    }

    public boolean isEmpty() {
        return fluid == null;
    }

    public FluidStack getAsStack(int amount) {
        if (fluid == null) {
            return null;
        }
        return new FluidStack(fluid, amount);
    }

    public boolean canDrainFluidType(FluidStack other) {
        return other.getFluid() == fluid;
    }
}
