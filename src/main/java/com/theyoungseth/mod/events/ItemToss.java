package com.theyoungseth.mod.events;

import com.theyoungseth.mod.Additionality;
import com.theyoungseth.mod.items.PortableJukebox;
import com.theyoungseth.mod.network.PickupDropJukeboxPayload;
import com.theyoungseth.mod.registries.Items;
import com.theyoungseth.mod.utils.GlobalStaticVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Additionality.MODID)
public class ItemToss {

    @SubscribeEvent
    public static void ItemTossEvent(ItemTossEvent event) {
        if(event.getEntity().getItem().is(Items.PORTABLE_JUKEBOX.asItem()) && !event.getPlayer().isLocalPlayer()) {
            PacketDistributor.sendToPlayer((ServerPlayer) event.getPlayer(), new PickupDropJukeboxPayload(GlobalStaticVariables.ITEM_DROP_EVENT));
        }
    }
}
