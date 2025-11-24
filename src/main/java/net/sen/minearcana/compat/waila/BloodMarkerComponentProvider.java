package net.sen.minearcana.compat.waila;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.blocks.entities.AspectTankBlockEntity;
import net.sen.minearcana.common.blocks.entities.BloodMarkerBlockEntity;
import net.sen.minearcana.common.utils.aspect.AspectStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import static net.sen.minearcana.common.blocks.entities.BloodMarkerBlockEntity.*;

public enum BloodMarkerComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation UID =
            ResourceLocation.fromNamespaceAndPath(MineArcana.MODID, "blood_marker");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag serverData = accessor.getServerData();

        if (serverData.contains(ID_TAG)) {
            String id = serverData.getString(ID_TAG);
            tooltip.add(Component.literal("Entity: " + id));
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof BloodMarkerBlockEntity blockEntity) {
            String entityID = blockEntity.getEntityID();
            if (!entityID.isEmpty()) {
                tag.putString(ID_TAG, entityID);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
