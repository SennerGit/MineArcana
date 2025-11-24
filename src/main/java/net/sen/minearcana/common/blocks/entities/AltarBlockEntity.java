package net.sen.minearcana.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sen.minearcana.common.registries.MineArcanaBlockEntites;
import net.sen.minearcana.common.utils.altar.*;

import java.util.List;

public class AltarBlockEntity extends BlockEntity {
    private boolean complete = false;
    private AltarType detectedType = AltarType.INVALID;
    private int highestTier = 0;

    public static final String COMPLETE_TAG = "Complete";
    public static final String TYPE_TAG = "DetectedType";
    public static final String TIER_TAG = "HighestTier";

    public AltarBlockEntity(BlockPos pos, BlockState blockState) {
        super(MineArcanaBlockEntites.ALTAR_BLOCK.get(), pos, blockState);
    }

    public static void serverTicker(Level level, BlockPos pos, BlockState state, AltarBlockEntity blockEntity) {
        if (level == null || level.isClientSide) return;
        if (level.getGameTime() % 20 == 0) {
            blockEntity.detectRitual(level);
        }
    }

    private void detectRitual(Level level) {
        detectedType = AltarType.INVALID;
        complete = false;
        int currentTier = 0;

        for (AltarType type : AltarType.values()) {
            if (type == AltarType.INVALID) continue;
            AltarStructure structure = AltarStructureRegistry.get(type);
            if (structure == null) continue;

            int maxTier = structure.getMaxTier();

            for (int tier = 1; tier <= maxTier; tier++) {
                if (structure.isTierComplete(level, worldPosition, tier)) {
                    currentTier = tier;
                } else {
                    break;
                }
            }

            if (currentTier > 0) {
                detectedType = type;
                complete = true;
                highestTier = currentTier;
                return;
            }
        }

        highestTier = 0;
    }

    public AltarType getDetectedType() { return detectedType; }
    public int getHighestTier() { return highestTier; }
    public boolean isComplete() { return complete; }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains(COMPLETE_TAG)) complete = tag.getBoolean(COMPLETE_TAG);
        if (tag.contains(TYPE_TAG)) detectedType = AltarType.valueOf(tag.getString(TYPE_TAG));
        if (tag.contains(TIER_TAG)) highestTier = tag.getInt(TIER_TAG);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putBoolean(COMPLETE_TAG, complete);
        tag.putString(TYPE_TAG, detectedType.name());
        tag.putInt(TIER_TAG, highestTier);
    }
}
