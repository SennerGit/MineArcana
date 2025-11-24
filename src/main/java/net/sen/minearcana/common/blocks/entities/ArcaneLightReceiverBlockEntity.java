package net.sen.minearcana.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.sen.minearcana.common.blocks.ArcaneLightReceiverBlock;
import net.sen.minearcana.common.registries.MineArcanaBlockEntites;
import net.sen.minearcana.common.utils.aspect.Aspect;

public class ArcaneLightReceiverBlockEntity extends BlockEntity {
    private int lastBeamPower = 0;
    private int ticksSinceHit = 0;
    private Aspect aspect;   // later

    public ArcaneLightReceiverBlockEntity(BlockPos pos, BlockState blockState) {
        super(MineArcanaBlockEntites.ARCANA_LIGHT_RECEIVER.get(), pos, blockState);
    }

    public static void serverTicker(Level level, BlockPos pos, BlockState state, ArcaneLightReceiverBlockEntity blockEntity) {
        if (level.isClientSide) return;

        blockEntity.ticksSinceHit++;

        // If no beam hit it this tick → fade out
        if (blockEntity.ticksSinceHit > 2) {
            if (state.getValue(ArcaneLightReceiverBlock.POWER) != 0) {
                level.setBlock(pos, state.setValue(ArcaneLightReceiverBlock.POWER, 0), 3);
                level.updateNeighbourForOutputSignal(pos, state.getBlock());
            }
        }
    }

    /**
     * Called by BeamEngine whenever the beam hits this block.
     */
    public void onBeamHit(int power) {
        lastBeamPower = Math.min(15, power);
        ticksSinceHit = 0;

        if (level != null && !level.isClientSide) {
            BlockState state = getBlockState();

            if (state.getValue(ArcaneLightReceiverBlock.POWER) != lastBeamPower) {
                level.setBlock(
                        worldPosition,
                        state.setValue(ArcaneLightReceiverBlock.POWER, lastBeamPower),
                        3
                );
                level.updateNeighbourForOutputSignal(worldPosition, state.getBlock());
            }
        }
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
