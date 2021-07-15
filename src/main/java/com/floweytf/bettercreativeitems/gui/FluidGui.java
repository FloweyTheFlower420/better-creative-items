package com.floweytf.bettercreativeitems.gui;

import com.floweytf.bettercreativeitems.container.FluidContainer;
import com.floweytf.bettercreativeitems.network.PacketHandler;
import com.floweytf.bettercreativeitems.network.SyncFluidPacket;
import com.floweytf.bettercreativeitems.tileentity.FluidTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.lwjgl.input.Mouse;

import java.io.IOException;

import static com.floweytf.bettercreativeitems.Constants.FLUIDS;
import static com.floweytf.bettercreativeitems.Constants.id;

public class FluidGui extends GuiContainer {
    private static final ResourceLocation GUI_CREATIVE_FLUID = id("textures/gui/fluid.png");
    private final FluidTileEntity te;
    private float startPos = 0;

    public FluidGui(FluidTileEntity te) {
        super(new FluidContainer());
        this.te = te;

        this.xSize = 194;
        this.ySize = 135;
    }

    private int getStartIndex() {
        int i = (FLUIDS.size() + 9 - 1) / 9 - 5;
        int j = (int)((double)(startPos * (float)i) + 0.5D);

        if (j < 0)
            j = 0;

        return j;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(this.te.getDisplayName().getUnformattedText(), 8, 6, 4210752);

        for(int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                int index = getStartIndex() + i;
                if (index * 9 + j < FLUIDS.size())
                    renderFluid(FLUIDS.get(index * 9 + j), 8 + j * 18, 17 + i * 18);
            }
        }

        if(te.getFluid() != null) {
            renderFluid(te.getFluid(), 8, 111);
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
        this.mc.getTextureManager().bindTexture(GUI_CREATIVE_FLUID);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        double scaledPos = startPos * 95;
        this.drawTexturedModalRect(this.guiLeft + 175, this.guiTop + 18 + (int)scaledPos, 195, 0, 12, 15);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        // bound
        mouseX = mouseX - guiLeft;
        mouseY = mouseY - guiTop;
        if(8 < mouseX && mouseX < 169 && 17 < mouseY && mouseY < 106) {
            // do math
            int slotX = (mouseX - 8) / 18;
            int slotY = (mouseY - 17) / 18;

            // obtain fluid at pos
            int index = (slotY + getStartIndex()) * 9 + slotX;
            if(index >= FLUIDS.size()) {
                // clear slot selected
                PacketHandler.INSTANCE.sendToServer(new SyncFluidPacket(FluidTileEntity.emptyFluid(), te.getPos()));
            }
            else {
                // set fluid
                PacketHandler.INSTANCE.sendToServer(new SyncFluidPacket(FluidRegistry.getFluidName(FLUIDS.get(index)), te.getPos()));
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0 && this.needsScrollBars()) {
            int j = (FLUIDS.size() + 9 - 1) / 9 - 5;

            if (i > 0)
                i = 1;

            if (i < 0)
                i = -1;

            this.startPos = (float)((double)this.startPos - (double)i / (double)j);
            this.startPos = MathHelper.clamp(this.startPos, 0.0F, 1.0F);
        }
    }

    private boolean needsScrollBars() {
        return FLUIDS.size() >= 5 * 9;
    }
}