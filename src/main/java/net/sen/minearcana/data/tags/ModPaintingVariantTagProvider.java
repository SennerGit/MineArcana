package net.sen.minearcana.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PaintingVariantTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.sen.minearcana.common.utils.ModUtils;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModPaintingVariantTagProvider extends PaintingVariantTagsProvider {
    public ModPaintingVariantTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> future, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, future, ModUtils.getModId(), existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.addMinecraftTags(pProvider);
        this.addNeoforgeTags(pProvider);
        this.addModTags(pProvider);
    }
    protected void addMinecraftTags(HolderLookup.Provider pProvider) {

    }
    protected void addNeoforgeTags(HolderLookup.Provider pProvider) {

    }
    protected void addModTags(HolderLookup.Provider pProvider) {

    }
}
