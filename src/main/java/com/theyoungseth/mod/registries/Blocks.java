package com.theyoungseth.mod.registries;

import com.theyoungseth.mod.Additionality;
import com.theyoungseth.mod.blocks.GoldenCarrotBlock;
import com.theyoungseth.mod.blocks.GoldenFarmland;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Blocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Additionality.MODID);

    public static final DeferredBlock<Block> GoldenCarrotBlock = BLOCKS.registerBlock(
            "golden_carrots",
            GoldenCarrotBlock::new,
            BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Additionality.MODID, "golden_carrots")))
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .pushReaction(PushReaction.DESTROY)
    );

    public static final DeferredBlock<Block> GOLDEN_FARMLAND = BLOCKS.registerBlock(
            "golden_farmland",
            GoldenFarmland::new,
            BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Additionality.MODID, "golden_farmland")))
                    .mapColor(MapColor.DIRT)
                    .randomTicks()
                    .strength(0.6F)
                    .sound(SoundType.GRAVEL)
    );

}
