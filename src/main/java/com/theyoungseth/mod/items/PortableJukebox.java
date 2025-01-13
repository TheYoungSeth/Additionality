package com.theyoungseth.mod.items;

import com.theyoungseth.mod.Additionality;
import com.theyoungseth.mod.menus.JukeboxSelectionMenu;
import com.theyoungseth.mod.network.PickupDropJukeboxPayload;
import com.theyoungseth.mod.screens.JukeboxContainerScreen;
import com.theyoungseth.mod.utils.GlobalStaticVariables;
import com.theyoungseth.mod.utils.MathForDummies;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.Objects;

public class PortableJukebox extends Item {

    public PortableJukebox(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, List<Component> tooltipComponents, TooltipFlag flag) {
        if(stack.get(DataComponents.CONTAINER).getSlots() == 0) {
            if(JukeboxContainerScreen.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("tooltip.additionality.portable_jukebox.shift"));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.additionality.portable_jukebox.non_shift"));
            }
        } else {
            tooltipComponents.add(Component.literal(String.valueOf(stack.get(DataComponents.CONTAINER).getSlots())).append(Component.translatable("tooltip.additionality.portable_jukebox.stored")));
            if (GlobalStaticVariables.currentlyPlaying != null) {
                tooltipComponents.add(Component.translatable("tooltip.additionality.portable_jukebox.currentlyPlaying").append(GlobalStaticVariables.currentlyPlaying));
            }
        }



        super.appendHoverText(stack, ctx, tooltipComponents, flag);
    }

    @Override
    public InteractionResult use(Level level, Player p, InteractionHand hand) {
        if(p.isCrouching()) {
            ItemStack stack = p.getOffhandItem();
            if(stack == null || stack == p.getItemInHand(hand)) return InteractionResult.PASS;
            if(stack.has(DataComponents.JUKEBOX_PLAYABLE) && !level.isClientSide()) {
                List<ItemStack> discs;
                discs = MathForDummies.convertIteratorToList(Objects.requireNonNull(p.getItemInHand(hand).get(DataComponents.CONTAINER)).nonEmptyItemsCopy().iterator());
                if(discs.toString().contains(stack.toString()) && !p.isCreative()) {
                    p.displayClientMessage(Component.translatable("label.additionality.already"), true);
                    return InteractionResult.PASS;
                }
                addMusicDisc(stack, level, p.getItemInHand(hand));
                p.displayClientMessage(Component.translatable("label.additionality.added"), true);
                if(!p.isCreative()) stack.shrink(1);
            }
        } else {
            if (p.getItemInHand(hand).get(DataComponents.CONTAINER).getSlots() == 0) {
                p.displayClientMessage(Component.translatable("label.additionality.no_discs"), true);
            } else {
                p.openMenu(new SimpleMenuProvider(
                        (containerId, playerInventory, player)
                                -> new JukeboxSelectionMenu(containerId, playerInventory),
                        Component.translatable("menu.title.additionality.jukebox_selection_menu")
                ));
            }

        }
        return InteractionResult.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    public static AbstractTickableSoundInstance createSoundInstance(SoundEvent song, SoundSource source, float volume, float pitch, boolean looping, SoundInstance.Attenuation attenuation, LivingEntity entity) {
        return new AbstractTickableSoundInstance(song, source, SoundInstance.createUnseededRandom()) {

            @Override
            public float getPitch() {
                return pitch;
            }

            @Override
            public float getVolume() {
                return volume;
            }

            @Override
            public boolean isLooping() {
                return looping;
            }

            @Override
            public void tick() {
                this.x = entity.getX();
                this.y = entity.getY();
                this.z = entity.getZ();
            }

        };
    }

    public void addMusicDisc(ItemStack disc, Level level, ItemStack currentItem) throws NullPointerException {
        if(disc.getComponents().has(DataComponents.JUKEBOX_PLAYABLE)) {
            List<ItemStack> discs = NonNullList.create();
            discs = MathForDummies.convertIteratorToList(currentItem.get(DataComponents.CONTAINER).nonEmptyItems().iterator());
            discs.add(discs.size(), disc);
            currentItem.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(discs));
        }
    }

    public static class ClientPayloadHandler {
        public static void handleDataOnMain(final PickupDropJukeboxPayload data, final IPayloadContext context) {
            switch (data.event()) {
                case GlobalStaticVariables.ITEM_PICKUP_EVENT -> {
                    Minecraft.getInstance().getSoundManager().resume();
                }
                case GlobalStaticVariables.ITEM_DROP_EVENT -> {
                    Minecraft.getInstance().getSoundManager().pause();
                }
            }
        }
    }
}
