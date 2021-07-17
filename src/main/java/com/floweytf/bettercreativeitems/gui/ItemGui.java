package com.floweytf.bettercreativeitems.gui;

import com.floweytf.bettercreativeitems.container.ItemContainer;
import com.floweytf.bettercreativeitems.network.CreativeInventoryPacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.HotbarSnapshot;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.floweytf.bettercreativeitems.Constants.*;

// Code yoinked from MC, just w/ a small extra thing.
@SuppressWarnings("NullableProblems")
public class ItemGui extends GuiContainer {
    private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    public static final InventoryBasic basicInventory = new InventoryBasic("tmp", true, 45);
    private static int selectedTabIndex;
    private float currentScroll;
    private boolean isScrolling;
    private boolean wasClicking;
    private GuiTextField searchField;
    private Slot destroyItemSlot;
    private boolean clearSearch;
    private CreativeCrafting listener;
    private static int tabPage = 0;
    private int maxPages = 0;
    private boolean stupidWorkaround = true;

    public ItemGui(EntityPlayer player) {
        super(new ItemContainer(player));
        player.openContainer = this.inventorySlots;
        this.allowUserInput = true;
        this.ySize = 136;
        this.xSize = 195;
    }

    protected void handleMouseClick(@Nullable Slot slotIn, int slotId, int mouseButton, ClickType type) {
        this.clearSearch = true;
        boolean flag = type == ClickType.QUICK_MOVE;
        type = slotId == -999 && type == ClickType.PICKUP ? ClickType.THROW : type;

        if (slotIn == null && type != ClickType.QUICK_CRAFT) {
            InventoryPlayer playerInventory = this.mc.player.inventory;

            if (!playerInventory.getItemStack().isEmpty()) {
                if (mouseButton == 0) {
                    this.mc.player.dropItem(playerInventory.getItemStack(), true);
                    CreativeInventoryPacket.sendPacket(-1, playerInventory.getItemStack());
                    playerInventory.setItemStack(ItemStack.EMPTY);
                }
                if (mouseButton == 1) {
                    ItemStack itemstack6 = playerInventory.getItemStack().splitStack(1);
                    this.mc.player.dropItem(itemstack6, true);
                    CreativeInventoryPacket.sendPacket(-1, itemstack6);
                }
            }
        }
        else {
            if (slotIn != null && !slotIn.canTakeStack(this.mc.player)) {
                return;
            }
            if (slotIn == this.destroyItemSlot && flag) {
                for (int j = 0; j < this.mc.player.inventoryContainer.getInventory().size(); ++j) {
                    CreativeInventoryPacket.sendPacket(j, ItemStack.EMPTY);
                }
            }
            else if (type != ClickType.QUICK_CRAFT && slotIn.inventory == basicInventory) {
                InventoryPlayer playerInventory = this.mc.player.inventory;
                ItemStack itemstack5 = playerInventory.getItemStack();
                ItemStack itemstack7 = slotIn.getStack();

                if (type == ClickType.SWAP) {
                    if (!itemstack7.isEmpty() && mouseButton >= 0 && mouseButton < 9) {
                        ItemStack itemstack10 = itemstack7.copy();
                        itemstack10.setCount(itemstack10.getMaxStackSize());
                        this.mc.player.inventory.setInventorySlotContents(mouseButton, itemstack10);
                        this.mc.player.inventoryContainer.detectAndSendChanges();
                    }

                    return;
                }

                if (type == ClickType.CLONE) {
                    if (playerInventory.getItemStack().isEmpty() && slotIn.getHasStack()) {
                        ItemStack itemstack9 = slotIn.getStack().copy();
                        itemstack9.setCount(itemstack9.getMaxStackSize());
                        playerInventory.setItemStack(itemstack9);
                    }

                    return;
                }
                if (type == ClickType.THROW) {
                    if (!itemstack7.isEmpty()) {
                        ItemStack itemstack8 = itemstack7.copy();
                        itemstack8.setCount(mouseButton == 0 ? 1 : itemstack8.getMaxStackSize());
                        this.mc.player.dropItem(itemstack8, true);
                        CreativeInventoryPacket.sendPacket(-1, itemstack8);
                    }
                    return;
                }

                if (!itemstack5.isEmpty() && !itemstack7.isEmpty() && itemstack5.isItemEqual(itemstack7) && ItemStack.areItemStackTagsEqual(itemstack5, itemstack7)) {
                    if (mouseButton == 0) {
                        if (flag) {
                            itemstack5.setCount(itemstack5.getMaxStackSize());
                        }
                        else if (itemstack5.getCount() < itemstack5.getMaxStackSize()) {
                            itemstack5.grow(1);
                        }
                    }
                    else {
                        itemstack5.shrink(1);
                    }
                }
                else if (!itemstack7.isEmpty() && itemstack5.isEmpty()) {
                    playerInventory.setItemStack(itemstack7.copy());
                    itemstack5 = playerInventory.getItemStack();

                    if (flag) {
                        itemstack5.setCount(itemstack5.getMaxStackSize());
                    }
                }
                else if (mouseButton == 0) {
                    playerInventory.setItemStack(ItemStack.EMPTY);
                }
                else {
                    playerInventory.getItemStack().shrink(1);
                }
            }
            else if (this.inventorySlots != null) {
                ItemStack itemstack3 = slotIn == null ? ItemStack.EMPTY : this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
                this.inventorySlots.slotClick(slotIn == null ? slotId : slotIn.slotNumber, mouseButton, type, this.mc.player);

                if (Container.getDragEvent(mouseButton) == 2) {
                    for (int k = 0; k < 9; ++k) {
                        CreativeInventoryPacket.sendPacket(36 + k, this.inventorySlots.getSlot(45 + k).getStack());
                    }
                }
                else if (slotIn != null) {
                    ItemStack itemstack4 = this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
                    CreativeInventoryPacket.sendPacket(slotIn.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36, itemstack4);
                    int i = 45 + mouseButton;

                    if (type == ClickType.SWAP) {
                        CreativeInventoryPacket.sendPacket(i - this.inventorySlots.inventorySlots.size() + 9 + 36, itemstack3);
                    }
                    else if (type == ClickType.THROW && !itemstack3.isEmpty()) {
                        ItemStack itemstack2 = itemstack3.copy();
                        itemstack2.setCount(mouseButton == 0 ? 1 : itemstack2.getMaxStackSize());
                        this.mc.player.dropItem(itemstack2, true);
                        CreativeInventoryPacket.sendPacket(-1, itemstack2);
                        this.mc.playerController.sendPacketDropItem(itemstack2);
                    }

                    this.mc.player.inventoryContainer.detectAndSendChanges();
                }
            }
        }
    }

    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        this.searchField = new GuiTextField(0, this.fontRenderer, this.guiLeft + 82, this.guiTop + 6, 80, this.fontRenderer.FONT_HEIGHT);
        this.searchField.setMaxStringLength(50);
        this.searchField.setEnableBackgroundDrawing(false);
        this.searchField.setVisible(false);
        this.searchField.setTextColor(16777215);
        int i = TAB.getTabIndex();
        selectedTabIndex = -1;
        this.setCurrentCreativeTab(CREATIVE_TABS[i]);
        this.listener = new CreativeCrafting(this.mc);
        this.mc.player.inventoryContainer.addListener(this.listener);
        int tabCount = CREATIVE_TABS.length;
        if (tabCount > 12) {
            buttonList.add(new GuiButton(101, guiLeft, guiTop - 50, 20, 20, "<"));
            buttonList.add(new GuiButton(102, guiLeft + xSize - 20, guiTop - 50, 20, 20, ">"));
            maxPages = (int) Math.ceil((tabCount - 12) / 10D);
        }
        ((ItemContainer) inventorySlots).scrollTo(0);
    }

    public void onGuiClosed() {
        super.onGuiClosed();

        if (this.mc.player != null && this.mc.player.inventory != null) {
            this.mc.player.inventoryContainer.removeListener(this.listener);
        }

        Keyboard.enableRepeatEvents(false);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (!CREATIVE_TABS[selectedTabIndex].hasSearchBar()) {
            if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat)) {
                this.setCurrentCreativeTab(CreativeTabs.SEARCH);
            }
            else {
                super.keyTyped(typedChar, keyCode);
            }
        }
        else {
            if (this.clearSearch) {
                this.clearSearch = false;
                this.searchField.setText("");
            }

            if (!this.checkHotbarKeys(keyCode)) {
                if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
                    this.updateCreativeSearch();
                }
                else {
                    super.keyTyped(typedChar, keyCode);
                }
            }
        }
    }

    private void updateCreativeSearch() {
        ItemContainer container = (ItemContainer) this.inventorySlots;
        container.itemList.clear();

        CreativeTabs tab = CREATIVE_TABS[selectedTabIndex];
        if (tab.hasSearchBar() && tab != CreativeTabs.SEARCH) {
            tab.displayAllRelevantItems(container.itemList);
            if (!this.searchField.getText().isEmpty()) {
                //TODO: Make this a SearchTree not a manual search
                String search = this.searchField.getText().toLowerCase(Locale.ROOT);
                java.util.Iterator<ItemStack> itr = container.itemList.iterator();
                while (itr.hasNext()) {
                    ItemStack stack = itr.next();
                    boolean matches = false;
                    for (String line : stack.getTooltip(this.mc.player, this.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL)) {
                        if (TextFormatting.getTextWithoutFormattingCodes(line).toLowerCase(Locale.ROOT).contains(search)) {
                            matches = true;
                            break;
                        }
                    }
                    if (!matches) {
                        itr.remove();
                    }
                }
            }
            this.currentScroll = 0.0F;
            container.scrollTo(0.0F);
            return;
        }

        if (this.searchField.getText().isEmpty()) {
            for (Item item : Item.REGISTRY) {
                item.getSubItems(CreativeTabs.SEARCH, container.itemList);
            }
        }
        else {
            container.itemList.addAll(this.mc.getSearchTree(SearchTreeManager.ITEMS).search(this.searchField.getText().toLowerCase(Locale.ROOT)));
        }

        this.currentScroll = 0.0F;
        container.scrollTo(0.0F);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        CreativeTabs creativetabs = CREATIVE_TABS[selectedTabIndex];

        if (creativetabs != null && creativetabs.drawInForegroundOfTab()) {
            GlStateManager.disableBlend();
            this.fontRenderer.drawString(I18n.format(creativetabs.getTranslatedTabLabel()), 8, 6, creativetabs.getLabelColor());
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            int i = mouseX - this.guiLeft;
            int j = mouseY - this.guiTop;

            for (CreativeTabs creativetabs : CREATIVE_TABS) {
                if (this.isMouseOverTab(creativetabs, i, j)) {
                    return;
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            int i = mouseX - this.guiLeft;
            int j = mouseY - this.guiTop;

            for (CreativeTabs creativetabs : CREATIVE_TABS) {
                if (creativetabs != null && this.isMouseOverTab(creativetabs, i, j)) {
                    this.setCurrentCreativeTab(creativetabs);
                    return;
                }
            }
        }

        super.mouseReleased(mouseX, mouseY, state);
    }

    private boolean needsScrollBars() {
        if (CREATIVE_TABS[selectedTabIndex] == null) return false;
        return CREATIVE_TABS[selectedTabIndex].shouldHidePlayerInventory() && ((ItemContainer) this.inventorySlots).canScroll();
    }

    private void setCurrentCreativeTab(CreativeTabs tab) {
        if (tab == null) return;
        int i = selectedTabIndex;
        selectedTabIndex = tab.getTabIndex();
        ItemContainer container = (ItemContainer) this.inventorySlots;
        this.dragSplittingSlots.clear();
        container.itemList.clear();

        if (tab == CreativeTabs.HOTBAR) {
            for (int j = 0; j < 9; ++j) {
                HotbarSnapshot hotbarsnapshot = this.mc.creativeSettings.getHotbarSnapshot(j);

                if (hotbarsnapshot.isEmpty()) {
                    for (int k = 0; k < 9; ++k) {
                        if (k == j) {
                            ItemStack itemstack = new ItemStack(Items.PAPER);
                            itemstack.getOrCreateSubCompound("CustomCreativeLock");
                            String s = GameSettings.getKeyDisplayString(this.mc.gameSettings.keyBindsHotbar[j].getKeyCode());
                            String s1 = GameSettings.getKeyDisplayString(this.mc.gameSettings.keyBindSaveToolbar.getKeyCode());
                            itemstack.setStackDisplayName((new TextComponentTranslation("inventory.hotbarInfo", s1, s)).getUnformattedText());
                            container.itemList.add(itemstack);
                        }
                        else {
                            container.itemList.add(ItemStack.EMPTY);
                        }
                    }
                }
                else {
                    container.itemList.addAll(hotbarsnapshot);
                }
            }
        }
        else if (tab == TAB) {
            for (Item item : ITEMS) {
                container.itemList.add(new ItemStack(item));
            }
        }
        else if (tab != CreativeTabs.SEARCH) {
            tab.displayAllRelevantItems(container.itemList);
        }

        if (this.searchField != null) {
            if (tab.hasSearchBar()) {
                this.searchField.setVisible(true);
                this.searchField.setCanLoseFocus(false);
                this.searchField.setFocused(true);
                this.searchField.setText("");
                this.searchField.width = tab.getSearchbarWidth();
                this.searchField.x = this.guiLeft + (82 /*default left*/ + 89 /*default width*/) - this.searchField.width;
                this.updateCreativeSearch();
            }
            else {
                this.searchField.setVisible(false);
                this.searchField.setCanLoseFocus(true);
                this.searchField.setFocused(false);
            }
        }

        this.currentScroll = 0.0F;
        container.scrollTo(0.0F);
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0 && this.needsScrollBars()) {
            int j = (((ItemContainer) this.inventorySlots).itemList.size() + 9 - 1) / 9 - 5;

            if (i > 0) {
                i = 1;
            }

            if (i < 0) {
                i = -1;
            }

            this.currentScroll = (float) ((double) this.currentScroll - (double) i / (double) j);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
            ((ItemContainer) this.inventorySlots).scrollTo(this.currentScroll);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        boolean flag = Mouse.isButtonDown(0);
        int i = this.guiLeft;
        int j = this.guiTop;
        int k = i + 175;
        int l = j + 18;
        int i1 = k + 14;
        int j1 = l + 112;

        if (!this.wasClicking && flag && mouseX >= k && mouseY >= l && mouseX < i1 && mouseY < j1) {
            this.isScrolling = this.needsScrollBars();
        }

        if (!flag) {
            this.isScrolling = false;
        }

        this.wasClicking = flag;

        if (this.isScrolling) {
            this.currentScroll = ((float) (mouseY - l) - 7.5F) / ((float) (j1 - l) - 15.0F);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
            ((ItemContainer) this.inventorySlots).scrollTo(this.currentScroll);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        int start = tabPage * 10;
        int end = Math.min(CREATIVE_TABS.length, ((tabPage + 1) * 10) + 2);
        if (tabPage != 0) start += 2;
        boolean rendered = false;

        for (CreativeTabs creativetabs : java.util.Arrays.copyOfRange(CREATIVE_TABS, start, end)) {
            if (creativetabs == null) continue;
            if (this.renderCreativeInventoryHoveringText(creativetabs, mouseX, mouseY)) {
                rendered = true;
                break;
            }
        }
        if (maxPages != 0) {
            String page = String.format("%d / %d", tabPage + 1, maxPages + 1);
            int width = fontRenderer.getStringWidth(page);
            GlStateManager.disableLighting();
            this.zLevel = 300.0F;
            itemRender.zLevel = 300.0F;
            fontRenderer.drawString(page, guiLeft + (xSize / 2) - (width / 2), guiTop - 44, -1);
            this.zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected void renderToolTip(ItemStack stack, int x, int y) {
        if (selectedTabIndex == CreativeTabs.SEARCH.getTabIndex()) {
            List<String> list = stack.getTooltip(this.mc.player, this.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
            CreativeTabs creativetabs = stack.getItem().getCreativeTab();

            if (creativetabs == null && stack.getItem() == Items.ENCHANTED_BOOK) {
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);

                if (map.size() == 1) {
                    Enchantment enchantment = map.keySet().iterator().next();

                    for (CreativeTabs creativetabs1 : CREATIVE_TABS) {
                        if (creativetabs1.hasRelevantEnchantmentType(enchantment.type)) {
                            creativetabs = creativetabs1;
                            break;
                        }
                    }
                }
            }

            if (creativetabs != null) {
                list.add(1, "" + TextFormatting.BOLD + TextFormatting.BLUE + I18n.format(creativetabs.getTranslatedTabLabel()));
            }

            for (int i = 0; i < list.size(); ++i) {
                if (i == 0) {
                    list.set(i, stack.getItem().getForgeRarity(stack).getColor() + list.get(i));
                }
                else {
                    list.set(i, TextFormatting.GRAY + list.get(i));
                }
            }

            net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
            net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);
            this.drawHoveringText(list, x, y, (font == null ? fontRenderer : font));
            net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
        }
        else {
            super.renderToolTip(stack, x, y);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.enableGUIStandardItemLighting();
        CreativeTabs creativetabs = CREATIVE_TABS[selectedTabIndex];
        if (stupidWorkaround) {
            setCurrentCreativeTab(CREATIVE_TABS[TAB.getTabIndex()]);
            stupidWorkaround = false;
        }

        int start = tabPage * 10;
        int end = Math.min(CREATIVE_TABS.length, ((tabPage + 1) * 10 + 2));
        if (tabPage != 0) start += 2;

        for (CreativeTabs creativetabs1 : java.util.Arrays.copyOfRange(CREATIVE_TABS, start, end)) {
            this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);

            if (creativetabs1 == null) continue;
            if (creativetabs1.getTabIndex() != selectedTabIndex) {
                this.drawTab(creativetabs1);
            }
        }

        if (tabPage != 0) {
            if (creativetabs != CreativeTabs.SEARCH) {
                this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);
                drawTab(CreativeTabs.SEARCH);
            }
        }

        this.mc.getTextureManager().bindTexture(creativetabs.getBackgroundImage());
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.searchField.drawTextBox();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.guiLeft + 175;
        int j = this.guiTop + 18;
        int k = j + 112;
        this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);

        if (creativetabs.shouldHidePlayerInventory()) {
            this.drawTexturedModalRect(i, j + (int) ((float) (k - j - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }

        if (creativetabs == null || creativetabs.getTabPage() != tabPage) {
            if (creativetabs != CreativeTabs.SEARCH) {
                return;
            }
        }

        this.drawTab(creativetabs);
    }

    protected boolean isMouseOverTab(CreativeTabs tab, int mouseX, int mouseY) {
        if (tab.getTabPage() != tabPage) {
            if (tab != CreativeTabs.SEARCH) {
                return false;
            }
        }

        int i = tab.getTabColumn();
        int j = 28 * i;
        int k = 0;

        if (tab.isAlignedRight()) {
            j = this.xSize - 28 * (6 - i) + 2;
        }
        else if (i > 0) {
            j += i;
        }

        if (tab.isTabInFirstRow()) {
            k = k - 32;
        }
        else {
            k = k + this.ySize;
        }

        return mouseX >= j && mouseX <= j + 28 && mouseY >= k && mouseY <= k + 32;
    }

    protected boolean renderCreativeInventoryHoveringText(CreativeTabs tab, int mouseX, int mouseY) {
        int i = tab.getTabColumn();
        int j = 28 * i;
        int k = 0;

        if (tab.isAlignedRight()) {
            j = this.xSize - 28 * (6 - i) + 2;
        }
        else if (i > 0) {
            j += i;
        }

        if (tab.isTabInFirstRow()) {
            k = k - 32;
        }
        else {
            k = k + this.ySize;
        }

        if (this.isPointInRegion(j + 3, k + 3, 23, 27, mouseX, mouseY)) {
            this.drawHoveringText(I18n.format(tab.getTranslatedTabLabel()), mouseX, mouseY);
            return true;
        }
        else {
            return false;
        }
    }

    protected void drawTab(CreativeTabs tab) {
        boolean flag = tab.getTabIndex() == selectedTabIndex;
        boolean flag1 = tab.isTabInFirstRow();
        int i = tab.getTabColumn();
        int j = i * 28;
        int k = 0;
        int l = this.guiLeft + 28 * i;
        int i1 = this.guiTop;
        int j1 = 32;

        if (flag) {
            k += 32;
        }

        if (tab.isAlignedRight()) {
            l = this.guiLeft + this.xSize - 28 * (6 - i);
        }
        else if (i > 0) {
            l += i;
        }

        if (flag1) {
            i1 = i1 - 28;
        }
        else {
            k += 64;
            i1 = i1 + (this.ySize - 4);
        }

        GlStateManager.disableLighting();
        GlStateManager.color(1F, 1F, 1F); //Forge: Reset color in case Items change it.
        GlStateManager.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
        this.drawTexturedModalRect(l, i1, j, k, 28, 32);
        this.zLevel = 100.0F;
        this.itemRender.zLevel = 100.0F;
        l = l + 6;
        i1 = i1 + 8 + (flag1 ? 1 : -1);
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        ItemStack itemstack = tab.getIconItemStack();
        this.itemRender.renderItemAndEffectIntoGUI(itemstack, l, i1);
        this.itemRender.renderItemOverlays(this.fontRenderer, itemstack, l, i1);
        GlStateManager.disableLighting();
        this.itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
        }

        if (button.id == 101) {
            tabPage = Math.max(tabPage - 1, 0);
        }
        else if (button.id == 102) {
            tabPage = Math.min(tabPage + 1, maxPages);
        }
    }

    @SideOnly(Side.CLIENT)
    public static class CreativeSlot extends Slot {
        private final Slot slot;

        public CreativeSlot(Slot p_i46313_2_, int index) {
            super(p_i46313_2_.inventory, index, 0, 0);
            this.slot = p_i46313_2_;
        }

        public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
            this.slot.onTake(thePlayer, stack);
            return stack;
        }

        public boolean isItemValid(ItemStack stack) {
            return this.slot.isItemValid(stack);
        }

        public ItemStack getStack() {
            return this.slot.getStack();
        }

        public boolean getHasStack() {
            return this.slot.getHasStack();
        }

        public void putStack(ItemStack stack) {
            this.slot.putStack(stack);
        }

        public void onSlotChanged() {
            this.slot.onSlotChanged();
        }

        public int getSlotStackLimit() {
            return this.slot.getSlotStackLimit();
        }

        public int getItemStackLimit(ItemStack stack) {
            return this.slot.getItemStackLimit(stack);
        }

        @Nullable
        public String getSlotTexture() {
            return this.slot.getSlotTexture();
        }

        public ItemStack decrStackSize(int amount) {
            return this.slot.decrStackSize(amount);
        }

        public boolean isHere(IInventory inv, int slotIn) {
            return this.slot.isHere(inv, slotIn);
        }

        public boolean isEnabled() {
            return this.slot.isEnabled();
        }

        public boolean canTakeStack(EntityPlayer playerIn) {
            return this.slot.canTakeStack(playerIn);
        }

        public net.minecraft.util.ResourceLocation getBackgroundLocation() {
            return this.slot.getBackgroundLocation();
        }

        public void setBackgroundLocation(net.minecraft.util.ResourceLocation texture) {
            this.slot.setBackgroundLocation(texture);
        }

        public void setBackgroundName(@Nullable String name) {
            this.slot.setBackgroundName(name);
        }

        @Nullable
        public net.minecraft.client.renderer.texture.TextureAtlasSprite getBackgroundSprite() {
            return this.slot.getBackgroundSprite();
        }

        public int getSlotIndex() {
            return this.slot.getSlotIndex();
        }

        public boolean isSameInventory(Slot other) {
            return this.slot.isSameInventory(other);
        }
        /*========================================= FORGE END =====================================*/
    }
}
