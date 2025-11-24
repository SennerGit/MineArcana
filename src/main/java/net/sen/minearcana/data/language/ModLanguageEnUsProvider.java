package net.sen.minearcana.data.language;

import net.minecraft.data.PackOutput;
import net.sen.minearcana.common.registries.*;

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
        this.addBlock(MineArcanaBlocks.ASPECT_MIXER, "Aspect Mixer");
        this.addBlock(MineArcanaBlocks.ASPECT_TANK, "Aspect Tank");
        this.addBlock(MineArcanaBlocks.ASPECT_PIPE, "Aspect Pipe");
        this.addBlock(MineArcanaBlocks.ARCANA_BLOCK, "Arcana Block");
        this.addBlock(MineArcanaBlocks.BUDDING_ARCANA, "Budding Arcana");
        this.addBlock(MineArcanaBlocks.ARCANA_CLUSTER, "Arcana Cluster");
        this.addBlock(MineArcanaBlocks.LARGE_ARCANA_BUD, "Large Arcana Bud");
        this.addBlock(MineArcanaBlocks.MEDIUM_ARCANA_BUD, "Medium Arcana Bud");
        this.addBlock(MineArcanaBlocks.SMALL_ARCANA_BUD, "Small Arcana Bud");
        this.addBlock(MineArcanaBlocks.ARCANA_CRYSTAL, "Arcana Crystal");
        this.addBlock(MineArcanaBlocks.ALTAR_BLOCK, "Altar");
        this.addBlock(MineArcanaBlocks.ARCANA_LIGHT_EMITTER, "Arcane Light Emitter");
        this.addBlock(MineArcanaBlocks.ARCANA_LIGHT_RECEIVER, "Arcane Light Receiver");
        this.addBlock(MineArcanaBlocks.ARCANA_MIRROR, "Mirror");
    }

    @Override
    void items() {
        this.addItem(MineArcanaItems.ARCANE_DUST, "Arcane Dust");
        this.addItem(MineArcanaItems.ARCANE_SHARD, "Arcane Shard");
        this.addItem(MineArcanaItems.RITUAL_KNIFE, "Ritual Knife");
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
