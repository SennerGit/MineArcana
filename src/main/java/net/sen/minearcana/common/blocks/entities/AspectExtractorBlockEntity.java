package net.sen.minearcana.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.sen.minearcana.common.registries.MineArcanaBlockEntites;

public class AspectExtractorBlockEntity extends BlockEntity implements BlockEntityTicker {
    public AspectExtractorBlockEntity(BlockPos pos, BlockState blockState) {
        super(MineArcanaBlockEntites.ASPECT_EXTRACTOR.get(), pos, blockState);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {

    }
}
