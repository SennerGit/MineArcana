package net.sen.minearcana.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.sen.minearcana.common.registries.MineArcanaTags;
import net.sen.minearcana.common.utils.ModUtils;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ModUtils.getModId(), existingFileHelper);
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

    @Override
    public String getName() {
        return "Block Tags";
    }
}
