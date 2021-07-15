package com.floweytf.bettercreativeitems.gui;

import com.floweytf.bettercreativeitems.ModMain;
import com.floweytf.bettercreativeitems.container.FluidContainer;
import com.floweytf.bettercreativeitems.network.PacketHandler;
import com.floweytf.bettercreativeitems.network.SyncFluidPacket;
import com.floweytf.bettercreativeitems.tileentity.FluidTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.io.IOException;

import static com.floweytf.bettercreativeitems.ModMain.FLUIDS;
import static com.floweytf.bettercreativeitems.ModMain.id;

public class FluidGui extends GuiContainer
{
    private static final ResourceLocation GUI_CHEST = id("textures/gui/fluid.png");
    private final FluidTileEntity te;
    private int startPos = 0;

    public FluidGui(FluidTileEntity te)
    {
        super(new FluidContainer());
        this.te = te;

        this.xSize = 194;
        this.ySize = 135;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(this.te.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        for(int i = startPos; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                if (i * 9 + j < ModMain.FLUIDS.size())
                    renderFluid(ModMain.FLUIDS.get(i * 9 + j), 8 + j * 18, 17 + i * 18);
            }
        }
    }

    private void renderFluid(Fluid fluid, int x, int y) {
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite sprite = this.mc.getTextureMapBlocks().getAtlasSprite(fluid.getStill().toString());
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(x + 1, y + 1, sprite, 16, 16);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GUI_CHEST);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        // bound
        if(8 < mouseX && mouseX < 169 && 17 < mouseY && mouseY < 106) {
            // do math
            int slotX = (mouseX - 8) / 18;
            int slotY = (mouseY - 17) / 18;

            // obtain fluid at pos
            int index = (slotY + startPos) * 9 + slotX;
            if(index >= ModMain.FLUIDS.size()) {
                // clear slot selected
                PacketHandler.INSTANCE.sendToServer(new SyncFluidPacket(FluidTileEntity.emptyFluid(), te.getPos()));
            }
            else {
                // set fluid
                PacketHandler.INSTANCE.sendToServer(new SyncFluidPacket(FluidRegistry.getFluidName(FLUIDS.get(index)), te.getPos()));
            }
        }
    }
}