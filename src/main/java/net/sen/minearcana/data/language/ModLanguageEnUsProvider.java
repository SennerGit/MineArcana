package net.sen.minearcana.data.language;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.sen.minearcana.common.registries.MineArcanaCreativeModeTabs;

import java.util.Collection;

public class ModLanguageEnUsProvider extends LanguageProviderHelper {
    public ModLanguageEnUsProvider(PackOutput output, String locale) {
        super(output, locale);
    }

    @Override
    void spawnEggs() {
    }

    @Override
    void blocks() {
    }

    @Override
    void items() {
    }

    @Override
    void paintings() {
    }

    @Override
    void effects() {
    }

    @Override
    void potions() {
    }

    @Override
    void sounds() {
    }

    @Override
    void custom() {
    }

    @Override
    void config() {
    }

    @Override
    void creativeTab() {
        this.addCreativeTab(MineArcanaCreativeModeTabs.MINEARCANA_TAB, "MineArcana");
    }

    @Override
    void baseAdvancements() {
    }
}
