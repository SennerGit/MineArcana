package net.sen.minearcana.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.sen.minearcana.common.registries.MineArcanaTags;
import net.sen.minearcana.common.utils.ModUtils;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, ModUtils.getModId(), existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.addMinecraftTags(pProvider);
        this.addNeoforgeTags(pProvider);
        this.addModTags(pProvider);
        this.addMagicElements(pProvider);
        this.addMagicAspects(pProvider);
    }
    protected void addMinecraftTags(HolderLookup.Provider pProvider) {

    }
    protected void addNeoforgeTags(HolderLookup.Provider pProvider) {

    }
    protected void addModTags(HolderLookup.Provider pProvider) {
    }

    protected void addMagicElements(HolderLookup.Provider pProvider) {
        this.tag(MineArcanaTags.MagicElements.MAGIC_ELEMENTS).addTags(
                MineArcanaTags.MagicElements.CHAOS,
                MineArcanaTags.MagicElements.ORDER,
                MineArcanaTags.MagicElements.CREATION,
                MineArcanaTags.MagicElements.SPIRIT,
                MineArcanaTags.MagicElements.COSMIC
        );
    }

    protected void addMagicAspects(HolderLookup.Provider pProvider) {
        this.tag(MineArcanaTags.MagicAspects.MAGIC_ASPECTS).addTags(
                MineArcanaTags.MagicAspects.DIVINE,
                MineArcanaTags.MagicAspects.INFERNAL,
                MineArcanaTags.MagicAspects.ASTRAL,
                MineArcanaTags.MagicAspects.COSMIC,
                MineArcanaTags.MagicAspects.LIFE,
                MineArcanaTags.MagicAspects.DEATH,
                MineArcanaTags.MagicAspects.EARTH,
                MineArcanaTags.MagicAspects.MECHANICAL,
                MineArcanaTags.MagicAspects.TAINTED
        );
    }

    @Override
    public String getName() {
        return "Item Tags";
    }
}
