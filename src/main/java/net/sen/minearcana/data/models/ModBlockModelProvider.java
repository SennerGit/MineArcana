package net.sen.minearcana.data.models;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.sen.minearcana.common.utils.ModUtils;

public class ModBlockModelProvider extends BlockModelProvider {
    public ModBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ModUtils.getModId(), existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }

    private ResourceLocation modResourceLocation(DeferredBlock<Block> block) {
        return modResourceLocation(block.getId().getPath());
    }

    private ResourceLocation modResourceLocation(String block) {
        ResourceLocation resourceLocation;
        resourceLocation = ModUtils.getModPath("block/" + block);
        return resourceLocation;
    }
}
