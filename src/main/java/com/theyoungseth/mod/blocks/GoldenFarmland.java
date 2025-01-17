package com.theyoungseth.mod.blocks;

import com.theyoungseth.mod.screens.JukeboxContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.FarmBlock;

import java.util.List;

public class GoldenFarmland extends FarmBlock {
    public GoldenFarmland(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, List<Component> tooltipComponents, TooltipFlag flag) {
        if (JukeboxContainerScreen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.additionality.golden_farmland.shift"));
            tooltipComponents.add(Component.translatable("tooltip.additionality.golden_farmland.shift2"));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.additionality.see_usage"));
        }
    }
}
