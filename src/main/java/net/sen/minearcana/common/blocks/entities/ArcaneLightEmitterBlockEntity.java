package net.sen.minearcana.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.sen.minearcana.common.registries.MineArcanaBlockEntites;
import net.sen.minearcana.common.utils.lightbeams.Beam;
import net.sen.minearcana.common.utils.lightbeams.BeamEngine;

public class ArcaneLightEmitterBlockEntity extends BlockEntity {

    public ArcaneLightEmitterBlockEntity(BlockPos pos, BlockState state) {
        super(MineArcanaBlockEntites.ARCANA_LIGHT_EMITTER.get(), pos, state);
    }

    /**
     * Server tick — not required yet.
     */
    public static void serverTicker(
            Level level, BlockPos pos, BlockState state, ArcaneLightEmitterBlockEntity be) {
        // No ticking logic yet
    }

    /**
     * Emits a beam using the current block FACING.
     */
    public void emit(int powerLevel) {
        if (level == null || level.isClientSide) return;

        Direction facing = getBlockState().getValue(BlockStateProperties.FACING);

        Vec3 dir = new Vec3(facing.getStepX(), facing.getStepY(), facing.getStepZ());

        Beam beam = new Beam(
                level,
                worldPosition,
                dir,
                powerLevel,
                powerLevel * 4,
                DyeColor.WHITE,
                null,
                level.getGameTime()
        );

        BeamEngine.get(level).addBeam(beam);
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
    }
}
