package net.sen.minearcana.data.language;

import net.minecraft.data.PackOutput;
import net.sen.minearcana.common.registries.MineArcanaAspects;
import net.sen.minearcana.common.registries.MineArcanaBlocks;
import net.sen.minearcana.common.registries.MineArcanaCreativeModeTabs;
import net.sen.minearcana.common.registries.MineArcanaElements;

public class ModLanguageEnUsProvider extends LanguageProviderHelper {
    public ModLanguageEnUsProvider(PackOutput output, String locale) {
        super(output, locale);
    }

    @Override
    void spawnEggs() {
    }

    @Override
    void blocks() {
        this.addBlock(MineArcanaBlocks.ARCANA_CAULDRON, "Cauldron");
        this.addBlock(MineArcanaBlocks.ASPECT_EXTRACTOR, "Aspect Extractor");
        this.addBlock(MineArcanaBlocks.ASPECT_CONDENSER, "Aspect Condenser");
        this.addBlock(MineArcanaBlocks.ASPECT_MIXER, "Aspect Mixer");
        this.addBlock(MineArcanaBlocks.ASPECT_TANK, "Aspect Tank");
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

    @Override
    void elements() {
        this.addElement(MineArcanaElements.CHAOS, "Chaos");
        this.addElement(MineArcanaElements.ORDER, "Order");
        this.addElement(MineArcanaElements.CREATION, "Creation");
        this.addElement(MineArcanaElements.SPIRIT, "Spirit");
        this.addElement(MineArcanaElements.COSMIC, "Cosmic");
    }

    @Override
    void aspects() {
        this.addAspect(MineArcanaAspects.INFERNAL, "Infernal");
        this.addAspect(MineArcanaAspects.TAINT, "Tainted");
        this.addAspect(MineArcanaAspects.STORM, "Storm");
        this.addAspect(MineArcanaAspects.FURY, "Fury");
        this.addAspect(MineArcanaAspects.ENTROPY, "Entropy");
        this.addAspect(MineArcanaAspects.DEMONIC, "Demonic");
        this.addAspect(MineArcanaAspects.JUDGMENT, "Judgment");
        this.addAspect(MineArcanaAspects.FROST, "Frost");
        this.addAspect(MineArcanaAspects.WATER, "Water");
        this.addAspect(MineArcanaAspects.DIVINE, "Divine");
        this.addAspect(MineArcanaAspects.EARTH, "Earth");
        this.addAspect(MineArcanaAspects.MECHANICAL, "Mechanical");
        this.addAspect(MineArcanaAspects.METALLIC, "Metallic");
        this.addAspect(MineArcanaAspects.CRYSTAL, "Crystal");
        this.addAspect(MineArcanaAspects.NATURE, "Nature");
        this.addAspect(MineArcanaAspects.LIFE, "Life");
        this.addAspect(MineArcanaAspects.DEATH, "Death");
        this.addAspect(MineArcanaAspects.LIMBO, "Limbo");
        this.addAspect(MineArcanaAspects.ASTRAL, "Astral");
        this.addAspect(MineArcanaAspects.VOID, "Void");
        this.addAspect(MineArcanaAspects.GRAVITY, "Gravity");
    }
}
