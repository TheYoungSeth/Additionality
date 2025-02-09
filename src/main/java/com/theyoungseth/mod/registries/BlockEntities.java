package com.theyoungseth.mod.registries;

import com.theyoungseth.mod.Additionality;
import com.theyoungseth.mod.blocks.entities.NestEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Additionality.MODID);

    public static final Supplier<BlockEntityType<NestEntity>> NEST = BLOCK_ENTITIES.register(
            "nest_entity",
            () -> new BlockEntityType<>(
                    NestEntity::new,
                    Blocks.NEST.get()
            )
    );
}
