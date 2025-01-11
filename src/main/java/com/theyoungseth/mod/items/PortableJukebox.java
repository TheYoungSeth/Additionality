package com.theyoungseth.mod.items;

import com.theyoungseth.mod.Additionality;
import com.theyoungseth.mod.menus.JukeboxSelectionMenu;
import com.theyoungseth.mod.registries.Items;
import net.minecraft.advancements.critereon.ItemJukeboxPlayablePredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.logging.Logger;

public class PortableJukebox extends Item {

    private final ListTag discs;

    public PortableJukebox(Properties properties) {
        super(properties);
        discs = new ListTag();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    }

    @Override
    public InteractionResult use(Level level, Player p, InteractionHand hand) {
        if(p.isCrouching()) {
            ItemStack stack = p.getOffhandItem();
            if(stack.has(DataComponents.JUKEBOX_PLAYABLE) && !level.isClientSide()) {
                addMusicDisc(stack);
                p.displayClientMessage(Component.literal(getDiscTag(0).toString() + discs.size()), false);
                //stack.shrink(1);
            }
        } else {
            if (discs == null || discs.isEmpty()) {
                p.displayClientMessage(Component.literal("No discs available."), true);
            } else {
                p.openMenu(new SimpleMenuProvider(
                        (containerId, playerInventory, player)
                                -> new JukeboxSelectionMenu(containerId, playerInventory, discs, this),
                        Component.translatable("menu.title.additionality.jukebox_selection_menu")
                ));
            }

        }
        return InteractionResult.SUCCESS;
    }

    public void addMusicDisc(ItemStack disc) throws NullPointerException {
        if(disc.getComponents().has(DataComponents.JUKEBOX_PLAYABLE)) {
            Level level = Minecraft.getInstance().level;
            assert level != null;
            discs.add(discs.size(), disc.save(level.registryAccess()));
        }
    }

    public ListTag getDiscs() {
        return discs;
    }

    public CompoundTag getDiscTag(int i) {
        return discs.getCompound(i);
    }
}
