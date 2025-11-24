package net.sen.minearcana.compat.waila;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.blocks.entities.ArcanaCauldronBlockEntity;
import net.sen.minearcana.common.utils.aspect.AspectStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.List;

import static net.sen.minearcana.common.blocks.entities.ArcanaCauldronBlockEntity.*;

public enum ArcanaCauldronComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation UID =
            ResourceLocation.fromNamespaceAndPath(MineArcana.MODID, "arcana_cauldron");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag serverData = accessor.getServerData();

        // --- Fluid info ---
        if (serverData.contains(FLUID_TAG)) {
            FluidStack fluid = FluidStack.parseOptional(accessor.getLevel().registryAccess(), serverData.getCompound(FLUID_TAG));
            tooltip.add(Component.literal("Fluid: " + fluid.getHoverName().getString() + " (" + fluid.getAmount() + "mb)"));
        } else {
            tooltip.add(Component.literal("Fluid: None"));
        }

        // --- Heat info ---
        int temp = serverData.getInt(TEMPERATURE_TAG);
        int baseTemp = serverData.getInt(TEMPERATURE_BASE_TAG);
        int maxTemp = serverData.getInt(TEMPERATURE_MAX_TAG);

        tooltip.add(Component.literal("Temperature: " + temp + "°C"));
        tooltip.add(Component.literal("Base Temperature: " + baseTemp + "°C"));
        tooltip.add(Component.literal("Max Temperature: " + maxTemp + "°C"));

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
        if (accessor.getBlockEntity() instanceof ArcanaCauldronBlockEntity blockEntity) {

            // Temperature values
            tag.putInt(TEMPERATURE_TAG, blockEntity.getTemperature());
            tag.putInt(TEMPERATURE_BASE_TAG, blockEntity.getBaseTemperature());
            tag.putInt(TEMPERATURE_MAX_TAG, blockEntity.getMaxTemperature());

            // Fluid info
            FluidStack fluidStack = blockEntity.getFluid();
            if (!fluidStack.isEmpty()) {
                tag.put(FLUID_TAG, fluidStack.save(accessor.getLevel().registryAccess()));
            }

            // Aspects
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
