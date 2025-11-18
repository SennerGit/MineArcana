package net.sen.minearcana.compat.waila;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.blocks.entities.AspectExtractorBlockEntity;
import net.sen.minearcana.common.utils.aspect.AspectStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.List;

import static net.sen.minearcana.common.blocks.entities.AspectExtractorBlockEntity.*;

public enum AspectExtractorComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation UID =
            ResourceLocation.fromNamespaceAndPath(MineArcana.MODID, "aspect_extractor");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag serverData = accessor.getServerData();

        // --- Item ---
        if (serverData.contains(ITEM_TAG)) {
            ItemStack itemStack = ItemStack.parseOptional(accessor.getLevel().registryAccess(), serverData.getCompound(ITEM_TAG));
            tooltip.add(Component.literal("Item: " + itemStack.getHoverName().getString()));
        } else {
            tooltip.add(Component.literal("Item: None"));
        }

        // --- Aspects ---
        if (serverData.contains(ASPECT_TAG)) {
            ListTag aspectTags = serverData.getList(ASPECT_TAG, 10); // 10 = CompoundTag
            if (aspectTags.isEmpty()) {
                tooltip.add(Component.literal("Aspects: None"));
            } else {
                tooltip.add(Component.literal("Aspects:"));
                for (int i = 0; i < aspectTags.size(); i++) {
                    CompoundTag aspectTag = aspectTags.getCompound(i);
                    String name = aspectTag.getString("Aspect");
                    int amount = aspectTag.getInt("Amount");
                    tooltip.add(Component.literal(" - " + name + ": " + amount));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof AspectExtractorBlockEntity blockEntity) {
            // --- Item ---
            ItemStack itemStack = blockEntity.inventoryHandler.getStackInSlot(0);
            if (!itemStack.isEmpty()) {
                tag.put(ITEM_TAG, itemStack.save(accessor.getLevel().registryAccess()));
            }

            // --- Aspects ---
            List<AspectStack> aspects = blockEntity.getAspects();
            if (!aspects.isEmpty()) {
                ListTag aspectTags = new ListTag();
                for (AspectStack stack : aspects) {
                    CompoundTag aspectTag = new CompoundTag();
                    aspectTag.putString("Aspect", stack.getAspect().getName()); // or ID
                    aspectTag.putInt("Amount", stack.getAmount());
                    aspectTags.add(aspectTag);
                }
                tag.put(ASPECT_TAG, aspectTags);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
