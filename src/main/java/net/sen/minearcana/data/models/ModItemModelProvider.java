package net.sen.minearcana.data.models;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static net.sen.minearcana.common.registries.MineArcanaItems.*;
import static net.sen.minearcana.common.registries.MineArcanaBlocks.*;

public class ModItemModelProvider extends ModItemModelHelper {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, existingFileHelper);
    }

    @Override
    protected void generateItems() {
        this.simpleItem(RITUAL_KNIFE);
        this.simpleItem(ARCANE_SHARD);
        this.simpleItem(ARCANE_DUST);
    }

    @Override
    protected void generateBlocks() {
    }
}