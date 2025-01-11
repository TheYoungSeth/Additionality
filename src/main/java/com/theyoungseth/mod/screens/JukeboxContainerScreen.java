package com.theyoungseth.mod.screens;

import com.theyoungseth.mod.Additionality;
import com.theyoungseth.mod.items.PortableJukebox;
import com.theyoungseth.mod.menus.JukeboxSelectionMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;

import static net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion.MOD_ID;

public class JukeboxContainerScreen extends AbstractContainerScreen<JukeboxSelectionMenu> {

    private static final ResourceLocation BACKGROUND_LOCATION = ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/gui/container/portable_jukebox.png");
    public ScrollableListWidget buttonList;
    public ScrollableListWidget.ButtonEntry selected;

    public JukeboxContainerScreen(JukeboxSelectionMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        if (menu.discs == null || menu.discs.isEmpty()) {
            Additionality.LOGGER.warn("Discs list is null or empty in JukeboxContainerScreen");
            return;
        } else {
            Additionality.LOGGER.warn("YIPPIEEEEEE");
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(
                RenderType::guiTextured,
                BACKGROUND_LOCATION,
                this.leftPos, this.topPos,
                0, 0,
                this.imageWidth, this.imageHeight,
                256, 256
        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int btn) {
        if (isMouseOver(mouseX, mouseY)) {
            buttonList.active = true;
            return buttonList.mouseClicked(mouseX, mouseY, btn);
        }
        return super.mouseClicked(mouseX, mouseY, btn);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int btn, double deltaX, double deltaY) {
        if (isMouseOver(mouseX, mouseY)) {
            buttonList.active = true;
            return buttonList.mouseDragged(mouseX, mouseY, btn, deltaX, deltaY);
        }
        return super.mouseDragged(mouseX, mouseY, btn, deltaX, deltaY);
    }

    @Override
    protected void init() {
        super.init();

        int left = this.leftPos + 5;
        int top = this.topPos + 25;
        int right = imageWidth - 44;
        int bottom = this.imageHeight + 31;
        int itemHeight = imageHeight - 21;

        buttonList = new ScrollableListWidget(this, left, top, right, bottom, 0, itemHeight, menu.discs);

        this.addRenderableWidget(buttonList);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBg(graphics, partialTick, mouseX, mouseY);
        super.render(graphics, mouseX, mouseY, partialTick);

        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double delta, double dontknowdontcare, double mouseX, double mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            return buttonList.mouseScrolled(delta, dontknowdontcare, mouseX, mouseY);
        }
        return super.mouseScrolled(delta, dontknowdontcare, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }

    public void setSelected(ScrollableListWidget.ButtonEntry buttonEntry) {
        selected = buttonEntry;
    }

    public class ScrollableListWidget extends ObjectSelectionList<JukeboxContainerScreen.ScrollableListWidget.ButtonEntry> {

        private final int listWidth;
        private int _spacing;
        private JukeboxContainerScreen parent;
        private JukeboxContainerScreen.ScrollableListWidget.ButtonEntry selected;

        public ScrollableListWidget(JukeboxContainerScreen parent, int x, int y, int listWidth, int itemHeight, int top, int bottom, ListTag discs) {
            super(Minecraft.getInstance(), listWidth, bottom - top, top, parent.getFont().lineHeight * 2 + 8);
            this.parent = parent;
            this.listWidth = listWidth;

            this.setPosition(x, y);

            if (discs == null || discs.isEmpty()) {
                Additionality.LOGGER.warn("Discs list is null or empty!");
                return;
            }

            int i = 0;
            for (Tag tag : discs) {
                this.addEntry(new JukeboxContainerScreen.ScrollableListWidget.ButtonEntry(tag.toString(), i * 20, parent, tag));
                i++;
                Additionality.LOGGER.info("Button: " + i);
            }
            this.setFocused(true);
        }

        @Override
        public boolean isFocused() {
            return parent.getFocused() == this;
        }

        @Override
        public void setSelected(@Nullable JukeboxContainerScreen.ScrollableListWidget.ButtonEntry entry) {
            super.setSelected(entry);
            if (entry != null) {
                parent.selected = entry;
            }
        }

        @Override
        protected void renderSelection(GuiGraphics guiGraphics, int top, int width, int height, int outerColor, int innerColor) {
        }

        @Override
        protected void renderListBackground(GuiGraphics guiGraphics) {

        }

        @Override
        public boolean mouseScrolled(double delta, double dontknowdontcare, double mouseX, double mouseY) {
            if (this.isMouseOver(mouseX, mouseY)) {
                double currentScroll = scrollAmount();
                setScrollAmount(currentScroll - delta * 20); // Adjust scroll speed if necessary
                return true;
            }
            return super.mouseScrolled(delta, dontknowdontcare, mouseX, mouseY);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int btn) {
            if (isMouseOverScrollbar(mouseX, mouseY)) {
                double newScroll = getNewScroll(mouseY);
                setScrollAmount(newScroll);
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, btn);
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            if (isMouseOverScrollbar(mouseX, mouseY)) {
                double newScroll = getNewScroll(mouseY);
                setScrollAmount(newScroll);
                return true;
            }
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }

        private double getNewScroll(double mouseY) {
            double relativeClickPosition = mouseY - this.getY();
            double scrollPercentage = relativeClickPosition / getHeight();

            int maxScroll = getMaxScroll();
            double newScroll = maxScroll * scrollPercentage;
            return newScroll;
        }

        private int getMaxScroll() {
            int contentHeight = contentHeight();
            int visibleHeight = this.getHeight();
            return Math.max(0, contentHeight - visibleHeight);
        }

        public boolean isMouseOverScrollbar(double mouseX, double mouseY) {
            int scrollbarWidth = 6;
            int scrollbarLeft = this.scrollBarX();
            int scrollbarRight = scrollbarLeft + scrollbarWidth;
            int top = topPos;
            int bottom = getBottom();

            return mouseX >= scrollbarLeft && mouseX <= scrollbarRight && mouseY >= top && mouseY <= bottom;
        }

        @Override
        protected int scrollBarX() {
            return this.getX() + this.getWidth() + 6;
        }

        @Override
        public int getRowWidth() {
            return listWidth;
        }

        public int spacing() {
            return _spacing;
        }

        public void setSpacing(int i) {
            _spacing = i;
        }

        public class ButtonEntry extends ObjectSelectionList.Entry<JukeboxContainerScreen.ScrollableListWidget.ButtonEntry> {

            private final String label;
            private final int yOffset;
            private Button button;
            private JukeboxContainerScreen parent;

            public ButtonEntry(String label, int yOffset, JukeboxContainerScreen parent, Tag tag) {
                this.label = label;
                this.yOffset = yOffset;
                this.parent = parent;

                assert Minecraft.getInstance().player != null;
                ItemStack stack = ItemStack.parse(Minecraft.getInstance().player.registryAccess(), tag).get();

                this.button = new Button.Builder(Component.literal(label),
                        btn -> onPress(btn, stack)
                        ).bounds(0, 0, 120, 20)
                        .build();
            }

            public void onPress(Button btn, ItemStack stack) {
                assert Minecraft.getInstance().player != null;
                Minecraft.getInstance().player.playSound(stack.get(DataComponents.JUKEBOX_PLAYABLE).song().holder().get().value().soundEvent().value(), 1f, 1f);
            }

            @Override
            public Component getNarration() {
                return Component.empty();
            }

            @Override
            public boolean mouseClicked(double p_95798_, double p_95799_, int p_95800_) {
                this.button.onPress();
                if (p_95800_ == 0) {
                    parent.setSelected(this);
                    return true;
                }
                else {
                    return false;
                }
            }

            @Override
            public void setFocused(boolean focused) {
                if (focused) {
                    JukeboxContainerScreen.ScrollableListWidget.this.setSelected(this);
                }
            }

            @Override
            public boolean isFocused() {
                return JukeboxContainerScreen.ScrollableListWidget.this.getSelected() == this;
            }

            @Override
            public void render(GuiGraphics guiGraphics, int entryIdx, int top, int left,
                               int entryWidth, int entryHeight, int mouseX, int mouseY,
                               boolean isMouseOver, float partialTick) {

                int buttonX = left + (entryWidth - this.button.getWidth()) / 2;
                int buttonY = top + (entryHeight - this.button.getHeight()) / 2;

                this.button.setX(buttonX);
                this.button.setY(buttonY);

                this.button.render(guiGraphics, mouseX, mouseY, partialTick);

                if (isMouseOver) {
                    //guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("Hovered: " + this.label), left, top, 0xFFFF00); // Highlighted text
                }
            }

        }
    }


}
