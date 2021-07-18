package com.floweytf.bettercreativeitems.gui;

import com.floweytf.bettercreativeitems.api.IFluidRenderer;
import com.floweytf.bettercreativeitems.container.FluidContainer;
import com.floweytf.bettercreativeitems.network.PacketHandler;
import com.floweytf.bettercreativeitems.network.SyncFluidPacket;
import com.floweytf.bettercreativeitems.plugin.FluidRendererRegistry;
import com.floweytf.bettercreativeitems.tileentity.FluidTileEntity;
import com.floweytf.bettercreativeitems.plugin.FluidRenderer;
import com.floweytf.bettercreativeitems.utils.SearchedArrayList;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.Collections;

import static com.floweytf.bettercreativeitems.Constants.id;

public class FluidGui extends GuiContainer {
    private static final ResourceLocation GUI_CREATIVE_FLUID = id("textures/gui/fluid.png");
    private final FluidTileEntity te;
    private float currentScrollPos = 0;
    private boolean isScrolling = false;
    private boolean clearSearch = false;
    private GuiTextField searchBar;
    private final SearchedArrayList<IFluidRenderer> searchedFluids = new SearchedArrayList<>(FluidRendererRegistry.getList(), IFluidRenderer::getName);

    public FluidGui(FluidTileEntity te) {
        super(new FluidContainer());
        this.te = te;

        this.xSize = 194;
        this.ySize = 135;

    }

    private int getStartIndex() {
        int i = (searchedFluids.size() + 9 - 1) / 9 - 5;
        int j = (int) ((double) (currentScrollPos * (float) i) + 0.5D);

        if (j < 0) {
            j = 0;
        }

        return j;
    }

    private boolean needsScrollBars() {
        return searchedFluids.size() >= 5 * 9;
    }

    private void renderToolTip(String name, int x, int y) {
        drawHoveringText(Collections.singletonList(name), x, y, fontRenderer);
        net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
    }

    private void renderBackground() {
        this.mc.getTextureManager().bindTexture(GUI_CREATIVE_FLUID);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        double scaledPos = currentScrollPos * 95;
        this.drawTexturedModalRect(this.guiLeft + 175, this.guiTop + 18 + (int) scaledPos, 195, 0, 12, 15);
    }

    private void renderFluids() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                int index = getStartIndex() + i;
                if (index * 9 + j < searchedFluids.size())
                    searchedFluids.get(index * 9 + j).draw(8 + j * 18, 17 + i * 18, this);
            }
        }

        if (!te.getFluidRenderer().isEmpty()) {
            te.getFluidRenderer().draw(8, 111, this);
        }
    }

    private void renderToolTips(int x, int y) {
        if (8 < x && x < 169 && 17 < y && y < 106) {
            // do math
            int slotX = (x - 8) / 18;
            int slotY = (y - 17) / 18;

            // obtain fluid at pos
            int index = (slotY + getStartIndex()) * 9 + slotX;
            if (index < searchedFluids.size()) {
                renderToolTip(searchedFluids.get(index).getName(), x, y);
            }
        }
        else if (8 < x && x < 25 && 111 < y && y < 128) {
            if (!te.getFluidRenderer().isEmpty()) {
                renderToolTip(te.getFluidRenderer().getName(), x, y);
            }
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        searchBar = new GuiTextField(0, this.fontRenderer, 82, 6, 89, this.fontRenderer.FONT_HEIGHT);
        searchBar.setMaxStringLength(50);
        searchBar.setEnableBackgroundDrawing(false);
        searchBar.setVisible(true);
        searchBar.setTextColor(16777215);
        searchBar.setFocused(true);
        searchBar.setCanLoseFocus(false);
        searchBar.setText("");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        renderBackground();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        renderFluids();
        searchBar.drawTextBox();
        renderToolTips(mouseX - guiLeft, mouseY - guiTop);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        // calculate actual position of the mouseX and mouseY in pixel offset on texture
        mouseX = mouseX - guiLeft;
        mouseY = mouseY - guiTop;

        // check if it is in the main search box area
        if (8 < mouseX && mouseX < 169 && 17 < mouseY && mouseY < 106) {
            // do math
            int slotX = (mouseX - 8) / 18;
            int slotY = (mouseY - 17) / 18;

            // obtain fluid at pos
            int index = (slotY + getStartIndex()) * 9 + slotX;
            if (index >= searchedFluids.size()) {
                // clear slot selected
                PacketHandler.INSTANCE.sendToServer(new SyncFluidPacket(FluidRenderer.EMPTY, te.getPos()));
            }
            else {
                // set fluid
                PacketHandler.INSTANCE.sendToServer(new SyncFluidPacket(searchedFluids.get(index), te.getPos()));
            }
        }
        else if (8 < mouseX && mouseX < 25 && 111 < mouseY && mouseY < 128) {
            if (!te.getFluidRenderer().isEmpty()) {
                // clear fluid
                PacketHandler.INSTANCE.sendToServer(new SyncFluidPacket(FluidRenderer.EMPTY, te.getPos()));
            }
        }
        else if (174 < mouseX && mouseX < 187 && 17 < mouseY && mouseY < 128) {
            mouseY = mouseY - 24;
            currentScrollPos = MathHelper.clamp(mouseY, 0, 95) / 95.f;
            isScrolling = true;
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        // calculate actual position of the mouseX and mouseY in pixel offset on texture
        mouseY = mouseY - guiTop;
        if (isScrolling) {
            mouseY = mouseY - 24;
            currentScrollPos = MathHelper.clamp(mouseY, 0, 95) / 95.f;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        isScrolling = false;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0 && this.needsScrollBars()) {
            int j = (searchedFluids.size() + 9 - 1) / 9 - 5;

            if (i > 0) {
                i = 1;
            }

            if (i < 0) {
                i = -1;
            }

            this.currentScrollPos = (float) ((double) this.currentScrollPos - (double) i / (double) j);
            this.currentScrollPos = MathHelper.clamp(this.currentScrollPos, 0.0F, 1.0F);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.clearSearch) {
            this.clearSearch = false;
            this.searchBar.setText("");
            this.searchedFluids.reset();
        }

        if (!this.checkHotbarKeys(keyCode)) {
            if (this.searchBar.textboxKeyTyped(typedChar, keyCode)) {
                searchedFluids.setSearchStr(this.searchBar.getText());
            }
            else {
                super.keyTyped(typedChar, keyCode);
            }
        }
    }
}