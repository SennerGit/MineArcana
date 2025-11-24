package net.sen.minearcana.common.utils.altar;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;

public class AltarStructure {
    // key = tier, value = list of positions, each with a list of valid blocks
    private final Map<Integer, List<List<OffsetBlock>>> tiers;

    public AltarStructure(Map<Integer, List<List<OffsetBlock>>> tiers) {
        this.tiers = tiers;
    }

    /**
     * Check if a specific tier is complete
     */
    public boolean isTierComplete(Level level, BlockPos core, int tier) {
        List<List<OffsetBlock>> tierBlocks = tiers.get(tier);
        if (tierBlocks == null) return false;

        for (List<OffsetBlock> optionsForPosition : tierBlocks) {
            boolean matched = false;
            for (OffsetBlock offset : optionsForPosition) {
                if (offset.matcher().matches(level.getBlockState(core.offset(offset.dx(), offset.dy(), offset.dz())))) {
                    matched = true;
                    break;
                }
            }
            if (!matched) return false;
        }
        return true;
    }

    public int getMaxTier() {
        return tiers.keySet().stream().max(Integer::compareTo).orElse(0);
    }

    public Map<Integer, List<List<OffsetBlock>>> getTiers() {
        return tiers;
    }
}
