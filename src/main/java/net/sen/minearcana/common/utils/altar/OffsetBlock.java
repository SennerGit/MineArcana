package net.sen.minearcana.common.utils.altar;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public record OffsetBlock(int dx, int dy, int dz, IBlockMatcher matcher) {
    public static OffsetBlock ofTag(int dx, int dy, int dz, TagKey<Block>... tag) {
        return new OffsetBlock(dx, dy, dz, new TagMatcher(Set.of(tag)));
    }

    public static OffsetBlock ofBlock(int x, int y, int z, Block... block) {
        return new OffsetBlock(x, y, z, new BlockMatcher(Set.of(block)));
    }
}
