package net.sen.minearcana.data.loottable;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(pOutput, ModLootTables.allBuiltin(), List.of(
                new SubProviderEntry(ModBlockLootTables::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(ModEntityLootTables::new, LootContextParamSets.ENTITY),
                new SubProviderEntry(ModChestLootTables::new, LootContextParamSets.CHEST),
                new SubProviderEntry(ModArchaeologyLootTables::new, LootContextParamSets.ARCHAEOLOGY),
                new SubProviderEntry(ModShearingLootTables::new, LootContextParamSets.SHEARING),
                new SubProviderEntry(ModSpecialLootTables::new, LootContextParamSets.EMPTY)
        ), provider);
    }
}
