package com.theyoungseth.mod.registries;

import com.theyoungseth.mod.Additionality;
import com.theyoungseth.mod.menus.JukeboxSelectionMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Additionality.MODID);

    public static final Supplier<MenuType<JukeboxSelectionMenu>> JUKEBOX_MENU = MENUS.register(
            "jukebox_menu",
            () -> new MenuType<>(JukeboxSelectionMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
