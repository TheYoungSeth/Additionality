package com.theyoungseth.mod.screens;

import com.theyoungseth.mod.Additionality;
import com.theyoungseth.mod.items.PortableJukebox;
import com.theyoungseth.mod.menus.JukeboxSelectionMenu;
import com.theyoungseth.mod.utils.GlobalStaticVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.*;
import org.joml.Vector2d;

import javax.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class JukeboxContainerScreen extends AbstractContainerScreen<JukeboxSelectionMenu> {

    private static final ResourceLocation BACKGROUND_LOCATION = ResourceLocation.fromNamespaceAndPath(Additionality.MODID, "textures/gui/container/portable_jukebox.png");
    public ScrollableListWidget buttonList;
    public ScrollableListWidget.ButtonEntry selected;
    public Inventory playerInv;
    public int buttonHeight;
    public int buttonSpacing;
    public Vector2d playingPosition;
    public Vector2d currentlyPlayingPosition;

    public JukeboxContainerScreen(JukeboxSelectionMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.playerInv = playerInventory;
    }

    @Override
    protected void init() {
        super.init();

        //general
        imageWidth = 230;
        imageHeight = 190;
        leftPos = (width - imageWidth) / 2;
        topPos = (height - imageHeight) / 2;
        titleLabelX = 140;
        titleLabelY = 174;
        buttonHeight = 17;
        buttonSpacing = buttonHeight + (buttonHeight / 3);

        playingPosition = new Vector2d(180, 60);
        currentlyPlayingPosition = new Vector2d(playingPosition.x, playingPosition.y + 10);

        //Additionality.LOGGER.info("\n WIDTH: " + imageWidth + "\n HEIGHT: " + imageHeight);

        //buttonList
        int left = leftPos + 12;
        int top = topPos + 55;
        int right = imageWidth - 115;
        int bottom = imageHeight + 66;
        int itemHeight = imageHeight - 65;

        buttonList = new ScrollableListWidget(this, left, top, right, bottom, 0, itemHeight, menu.discs);

        addRenderableWidget(buttonList);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(
                RenderType::guiTextured,
                BACKGROUND_LOCATION,
                this.leftPos, this.topPos,
                0, 0,
                this.imageWidth, this.imageHeight,
                this.imageWidth, this.imageHeight
        );
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBg(graphics, partialTick, mouseX, mouseY);
        super.render(graphics, mouseX, mouseY, partialTick);

        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 16777215, false);
        guiGraphics.drawCenteredString(this.font, Component.translatable("label.additionality.playing"), (int) this.playingPosition.x, (int) playingPosition.y, 16777215);
        if(GlobalStaticVariables.currentlyPlaying != null) guiGraphics.drawCenteredString(this.font, GlobalStaticVariables.currentlyPlaying, (int) this.currentlyPlayingPosition.x, (int) currentlyPlayingPosition.y, 16777215); else guiGraphics.drawCenteredString(this.font, Component.literal("§cnothing."), (int) this.currentlyPlayingPosition.x, (int) currentlyPlayingPosition.y, 16777215);
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
    public boolean mouseScrolled(double delta, double dontknowdontcare, double mouseX, double mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            return buttonList.mouseScrolled(delta, dontknowdontcare, mouseX, mouseY);
        }
        return super.mouseScrolled(delta, dontknowdontcare, mouseX, mouseY);
    }



    public void setSelected(ScrollableListWidget.ButtonEntry buttonEntry) {
        selected = buttonEntry;
    }

    public class ScrollableListWidget extends ObjectSelectionList<ScrollableListWidget.ButtonEntry> {

        private final int listWidth;
        private JukeboxContainerScreen parent;
        private ButtonEntry selected;

        public ScrollableListWidget(JukeboxContainerScreen parent, int x, int y, int listWidth, int itemHeight, int top, int bottom, List<ItemStack> discs) {
            super(Minecraft.getInstance(), listWidth, bottom - top, top, parent.buttonSpacing);
            this.parent = parent;
            this.listWidth = listWidth;

            setPosition(x, y);

            if (discs == null || discs.isEmpty()) {
                Additionality.LOGGER.warn("Discs list is null or empty!");
                return;
            }

            int i = 0;
            for (ItemStack stack : discs) {
                List<Component> list = stack.getTooltipLines(Item.TooltipContext.of(playerInv.player.level()), playerInv.player, Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
                Component component = null;
                for(int t = 0; t < list.size(); t++) {
                    if (list.get(t).getString().contains("-")) component = list.get(t);
                }
                if(component == null) component = list.getLast();
                component = component.copy().setStyle(list.get(0).getStyle());
                addEntry(new ButtonEntry((MutableComponent) component, i * buttonHeight, parent, stack));
                i++;
            }
            setFocused(true);
        }

        @Override
        public boolean isFocused() {
            return parent.getFocused() == this;
        }

        @Override
        public void setSelected(@Nullable ButtonEntry entry) {
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
                setScrollAmount(currentScroll - delta * buttonHeight / 2); // Adjust scroll speed if necessary
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

        public class ButtonEntry extends Entry<ButtonEntry> {
            private final MutableComponent label;
            private final int yOffset;
            private Button button;
            private JukeboxContainerScreen parent;

            public ButtonEntry(MutableComponent label, int yOffset, JukeboxContainerScreen parent, ItemStack stack) {
                this.label = label;
                this.yOffset = yOffset;
                this.parent = parent;

                this.button = new Button.Builder(label,
                        btn -> onPress(btn, stack, parent, GlobalStaticVariables.currentlyPlayingPortableJukeboxSong)
                        ).bounds(0, 0, 105, buttonHeight)
                        .build();
            }

            public void onPress(Button btn, ItemStack stack, JukeboxContainerScreen parent, SoundInstance instance) {
                //TO-DO: ADD STOP BUTTON
                SoundManager manager = Minecraft.getInstance().getSoundManager();
                playButtonClickSound(manager);
                SoundInstance preInstance = instance;
                SoundEvent song = JukeboxSong.fromStack(Minecraft.getInstance().level.registryAccess(), stack).get().value().soundEvent().value();
                instance = PortableJukebox.createSoundInstance(song, SoundSource.RECORDS, 1f, 1f, true, SoundInstance.Attenuation.LINEAR, parent.playerInv.player);
                if(preInstance != null) {
                    manager.stop(preInstance);
                    GlobalStaticVariables.currentlyPlaying = null;
                    GlobalStaticVariables.currentlyPlayingPortableJukeboxSong = null;
                    if(!Objects.equals(preInstance.toString(), instance.toString())) {
                        manager.play(instance);
                        GlobalStaticVariables.currentlyPlaying = btn.getMessage();
                        GlobalStaticVariables.currentlyPlayingPortableJukeboxSong = instance;
                    }
                } else {
                    GlobalStaticVariables.currentlyPlayingPortableJukeboxSong = instance;
                    GlobalStaticVariables.currentlyPlaying = btn.getMessage();
                    manager.play(instance);
                }


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
                    ScrollableListWidget.this.setSelected(this);
                }
            }

            @Override
            public boolean isFocused() {
                return ScrollableListWidget.this.getSelected() == this;
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
