package com.theyoungseth.mod.menus;

import com.theyoungseth.mod.registries.MenuTypes;
import com.theyoungseth.mod.utils.MathForDummies;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class JukeboxSelectionMenu extends AbstractContainerMenu {

    public List<ItemStack> discs;

    public JukeboxSelectionMenu(int containerId, Inventory playerInv) {
        super(MenuTypes.JUKEBOX_MENU.get(), containerId);

        try {
            discs = MathForDummies.convertIteratorToList(playerInv.player.getItemInHand(playerInv.player.getUsedItemHand()).get(DataComponents.CONTAINER).nonEmptyItems().iterator());
        } catch(Exception ignored) {}

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
