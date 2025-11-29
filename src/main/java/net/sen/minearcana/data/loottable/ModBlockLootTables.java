package net.sen.minearcana.data.loottable;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;

import static net.sen.minearcana.common.registries.MineArcanaBlocks.*;
import static net.sen.minearcana.common.registries.MineArcanaItems.*;

public class ModBlockLootTables extends BlockLootTableHelper {
    private static final LootItemCondition.Builder SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));

    public ModBlockLootTables(HolderLookup.Provider registries) {
        super(registries);
    }

    @Override
    protected void generate() {
        this.dropSelf(ARCANA_CAULDRON);
        this.dropSelf(ASPECT_EXTRACTOR);
        this.dropSelf(ASPECT_MIXER);
        this.dropSelf(ASPECT_TANK);
        this.dropSelf(ASPECT_PIPE);

        this.dropSelf(ARCANA_LIGHT_EMITTER);
        this.dropSelf(ARCANA_LIGHT_RECEIVER);
        this.dropSelf(ARCANA_MIRROR);

        this.dropSelf(WAND_CRAFTING_STATION);

        this.dropSelf(ALTAR_BLOCK);

        this.dropWhenSilkTouch(ARCANA_CRYSTAL.get());
        this.dropSelf(ARCANA_BLOCK);
        this.dropArcanaCluster(ARCANA_CLUSTER, ARCANE_SHARD, 2, 4);
        this.dropWhenSilkTouch(LARGE_ARCANA_BUD.get());
        this.dropWhenSilkTouch(MEDIUM_ARCANA_BUD.get());
        this.dropWhenSilkTouch(SMALL_ARCANA_BUD.get());
    }
}
