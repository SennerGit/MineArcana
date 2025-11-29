package net.sen.minearcana.data.models;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static net.sen.minearcana.common.registries.MineArcanaBlocks.*;

public class ModBlockStateProvider extends ModBlockStateHelper {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.blockWithItem(ARCANA_CAULDRON);
        this.blockWithItem(ASPECT_EXTRACTOR);
        this.blockWithItem(ASPECT_MIXER);
        this.blockWithItem(ASPECT_TANK);
        this.blockWithItem(ASPECT_PIPE);

        this.blockWithItem(ARCANA_LIGHT_EMITTER);
        this.blockWithItem(ARCANA_LIGHT_RECEIVER);
        this.blockWithItem(ARCANA_MIRROR);

        this.blockWithItem(WAND_CRAFTING_STATION);

        this.blockWithItem(ALTAR_BLOCK);

        this.flatBlock(BLOOD_MARKER);

        this.blockWithItem(ARCANA_CRYSTAL);
        this.blockWithItem(ARCANA_BLOCK);
        this.blockWithItem(BUDDING_ARCANA);
        this.blockCrystalCluster(ARCANA_CLUSTER);
        this.blockCrystalCluster(LARGE_ARCANA_BUD);
        this.blockCrystalCluster(MEDIUM_ARCANA_BUD);
        this.blockCrystalCluster(SMALL_ARCANA_BUD);

        this.flatBlock(ARCANA_MARKER);
    }
}
