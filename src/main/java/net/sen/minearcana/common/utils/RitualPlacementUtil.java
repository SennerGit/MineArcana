package net.sen.minearcana.common.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public final class RitualPlacementUtil {

    public static boolean canPlace(Level level, BlockPos pos) {
        BlockState clickedState = level.getBlockState(pos);

        // Must be solid
        if (clickedState.isAir() || clickedState.canBeReplaced()) {
            return false;
        }

        // Above must be empty & not another marker
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);

        return aboveState.isAir();
    }

    public static boolean placeMarker(Level level, BlockPos above, BlockState markerState) {
        if (!level.getBlockState(above).isAir()) return false;
        level.setBlock(above, markerState, 3);
        return true;
    }
}
