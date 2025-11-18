package net.sen.minearcana.compat.waila;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.blocks.entities.AspectTankBlockEntity;
import net.sen.minearcana.common.utils.aspect.AspectStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.List;

import static net.sen.minearcana.common.blocks.entities.AspectPipeBlockEntity.*;

public enum AspectTankComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation UID =
            ResourceLocation.fromNamespaceAndPath(MineArcana.MODID, "aspect_extractor");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag serverData = accessor.getServerData();

        if (serverData.contains(ASPECT_TAG)) {
            CompoundTag aspectTag = serverData.getCompound(ASPECT_TAG);
            String name = aspectTag.getString("Aspect");
            int amount = aspectTag.getInt("Amount");
            tooltip.add(Component.literal("Aspect Tank Data: " + name));
            tooltip.add(Component.literal("Aspect Tank Amount: " + amount));
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof AspectTankBlockEntity blockEntity) {
            AspectStack aspectStack = blockEntity.getAspect();
            if (!aspectStack.isEmpty()) {
                CompoundTag aspectTag = new CompoundTag();
                aspectTag.putString("Aspect", aspectStack.getAspect().getName()); // or ID
                aspectTag.putInt("Amount", aspectStack.getAmount());
                tag.put(ASPECT_TAG, aspectTag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
