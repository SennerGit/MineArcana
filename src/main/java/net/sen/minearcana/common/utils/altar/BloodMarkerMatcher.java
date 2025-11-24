package net.sen.minearcana.common.utils.altar;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sen.minearcana.common.blocks.BloodMarkerBlock;
import net.sen.minearcana.common.blocks.entities.BloodMarkerBlockEntity;

public record BloodMarkerMatcher(EntityType<?> entityType, Level level, BlockPos pos)
        implements IBlockMatcher {

    @Override
    public boolean matches(BlockState state) {
        if (!(state.getBlock() instanceof BloodMarkerBlock)) {
            return false;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof BloodMarkerBlockEntity marker)) {
            return false;
        }

        String requiredId = BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString();
        String actualId = marker.getEntityID();

        return requiredId.equals(actualId);
    }
}
