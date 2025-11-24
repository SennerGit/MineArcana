package net.sen.minearcana.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sen.minearcana.common.registries.MineArcanaBlockEntites;

public class BloodMarkerBlockEntity extends BlockEntity {
    private String entityID = "";

    public static final String ID_TAG = "EntityId";

    public BloodMarkerBlockEntity(BlockPos pos, BlockState blockState) {
        super(MineArcanaBlockEntites.BLOOD_MARKER.get(), pos, blockState);
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
        setChanged();
        if (level != null && !level.isClientSide)
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    public String getEntityID() {
        return entityID;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString(ID_TAG, this.entityID == null ? "" : this.entityID);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.entityID = tag.getString(ID_TAG);
    }
}