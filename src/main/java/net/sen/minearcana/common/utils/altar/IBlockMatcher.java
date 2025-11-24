package net.sen.minearcana.common.utils.altar;

import net.minecraft.world.level.block.state.BlockState;

public sealed interface IBlockMatcher permits TagMatcher, BlockMatcher, BloodMarkerMatcher {
    boolean matches(BlockState state);
}
