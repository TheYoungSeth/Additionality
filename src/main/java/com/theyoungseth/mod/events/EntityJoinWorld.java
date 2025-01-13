package com.theyoungseth.mod.events;

import com.theyoungseth.mod.Additionality;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = Additionality.MODID)
public class EntityJoinWorld {

    @SubscribeEvent
    public static void EntityJoinWorldEvent(EntityJoinLevelEvent event) {
    }
}
