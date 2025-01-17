package com.theyoungseth.mod;

import com.mojang.logging.LogUtils;
import com.theyoungseth.mod.events.EntityJoinWorld;
import com.theyoungseth.mod.events.ItemPickup;
import com.theyoungseth.mod.events.ItemToss;
import com.theyoungseth.mod.registries.Blocks;
import com.theyoungseth.mod.registries.CreativeTabs;
import com.theyoungseth.mod.registries.Items;
import com.theyoungseth.mod.registries.MenuTypes;
import com.theyoungseth.mod.screens.JukeboxContainerScreen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
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

        Blocks.BLOCKS.register(modEventBus);
        Items.ITEMS.register(modEventBus);
        MenuTypes.MENUS.register(modEventBus);
        CreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void setupClient(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(Blocks.GoldenCarrotBlock.get(), RenderType.cutout());
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(MenuTypes.JUKEBOX_MENU.get(), JukeboxContainerScreen::new);

        }
    }
}
