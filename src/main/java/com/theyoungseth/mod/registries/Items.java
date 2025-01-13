package com.theyoungseth.mod.registries;

import com.theyoungseth.mod.Additionality;
import com.theyoungseth.mod.items.PortableJukebox;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.sound.sampled.Port;

public class Items {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Additionality.MODID);

    public static final DeferredItem<Item> PORTABLE_JUKEBOX = ITEMS.registerItem(
            "portable_jukebox",
            PortableJukebox::new,
            new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Additionality.MODID, "portable_jukebox"))).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY).stacksTo(1).rarity(Rarity.UNCOMMON));

}
