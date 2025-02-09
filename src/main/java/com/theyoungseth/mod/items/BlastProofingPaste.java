package com.theyoungseth.mod.items;

import com.theyoungseth.mod.Additionality;
import com.theyoungseth.mod.registries.StateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BlastProofingPaste extends Item {

    public BlastProofingPaste(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("tooltip.additionality.blast_proofing_paste"));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        CompoundTag tag = context.getItemInHand().get(DataComponents.CUSTOM_DATA).isEmpty() ? tag = new CompoundTag() : context.getItemInHand().get(DataComponents.CUSTOM_DATA).copyTag();
        BlockState clickedBlock = context.getLevel().getBlockState(context.getClickedPos());
        Level level = context.getLevel();

        ItemStack itemStack = context.getItemInHand();
        if(context.getPlayer().isCrouching()) {
            if(tag.contains("pos")) {
                Iterable<BlockPos> pos = BlockPos.betweenClosed(
                        new BlockPos(tag.getIntArray("pos")[0], tag.getIntArray("pos")[1], tag.getIntArray("pos")[2]),
                        context.getClickedPos());

                pos.forEach(blockPos -> {
                    //Neoforge discord told me you can't set Block Properties dynamically so I had to scrap this :(
                    //BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(context.getLevel().getBlockState(context.getClickedPos()).getBlock()).explosionResistance(1000000.0F).setId(context.getLevel().getBlockState(context.getClickedPos()).getBlockHolder().getKey());
                    //context.getLevel().setBlock(blockPos, new Block(properties).defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
                });
                context.getItemInHand().set(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            } else {
                tag.putIntArray("pos", new int[]{context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ()});
                context.getItemInHand().set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                context.getPlayer().displayClientMessage(Component.literal("First Block Saved!"), true);
            }

        } else {
            context.getPlayer().displayClientMessage(Component.literal("bomboclart it didn't work :("), true);
            context.getPlayer().displayClientMessage(Component.literal(clickedBlock.getValue(StateProperties.BLAST_RESISTANT).toString()), false);
            clickedBlock.setValue(StateProperties.BLAST_RESISTANT, false);
            level.setBlock(context.getClickedPos(), clickedBlock, 11);
            //context.getPlayer().displayClientMessage(Component.literal(clickedBlock.getValue(StateProperties.BLAST_RESISTANT).toString()), false);


            //context.getLevel().setBlock(context.getClickedPos(), Blocks.AIR.defaultBlockState(), 3);
        }



        Additionality.LOGGER.info(context.getClickedPos().toShortString());
        return InteractionResult.PASS;


    }
}
