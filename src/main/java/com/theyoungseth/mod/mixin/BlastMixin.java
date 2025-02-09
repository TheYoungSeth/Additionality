package com.theyoungseth.mod.mixin;

import com.theyoungseth.mod.registries.StateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlastMixin {

    @Shadow
    private BlockState defaultBlockState;

    @Unique
    private static final BooleanProperty BLAST_RESISTANT = StateProperties.BLAST_RESISTANT;

    @Shadow
    public final BlockState defaultBlockState() {
        return this.defaultBlockState.setValue(BLAST_RESISTANT, false);
    }



    @Inject(method = "createBlockStateDefinition", at = @At("HEAD"))
    protected void stateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(BLAST_RESISTANT);
    }

    //getStateForPlacement(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;
    public BlockState getStateForPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        return this.defaultBlockState().setValue(BLAST_RESISTANT, false);
    }
}
