package com.theyoungseth.mod;

import com.mojang.logging.LogUtils;
import com.theyoungseth.mod.events.EntityJoinWorld;
import com.theyoungseth.mod.events.ItemPickup;
import com.theyoungseth.mod.events.ItemToss;
import com.theyoungseth.mod.registries.Items;
import com.theyoungseth.mod.registries.MenuTypes;
import com.theyoungseth.mod.screens.JukeboxContainerScreen;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.slf4j.Logger;

@Mod(Additionality.MODID)
public class Additionality {

    public static final String MODID = "additionality";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Additionality(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::setupClient);

        Items.ITEMS.register(modEventBus);
        MenuTypes.MENUS.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void setupClient(FMLClientSetupEvent event) {
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(MenuTypes.JUKEBOX_MENU.get(), JukeboxContainerScreen::new);

        }
    }
}
