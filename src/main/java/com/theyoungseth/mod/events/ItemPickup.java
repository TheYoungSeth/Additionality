package com.theyoungseth.mod.events;

import com.theyoungseth.mod.Additionality;
import com.theyoungseth.mod.network.PickupDropJukeboxPayload;
import com.theyoungseth.mod.registries.Items;
import com.theyoungseth.mod.utils.GlobalStaticVariables;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Additionality.MODID)
public class ItemPickup {

    @SubscribeEvent
    public static void ItemEntityPickupEvent(ItemEntityPickupEvent.Post event) {
        if(event.getOriginalStack().is(Items.PORTABLE_JUKEBOX.asItem()) && !event.getPlayer().isLocalPlayer()) {
            PacketDistributor.sendToPlayer((ServerPlayer) event.getPlayer(), new PickupDropJukeboxPayload(GlobalStaticVariables.ITEM_PICKUP_EVENT));
        }
    }
}
