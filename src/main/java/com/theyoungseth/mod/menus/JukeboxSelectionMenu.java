package com.theyoungseth.mod.menus;

import com.theyoungseth.mod.Additionality;
import com.theyoungseth.mod.items.PortableJukebox;
import com.theyoungseth.mod.registries.MenuTypes;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class JukeboxSelectionMenu extends AbstractContainerMenu {

    public ListTag discs;
    public PortableJukebox jukebox;

    public JukeboxSelectionMenu(int containerId, Inventory playerInv, ListTag discs, PortableJukebox jukebox) {
        this(containerId, playerInv);
        this.discs = discs;
        this.jukebox = jukebox;
    }
    public JukeboxSelectionMenu(int containerId, Inventory playerInv) {
        super(MenuTypes.JUKEBOX_MENU.get(), containerId);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
