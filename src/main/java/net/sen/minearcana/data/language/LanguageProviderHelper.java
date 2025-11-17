package net.sen.minearcana.data.language;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.sen.minearcana.common.registries.MineArcanaRegistries;
import net.sen.minearcana.common.utils.ModUtils;
import net.sen.minearcana.common.utils.aspect.Aspect;
import net.sen.minearcana.common.utils.element.Element;

import java.util.Locale;
import java.util.function.Supplier;

public abstract class LanguageProviderHelper extends LanguageProvider {
    public LanguageProviderHelper(PackOutput output, String locale) {
        super(output, ModUtils.getModId(), locale);
    }

    @Override
    protected void addTranslations() {
        blocks();
        items();
        spawnEggs();
        paintings();
        effects();
        potions();
        sounds();
        custom();
        config();
        creativeTab();
        baseAdvancements();
        elements();
        aspects();
    }

    abstract void spawnEggs();
    abstract void blocks();
    abstract void items();
    abstract void paintings();
    abstract void effects();
    abstract void potions();
    abstract void sounds();
    abstract void custom();
    abstract void config();
    abstract void creativeTab();
    abstract void baseAdvancements();
    abstract void elements();
    abstract void aspects();

    protected void addTagFilterUI(String id, String name) {
        this.add("gui.tag_filter.lostworlds." + id, name);
    }

    protected void addPainting(DeferredHolder<PaintingVariant, PaintingVariant> painting, String title, String author) {
        add("painting." + ModUtils.getModId() + "." + painting.getId().getPath() + ".title", title);
        add("painting." + ModUtils.getModId() + "." + painting.getId().getPath() + ".author", author);
    }

    protected void addPotion(DeferredHolder<Potion, Potion> potion, String title) {
        add("items.minecraft.potion.effect." + potion.getId().getPath(), title + " Potion");
        add("items.minecraft.splash_potion.effect." + potion.getId().getPath(), title + "Splash Potion");
        add("items.minecraft.lingering_potion.effect." + potion.getId().getPath(), title + " Lingering Potion");
    }

    protected void addCreativeTab(DeferredHolder<CreativeModeTab, CreativeModeTab> tab, String name) {
        add("itemgroup." + ModUtils.getModId() + "." + tab.getId().getPath(), name);
    }

    protected void addProfession(DeferredHolder<VillagerProfession, VillagerProfession> profession, String name) {
        add("entity.minecraft.villager." + ModUtils.getModId() + "." + profession.getId().getPath(), name);
    }

    protected void addRecord(DeferredItem<Item> record, String name, String description) {
        addItem(record, name);
        add(record.get() + ".desc", description);
    }

    protected void addSound(DeferredHolder<SoundEvent, SoundEvent> sound, String name) {
        add("sounds." + ModUtils.getModId() + "." + sound.get().getLocation().getPath(), name);
//        add("sounds.modid.name", name);
    }

    protected void addEnchantment(DeferredHolder<Enchantment, Enchantment> enchantment, String name) {
        add("sounds." + ModUtils.getModId() + "." + enchantment.getId().getPath(), name);
//        add("sounds.modid.name", name);
    }

    protected void createBook(String id, String name, String landing) {
        this.add("items."+id+".book.name", name);
        this.add("info."+id+".book.landing", landing);
    }

    protected void addElement(Supplier<Element> element, String name) {
        ResourceLocation id = MineArcanaRegistries.ELEMENT.getKey(element.get());

        if (id == null) {
            throw new IllegalStateException("Tried to add language entry for unregistered element: " + element);
        }

        add("element." + id.getNamespace() + ".element/" + id.getPath().toLowerCase(Locale.ROOT), name);
    }

    protected void addAspect(Supplier<Aspect> aspect, String name) {
        ResourceLocation id = MineArcanaRegistries.ASPECT.getKey(aspect.get());

        if (id == null) {
            throw new IllegalStateException("Tried to add language entry for unregistered element: " + aspect);
        }

        add("aspect." + id.getNamespace().toLowerCase() + "." + id.getPath().toLowerCase(Locale.ROOT), name);
    }
}
