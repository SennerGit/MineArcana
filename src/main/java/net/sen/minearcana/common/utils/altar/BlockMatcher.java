package net.sen.minearcana.common.utils.altar;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public record BlockMatcher(Set<Block> blocks) implements IBlockMatcher {
    @Override
    public boolean matches(BlockState state) {
        return blocks.contains(state.getBlock());
    }
}
