package net.sen.minearcana.compat.waila;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.blocks.entities.AltarBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import static net.sen.minearcana.common.blocks.entities.AltarBlockEntity.*;

public enum AltarComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(MineArcana.MODID, "altar");

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor accessor, IPluginConfig iPluginConfig) {
        CompoundTag serverData = accessor.getServerData();

        if (serverData.contains(COMPLETE_TAG)) {
            boolean complete = serverData.getBoolean(COMPLETE_TAG);
            iTooltip.add(Component.literal(complete ? "Altar Structure: Complete" : "Altar Structure: Incomplete"));
        }

        if (serverData.contains(TYPE_TAG)) {
            String type = serverData.getString(TYPE_TAG);
            iTooltip.add(Component.literal("Ritual Type: " + type));
        }

        if (serverData.contains(TIER_TAG)) {
            int tier = serverData.getInt(TIER_TAG);
            iTooltip.add(Component.literal("Tier: " + tier));
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof AltarBlockEntity blockEntity) {
            tag.putBoolean(COMPLETE_TAG, blockEntity.isComplete());
            tag.putString(TYPE_TAG, blockEntity.getDetectedType().toString());
            tag.putInt(TIER_TAG, blockEntity.getHighestTier());
        }
    }

    @Override
    public ResourceLocation getUid() { return UID; }
}
