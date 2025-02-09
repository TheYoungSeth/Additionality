package com.theyoungseth.mod.blocks;

import com.theyoungseth.mod.registries.Blocks;
import com.theyoungseth.mod.registries.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CarrotBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class GoldenCarrotBlock extends CarrotBlock {
    public GoldenCarrotBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return Items.GOLDEN_CARROT_SEED;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock().defaultBlockState().equals(Blocks.GOLDEN_FARMLAND.get().defaultBlockState());
    }
}
