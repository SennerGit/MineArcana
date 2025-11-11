package net.sen.minearcana.data.aspects;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.registries.MineArcanaAspects;
import net.sen.minearcana.common.registries.MineArcanaElements;
import net.sen.minearcana.common.registries.MineArcanaTags;
import net.sen.minearcana.common.utils.ModUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * Auto-assigning MagicAspectDataGenerator.
 *
 * Rules:
 *  - Base items (raw resources, seeds, dirt) => value 1
 *  - Processed/refined (ingots, planks, cooked) => value 2
 *  - Tools/armor/weapons => value 2-6 depending on material
 *  - Rare/magic/boss items => value 6-12
 *  - Heuristics rely on registry path substrings (diamond, netherite, gold, raw, ore, block, sword, etc.)
 *
 * You can still override or add single items manually in the addXxx() sections.
 */
public class MagicAspectDataGenerator extends MagicAspectDataProvider {

    public MagicAspectDataGenerator(PackOutput pOutput,
                                    CompletableFuture<HolderLookup.Provider> pLookupProvider,
                                    CompletableFuture<TagsProvider.TagLookup<Block>> pBlockTags,
                                    @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, ModUtils.getModId(), existingFileHelper);
    }

    @Override
    protected void addAspects(HolderLookup.Provider provider) {
        addChaos(provider);
        addOrder(provider);
        addCreation(provider);
        addSpirit(provider);
        addCosmic(provider);
    }

    // -------------------------
    // Element groups
    // -------------------------
    protected void addChaos(HolderLookup.Provider provider) {
        AspectBuilder infernal = this.addAspect(MineArcanaAspects.INFERNAL, MineArcanaElements.CHAOS);
        AspectBuilder taint = this.addAspect(MineArcanaAspects.TAINT, MineArcanaElements.CHAOS);
        AspectBuilder storm = this.addAspect(MineArcanaAspects.STORM, MineArcanaElements.CHAOS);
        AspectBuilder fury = this.addAspect(MineArcanaAspects.FURY, MineArcanaElements.CHAOS);
        AspectBuilder entropy = this.addAspect(MineArcanaAspects.ENTROPY, MineArcanaElements.CHAOS, MineArcanaElements.ORDER);
        AspectBuilder demonic = this.addAspect(MineArcanaAspects.DEMONIC, MineArcanaElements.CHAOS, MineArcanaElements.SPIRIT);

        // direct manual picks (highly specific)
        infernal.addItem(Items.BLAZE_ROD, 5).addItem(Items.MAGMA_BLOCK, 3).addItem(Items.LAVA_BUCKET, 8).addItem(Items.NETHER_STAR, 10);
        taint.addItem(Items.ROTTEN_FLESH, 2).addItem(Items.SPIDER_EYE, 2).addItem(Items.FERMENTED_SPIDER_EYE, 6);
        storm.addItem(Items.TRIDENT, 3).addItem(Items.LIGHTNING_ROD, 2);
        entropy.addItem(Items.TNT, 4).addItem(Items.FLINT_AND_STEEL, 3).addItem(Items.GUNPOWDER, 2);
        demonic.addItem(Items.WITHER_SKELETON_SKULL, 8).addItem(Items.GHAST_TEAR, 6);

        //tag assignements
        fury.addMatchingTag(Tags.Items.MELEE_WEAPON_TOOLS, 2);
        fury.addMatchingTag(Tags.Items.RANGED_WEAPON_TOOLS, 2);

        // broad assignment by name heuristics
        addMatching(i -> containsAny(i, "fire", "magma", "blaze", "lava"), infernal, 2);
        addMatching(i -> containsAny(i, "poison", "rotten", "spider", "fermented"), taint, 1);
        addMatching(i -> containsAny(i, "tnt", "charge", "explosive", "gunpowder"), entropy, 2);
    }

    protected void addOrder(HolderLookup.Provider provider) {
        AspectBuilder judgment = this.addAspect(MineArcanaAspects.JUDGMENT, MineArcanaElements.ORDER);
        AspectBuilder frost = this.addAspect(MineArcanaAspects.FROST, MineArcanaElements.ORDER);
        AspectBuilder water = this.addAspect(MineArcanaAspects.WATER, MineArcanaElements.ORDER);
        AspectBuilder divine = this.addAspect(MineArcanaAspects.DIVINE, MineArcanaElements.ORDER, MineArcanaElements.SPIRIT);

        // manual/high items
        judgment.addItem(Items.DIAMOND_SWORD, valueByMaterial("diamond", 6))
                .addItem(Items.NETHERITE_SWORD, valueByMaterial("netherite", 9))
                .addItem(Items.IRON_SWORD, valueByMaterial("iron", 3));
        frost.addItem(Items.ICE, 1).addItem(Items.BLUE_ICE, 3).addItem(Items.POWDER_SNOW_BUCKET, 3);
        water.addItem(Items.WATER_BUCKET, 2).addItem(Items.HEART_OF_THE_SEA, 6).addItem(Items.CONDUIT, 8);
        divine.addItem(Items.GOLDEN_APPLE, 5).addItem(Items.TOTEM_OF_UNDYING, 10);

        //tag assignements

        // common heuristics
        addMatching(i -> isSword(i) || isAxe(i) || isShield(i), judgment, 2);
        addMatching(i -> containsAny(i, "ice", "snow", "powder_snow", "frost"), frost, 1);
        addMatching(i -> containsAny(i, "fish", "prismarine", "coral", "seagrass", "conduit"), water, 2);
        addMatching(i -> containsAny(i, "golden_apple", "totem", "enchanted_golden_apple", "gold_block"), divine, 4);
    }

    protected void addCreation(HolderLookup.Provider provider) {
        AspectBuilder earth = this.addAspect(MineArcanaAspects.EARTH, MineArcanaElements.CREATION);
        AspectBuilder mechanical = this.addAspect(MineArcanaAspects.MECHANICAL, MineArcanaElements.CREATION);
        AspectBuilder metallic = this.addAspect(MineArcanaAspects.METALLIC, MineArcanaElements.CREATION);
        AspectBuilder crystal = this.addAspect(MineArcanaAspects.CRYSTAL, MineArcanaElements.CREATION);
        AspectBuilder nature = this.addAspect(MineArcanaAspects.NATURE, MineArcanaElements.CREATION, MineArcanaElements.SPIRIT);

        // manual seeds
        earth.addItem(Items.DIRT, 1).addItem(Items.CLAY_BALL, 1).addItem(Items.COAL, 1).addItem(Items.IRON_ORE, 2);
        mechanical.addItem(Items.REDSTONE, 2).addItem(Items.PISTON, 3).addItem(Items.HOPPER, 4);
        metallic.addItem(Items.IRON_INGOT, 2).addItem(Items.GOLD_INGOT, 2).addItem(Items.NETHERITE_INGOT, 8);
        crystal.addItem(Items.DIAMOND, 5).addItem(Items.QUARTZ, 3).addItem(Items.EMERALD, 4);
        nature.addItem(Items.WHEAT_SEEDS, 1).addItem(Items.BONE_MEAL, 2).addItem(Items.SUGAR_CANE, 1);

        //tag assignements

        // heuristics
        addMatching(i -> containsAny(i, "ore", "raw_", "stone", "cobbled", "dirt", "gravel", "deepslate"), earth, 2);
        addMatching(i -> containsAny(i, "piston", "redstone", "comparator", "repeater", "hopper", "dispenser"), mechanical, 2);
        addMatching(i -> containsAny(i, "ingot", "nugget", "block_of_", "ore", "raw_"), metallic, 2);
        addMatching(i -> containsAny(i, "diamond", "emerald", "quartz", "prismarine", "amethyst"), crystal, 3);
        addMatching(i -> isFood(i) || containsAny(i, "seed", "sapling", "leaf", "mushroom", "flower"), nature, 1);
    }

    protected void addSpirit(HolderLookup.Provider provider) {
        AspectBuilder life = this.addAspect(MineArcanaAspects.LIFE, MineArcanaElements.SPIRIT);
        AspectBuilder death = this.addAspect(MineArcanaAspects.DEATH, MineArcanaElements.SPIRIT);
        AspectBuilder limbo = this.addAspect(MineArcanaAspects.LIMBO, MineArcanaElements.SPIRIT);

        life.addItem(Items.APPLE, 1).addItem(Items.GOLDEN_CARROT, 3).addItem(Items.HONEY_BOTTLE, 2);
        death.addItem(Items.BONE, 1).addItem(Items.SOUL_SAND, 2).addItem(Items.WITHER_ROSE, 4);
        limbo.addItem(Items.CHORUS_FRUIT, 3).addItem(Items.SHULKER_SHELL, 6);

        addMatching(i -> containsAny(i, "bone", "skull", "rotten", "soul", "wither"), death, 1);
        addMatching(i -> containsAny(i, "apple", "carrot", "honey", "sapling", "beetroot"), life, 1);
        addMatching(i -> containsAny(i, "chorus", "ender", "shulker", "dragon", "spawn_egg"), limbo, 3);
    }

    protected void addCosmic(HolderLookup.Provider provider) {
        AspectBuilder astral = this.addAspect(MineArcanaAspects.ASTRAL, MineArcanaElements.COSMIC);
        AspectBuilder aVoid = this.addAspect(MineArcanaAspects.VOID, MineArcanaElements.COSMIC);
        AspectBuilder gravity = this.addAspect(MineArcanaAspects.GRAVITY, MineArcanaElements.COSMIC);

        astral.addItem(Items.ENDER_PEARL, 3).addItem(Items.DRAGON_BREATH, 8).addItem(Items.NETHERITE_SCRAP, 4);
        aVoid.addItem(Items.OBSIDIAN, 2).addItem(Items.CRYING_OBSIDIAN, 3).addItem(Items.WITHER_SKELETON_SKULL, 7);
        gravity.addItem(Items.ELYTRA, 10).addItem(Items.FEATHER, 1).addItem(Items.SLIME_BALL, 2);

        addMatching(i -> containsAny(i, "ender", "dragon", "shulker", "elytra"), astral, 2);
        addMatching(i -> containsAny(i, "obsidian", "blackstone", "void", "bedrock", "wither"), aVoid, 2);
        addMatching(i -> containsAny(i, "feather", "elytra", "gravity", "weight"), gravity, 1);
    }

    // -------------------------
    // Helpers
    // -------------------------
    private static boolean containsAny(Item item, String... tokens) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        if (id == null) return false;
        String path = id.getPath().toLowerCase(Locale.ROOT);
        for (String t : tokens) if (path.contains(t)) return true;
        return false;
    }

    private static boolean isSword(Item item) {
        return item instanceof SwordItem;
    }

    private static boolean isAxe(Item item) {
        return item instanceof AxeItem;
    }

    private static boolean isShield(Item item) {
        return item instanceof ShieldItem;
    }

    private static boolean isFood(Item item) {
        FoodProperties foodProperties = item.getFoodProperties(new ItemStack(item), null);
        if (foodProperties == null) return false;
        // ignore drinks or potions if needed
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        return !name.contains("potion") && !name.contains("stew");
    }

    private int valueByMaterial(String materialToken, int highValue) {
        // if you want material-specific scaling later, handle here.
        return highValue;
    }

    /**
     * Adds all items from registry matching the predicate to the aspect builder using given value.
     */
    private void addMatching(Predicate<Item> pred, AspectBuilder builder, int value) {
        BuiltInRegistries.ITEM.stream().filter(pred).forEach(item -> {
            // skip if already present to avoid duplicates
            try {
                builder.addItem(item, value);
            } catch (IllegalArgumentException ignored) {
                // ignore unregistered
            }
        });
    }

    private void addTag(TagKey<Item> tag, AspectBuilder builder, int value, HolderLookup.Provider provider) {
        Optional<HolderSet.Named<Item>> tagEntries = provider.lookupOrThrow(BuiltInRegistries.ITEM.key()).get(tag);

        if (tagEntries.isEmpty()) {
            MineArcana.LOGGER.warn("Tag {} has no entries when adding to aspect builder.", tag.location());
            return;
        }

        tagEntries.get().forEach(holder -> {
            builder.addMatchingTag(tag, value);
        });
    }

    // Overload for BuiltInRegistries.ITEM convenience
    private void addMatching(BuiltInRegistries registry, Predicate<Item> pred, AspectBuilder builder, int value) {
        // unused, provided to match earlier signature; not needed
    }
}
