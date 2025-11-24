package net.sen.minearcana.common.utils.altar;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public record TagMatcher(Set<TagKey<Block>> tags) implements IBlockMatcher {
    @Override
    public boolean matches(BlockState state) {
        for (TagKey tag : tags) {
            if (state.is(tag)) return true;
        }
        return false;
    }
}
