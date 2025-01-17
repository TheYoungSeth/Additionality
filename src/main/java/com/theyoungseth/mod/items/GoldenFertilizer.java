package com.theyoungseth.mod.items;

import com.theyoungseth.mod.registries.Blocks;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

public class GoldenFertilizer extends Item {
    public GoldenFertilizer(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Block clickedBlock = context.getLevel().getBlockState(context.getClickedPos()).getBlock();
        if(clickedBlock.defaultBlockState().equals(net.minecraft.world.level.block.Blocks.FARMLAND.defaultBlockState())) {
            context.getLevel().setBlock(context.getClickedPos(), Blocks.GOLDEN_FARMLAND.get().defaultBlockState(), 3);
            context.getItemInHand().shrink(1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
