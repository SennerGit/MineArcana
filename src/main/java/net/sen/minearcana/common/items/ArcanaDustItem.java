package net.sen.minearcana.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.sen.minearcana.common.blocks.entities.BloodMarkerBlockEntity;
import net.sen.minearcana.common.registries.MineArcanaBlocks;
import net.sen.minearcana.common.registries.MineArcanaDataComponents;
import net.sen.minearcana.common.utils.RitualPlacementUtil;

public class ArcanaDustItem extends Item {
    public ArcanaDustItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (!RitualPlacementUtil.canPlace(level, pos))
            return InteractionResult.FAIL;

        BlockPos placePos = pos.above();
        boolean placed = RitualPlacementUtil.placeMarker(level, placePos,
                MineArcanaBlocks.ARCANA_MARKER.get().defaultBlockState());

        if (!placed)
            return InteractionResult.FAIL;

        if (!context.getPlayer().isCreative())
            context.getItemInHand().shrink(1); // dust is consumed

        return InteractionResult.SUCCESS;
    }
}
