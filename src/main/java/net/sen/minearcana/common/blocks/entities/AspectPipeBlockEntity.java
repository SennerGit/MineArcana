package net.sen.minearcana.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.registries.MineArcanaBlockEntites;
import net.sen.minearcana.common.utils.aspect.Aspect;
import net.sen.minearcana.common.utils.aspect.AspectStack;
import net.sen.minearcana.common.utils.pipelogic.IAspectHandler;
import net.sen.minearcana.common.utils.pipelogic.IAspectPipeHandler;
import net.sen.minearcana.common.utils.pipelogic.PipeMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AspectPipeBlockEntity extends BlockEntity implements IAspectHandler, IAspectPipeHandler {
    private AspectStack aspect = AspectStack.empty();
    private final int transferAmount = 10;
    private static final int CAPACITY = 10;

    public static final String ASPECT_TAG = "Aspect";
    public Map<BlockPos, PipeMode> neighbourPos = new HashMap<>();

    public AspectPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(MineArcanaBlockEntites.ASPECT_PIPE.get(), pos, blockState);
    }

    public static void serverTicker(Level level, BlockPos pos, BlockState state, AspectPipeBlockEntity blockEntity) {
        blockEntity.handleTransfer();
    }

    /**
     PIPE LOGIC
     */
    public void onNetworkChanged() {
        // re-scan neighbors
        for (Direction dir : Direction.values()) {
            tryTransfer(getBlockPos().relative(dir));
        }
    }

    private void tryTransfer(BlockPos neighbourPos) {
        this.neighbourPos.clear();
        if (level.getBlockEntity(neighbourPos) instanceof IAspectHandler aspectHandler) {
            if (level.getBlockEntity(neighbourPos) instanceof IAspectPipeHandler) {
                this.neighbourPos.put(neighbourPos, PipeMode.PIPE);
            } else if (aspectHandler.canInsert() && aspectHandler.canExtract()) {
                this.neighbourPos.put(neighbourPos, PipeMode.BOTH);
            } else if (aspectHandler.canInsert()) {
                this.neighbourPos.put(neighbourPos, PipeMode.INPUT);
            } else if (aspectHandler.canExtract()) {
                this.neighbourPos.put(neighbourPos, PipeMode.OUTPUT);
            } else {
                this.neighbourPos.put(neighbourPos, PipeMode.DISABLED);
            }
        }
    }

    //TODO: Implement transfer logic
    private void handleTransfer() {

    }

    /**
     STORAGE LOGIC
     */
    public AspectStack addAspect(AspectStack incoming) {
        if (incoming == null || incoming.isEmpty()) return incoming;

        // If tank is empty → fill directly
        if (aspect.isEmpty()) {
            int accepted = Math.min(incoming.getAmount(), CAPACITY);
            aspect = new AspectStack(incoming.getAspect(), accepted);

            // Leftover
            int leftover = incoming.getAmount() - accepted;
            return leftover > 0 ? new AspectStack(incoming.getAspect(), leftover) : AspectStack.empty();
        }

        // If different type → reject fully
        if (!aspect.isSameType(incoming)) {
            return incoming;
        }

        // Add until full
        int space = CAPACITY - aspect.getAmount();
        if (space <= 0) return incoming; // full

        int accepted = Math.min(incoming.getAmount(), space);
        aspect.addAmount(accepted);

        int leftover = incoming.getAmount() - accepted;
        return leftover > 0 ? new AspectStack(incoming.getAspect(), leftover) : AspectStack.empty();
    }

    public AspectStack removeAspect(AspectStack aspectStack) {
        AspectStack empty = new AspectStack(aspectStack.getAspect(), 0);
        if (aspectStack.getAmount() <= 0) return empty;
        if (aspectStack.isEmpty() || !aspect.equals(aspectStack)) return empty;

        if (aspectStack.getAmount() >= aspect.getAmount()) {
            aspect = AspectStack.empty();
            return new AspectStack(aspectStack.getAspect(), aspectStack.getAmount() - aspect.getAmount());
        } else {
            aspect = new AspectStack(aspect.getAspect(), aspect.getAmount() - aspectStack.getAmount());
            return aspectStack;
        }
    }

    /**
     * SAVING / LOADING
     */
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains(ASPECT_TAG)) {
            aspect = AspectStack.parseOptional(registries, tag.getCompound(ASPECT_TAG));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (!aspect.isEmpty()) {
            tag.put(ASPECT_TAG, aspect.save(registries));
        }
    }

    /**
     * IAspectHandler Implementation
     */
    @Override
    public AspectStack getAspect() {
        return aspect;
    }

    @Override
    public void insertAspect(AspectStack stack) {
        addAspect(stack);
    }

    @Override
    public AspectStack extractAspect(int amount) {
        return removeAspect(new AspectStack(aspect.getAspect(), amount));
    }

    @Override
    public boolean canInsert() {
        return true;
    }

    @Override
    public boolean canExtract() {
        return true;
    }
}
