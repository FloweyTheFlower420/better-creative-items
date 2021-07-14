package com.floweytf.bettercreativeitems.gui;

import com.floweytf.bettercreativeitems.container.FluidContainer;
import com.floweytf.bettercreativeitems.tileentity.FluidTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import static com.floweytf.bettercreativeitems.ModMain.id;

public class FluidGui extends GuiContainer
{
    private static final ResourceLocation GUI_CHEST = id("textures/gui/fluid.png");
    private final FluidTileEntity te;

    public FluidGui(FluidTileEntity te)
    {
        super(new FluidContainer());
        this.te = te;

        this.xSize = 179;
        this.ySize = 256;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(this.te.getDisplayName().getUnformattedText(), 8, 6, 16086784);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GUI_CHEST);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
}