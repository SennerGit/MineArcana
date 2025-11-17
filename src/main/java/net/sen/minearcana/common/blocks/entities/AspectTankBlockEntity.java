package net.sen.minearcana.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.sen.minearcana.common.registries.MineArcanaBlockEntites;

public class AspectTankBlockEntity extends BlockEntity implements BlockEntityTicker {
    public static final int MAX_FLUID = 10 * FluidType.BUCKET_VOLUME;
    public FluidStack fluidStack = FluidStack.EMPTY;

    public AspectTankBlockEntity(BlockPos pos, BlockState blockState) {
        super(MineArcanaBlockEntites.ASPECT_TANK.get(), pos, blockState);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {

    }

    public boolean addWater(FluidStack stack) {
        // Only accept water
        if (stack.getFluid() != Fluids.WATER) return false;

        // Only one bucket worth (1000 mB)
        if (fluidStack.isEmpty()) {
            fluidStack = stack.copy();
            fluidStack.setAmount(FluidType.BUCKET_VOLUME);
            setChanged();
            return true;
        }

        // Already full
        return false;
    }

    public FluidStack getFluid() {
        return fluidStack;
    }

    public void empty() {
        fluidStack = FluidStack.EMPTY;
        setChanged();
    }
}
