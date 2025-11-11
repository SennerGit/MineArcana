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
        this.blockWithItem(CAULDRON);
        this.blockWithItem(ASPECT_EXTRACTOR);
        this.blockWithItem(ASPECT_CONDENSER);
        this.blockWithItem(ASPECT_MIXER);
        this.blockWithItem(ASPECT_TANK);
    }
}
