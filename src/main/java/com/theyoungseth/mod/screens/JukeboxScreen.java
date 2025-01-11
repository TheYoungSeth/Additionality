package com.theyoungseth.mod.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class JukeboxScreen extends Screen {
    public JukeboxScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();

        int listX = this.width / 2 - 75; // Centered horizontally
        int listY = this.height / 4;    // Top offset
        int listWidth = 150;            // Width of the scroll area
        int listHeight = 100;
    }
    @Override
    public void tick() {
        super.tick();
    }

    // mouseX and mouseY indicate the scaled coordinates of where the cursor is in on the screen
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Background is typically rendered first
        this.renderBackground(graphics, mouseX, mouseY, partialTick);

        // Render things here before widgets (background textures)

        // Then the widgets if this is a direct child of the Screen
        super.render(graphics, mouseX, mouseY, partialTick);


        // Render things after widgets (tooltips)
    }
}
