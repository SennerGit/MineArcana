package net.sen.minearcana.data.aspects;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.registries.MineArcanaTags;
import net.sen.minearcana.common.utils.ModUtils;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class MagicAspectDataGenerator extends MagicAspectDataProvider{
    public MagicAspectDataGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, ModUtils.getModId(), existingFileHelper);
    }

    @Override
    protected void addAspects(HolderLookup.Provider provider) {
        this.addAspect("divine", MineArcanaTags.MagicElements.ORDER)
                .addItem(Items.GOLDEN_APPLE, 5)
                .addItem(Items.TOTEM_OF_UNDYING, 10)
                .addItem(Items.GLOWSTONE_DUST, 2)
                .addItem(Items.ENCHANTED_GOLDEN_APPLE, 15)
                .addItem(Items.GOLD_BLOCK, 3);

        this.addAspect("infernal", MineArcanaTags.MagicElements.CHAOS)
                .addItem(Items.BLAZE_ROD, 5)
                .addItem(Items.MAGMA_BLOCK, 3)
                .addItem(Items.NETHER_BRICKS, 2)
                .addItem(Items.LAVA_BUCKET, 8)
                .addItem(Items.NETHER_STAR, 10);

        this.addAspect("astral", MineArcanaTags.MagicElements.COSMIC)
                .addItem(Items.ENDER_PEARL, 3)
                .addItem(Items.ENDER_EYE, 5)
                .addItem(Items.SHULKER_SHELL, 2)
                .addItem(Items.DRAGON_BREATH, 8)
                .addItem(Items.NETHERITE_SCRAP, 4);

        this.addAspect("life", MineArcanaTags.MagicElements.SPIRIT)
                .addItem(Items.APPLE, 1)
                .addItem(Items.GOLDEN_CARROT, 3)
                .addItem(Items.HONEY_BOTTLE, 2)
                .addItem(Items.MOSS_BLOCK, 1)
                .addItem(Items.OAK_SAPLING, 1)
                .addItem(Items.TOTEM_OF_UNDYING, 10);

        this.addAspect("death", MineArcanaTags.MagicElements.SPIRIT)
                .addItem(Items.BONE, 1)
                .addItem(Items.ROTTEN_FLESH, 1)
                .addItem(Items.WITHER_SKELETON_SKULL, 8)
                .addItem(Items.SOUL_SAND, 2)
                .addItem(Items.WITHER_ROSE, 5);

        this.addAspect("earth", MineArcanaTags.MagicElements.CREATION)
                .addItem(Items.IRON_INGOT, 2)
                .addItem(Items.EMERALD, 4)
                .addItem(Items.CLAY_BALL, 1)
                .addItem(Items.DIRT, 1);

        this.addAspect("mechanical", MineArcanaTags.MagicElements.ORDER, MineArcanaTags.MagicElements.CREATION)
                .addItem(Items.REDSTONE, 2)
                .addItem(Items.PISTON, 3)
                .addItem(Items.REPEATER, 3)
                .addItem(Items.HOPPER, 4)
                .addItem(Items.IRON_BLOCK, 5);

        this.addAspect("tainted", MineArcanaTags.MagicElements.CHAOS)
                .addItem(Items.SLIME_BALL, 2)
                .addItem(Items.SPIDER_EYE, 3)
                .addItem(Items.FERMENTED_SPIDER_EYE, 4)
                .addItem(Items.WARPED_FUNGUS, 3)
                .addItem(Items.POISONOUS_POTATO, 2);

        this.addAspect("frost", MineArcanaTags.MagicElements.ORDER)
                .addItem(Items.ICE, 2)
                .addItem(Items.BLUE_ICE, 4)
                .addItem(Items.SNOWBALL, 1)
                .addItem(Items.PACKED_ICE, 3)
                .addItem(Items.POWDER_SNOW_BUCKET, 5);

        this.addAspect("storm", MineArcanaTags.MagicElements.CHAOS)
                .addItem(Items.TRIDENT, 8)
                .addItem(Items.LIGHTNING_ROD, 3)
                .addItem(Items.FEATHER, 1)
                .addItem(Items.ELYTRA, 5)
                .addItem(Items.GUNPOWDER, 2);
    }
}
