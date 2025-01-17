package com.theyoungseth.mod.registries;

import com.theyoungseth.mod.Additionality;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Additionality.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.additionality"))
            .icon(() -> Items.PORTABLE_JUKEBOX.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(Items.PORTABLE_JUKEBOX);
                output.accept(Items.GOLDEN_CARROT_SEED);
                output.accept(Items.GOLDEN_FARMLAND);
                output.accept(Items.GOLDEN_FERTILIZER);
            }).build());

}
