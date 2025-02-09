package com.theyoungseth.mod.blocks.entities;

import com.theyoungseth.mod.blocks.Nest;
import com.theyoungseth.mod.registries.BlockEntities;
import com.theyoungseth.mod.utils.GlobalStaticVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class NestEntity extends BlockEntity {
    public int eggTime;
    public int checkTime;
    public final RandomSource random;
    private static final EntityDimensions ZERO_SIZED_DIMENSIONS = EntityDimensions.fixed(0.0F, 0.0F);

    public NestEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntities.NEST.get(), pos, blockState);
        random = RandomSource.create();
        this.eggTime = GlobalStaticVariables.EGG_TIME;
        this.checkTime = GlobalStaticVariables.CHECK_TIME;
    }

    public static <T extends NestEntity> void eggTicker(Level level, BlockPos blockPos, BlockState blockState, T t) {
        ServerLevel serverLevel = (ServerLevel)level;

        if(--t.checkTime <= 0) {
            t.checkTime = GlobalStaticVariables.CHECK_TIME;
            AABB eggSearchBox = new AABB(blockPos.getX() - 2.5, blockPos.getY(), blockPos.getZ() - 2.5, blockPos.getX() + 2.5, blockPos.getY() + 1, blockPos.getZ() + 2.5);
            List<ItemEntity> itemEntityList = serverLevel.getEntitiesOfClass(ItemEntity.class, eggSearchBox);
            for(ItemEntity item : itemEntityList) {
                if(item.getItem().is(Items.EGG) && item.invulnerableTime <= 0 && blockState.getValue(Nest.EGGS) < 4) {
                    if(item.getItem().getCount() > 1) {
                        item.discard();
                        ItemStack newStack = new ItemStack(Items.EGG, item.getItem().getCount() - 1);
                        ItemEntity newItemEntity = new ItemEntity(serverLevel, item.getX(), item.getY(), item.getZ(), newStack);
                        serverLevel.addFreshEntity(newItemEntity);
                    } else {
                        item.discard();
                    }
                    serverLevel.setBlock(blockPos, blockState.setValue(Nest.EGGS, blockState.getValue(Nest.EGGS) + 1), 3);
                    serverLevel.playSound(null, blockPos, SoundEvents.GRASS_STEP, SoundSource.BLOCKS, 1.7F, .7F);
                    break;
                }
            }
        }

        if(blockState.getValue(Nest.EGGS) > 0) {
            if (--t.eggTime <= 0) {
                serverLevel.playSound(null, blockPos, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 1.0F, (t.random.nextFloat() - t.random.nextFloat()) * 0.2F + 1.0F);
                serverLevel.setBlock(blockPos, blockState.setValue(Nest.EGGS, blockState.getValue(Nest.EGGS) - 1), 3);
                Chicken chicken = EntityType.CHICKEN.create(serverLevel, EntitySpawnReason.TRIGGERED);
                if (chicken != null) {
                    chicken.setAge(-24000);
                    chicken.moveTo(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 0, 0.0F);
                    if (!chicken.fudgePositionAfterSizeChange(ZERO_SIZED_DIMENSIONS)) {
                        return;
                    }
                    serverLevel.addFreshEntity(chicken);
                }
                //needs to be client side im gonna kill myself
                /*for(int i = 0; i < 8; ++i) {
                    level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, Items.EGG.getDefaultInstance()), blockPos.getX(), blockPos.getY() + 0.2F, blockPos.getZ(), ((double)t.random.nextFloat() - (double)0.5F) * 0.08, ((double)t.random.nextFloat() - (double)0.5F) * 0.08, ((double)t.random.nextFloat() - (double)0.5F) * 0.08);
                }*/
                t.eggTime = GlobalStaticVariables.EGG_TIME;
            }
        }

    }
}
