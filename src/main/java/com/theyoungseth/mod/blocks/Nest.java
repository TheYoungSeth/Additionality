package com.theyoungseth.mod.blocks;

import com.mojang.serialization.MapCodec;
import com.theyoungseth.mod.blocks.entities.NestEntity;
import com.theyoungseth.mod.registries.BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Nest extends BaseEntityBlock {
    public static final MapCodec<Nest> CODEC = simpleCodec(Nest::new);
    public static final EnumProperty<Direction> FACING;
    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 3, 15);
    public static final IntegerProperty EGGS = IntegerProperty.create("eggs", 0, 4);

    public Nest(Properties properties) {
        super(properties);
    }

    protected MapCodec<? extends Nest> codec() {
        return CODEC;
    }

    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING)));
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.getValue(FACING)));
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(EGGS);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return state.getValue(EGGS);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NestEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return  level instanceof ServerLevel serverLevel ?
                createTickerHelper(blockEntityType, BlockEntities.NEST.get(), NestEntity::eggTicker) : null;
    }

    @Override
    protected @NotNull InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(stack.is(Items.EGG) && state.getValue(EGGS) < 4) {
            stack.shrink(1);
            level.setBlock(pos, state.setValue(EGGS, state.getValue(EGGS) + 1), 3);
            level.playSound(player, pos, SoundEvents.GRASS_STEP, SoundSource.BLOCKS, 1.7F, .7F);
            return InteractionResult.SUCCESS;
        } else if(!stack.is(Items.EGG) && state.getValue(EGGS) > 0) {
            level.setBlock(pos, state.setValue(EGGS, state.getValue(EGGS) - 1), 3);
            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5F, pos.getY()+ 0.5F, pos.getZ() + 0.5F, new ItemStack(Items.EGG)));
            level.playSound(player, pos, SoundEvents.GRASS_STEP, SoundSource.BLOCKS, 1.7F, .7F);
            return InteractionResult.SUCCESS;

        }

        return InteractionResult.PASS;
    }

    @Override
    @Nullable
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(EGGS, 0);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    static {
        FACING = BlockStateProperties.HORIZONTAL_FACING;
    }


}
