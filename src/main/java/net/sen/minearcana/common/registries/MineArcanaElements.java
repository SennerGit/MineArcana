package net.sen.minearcana.common.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.utils.element.Element;

import java.util.function.Supplier;

public class MineArcanaElements {
    public static final DeferredRegister<Element> ELEMENTS = DeferredRegister.create(MineArcanaRegistries.ELEMENT, MineArcana.MODID);

    public static final Supplier<Element> EMPTY = registerElement("empty", 0xFFFFFF);

    public static final Supplier<Element> CHAOS = registerElement("chaos", 0xFFFFFF);
    public static final Supplier<Element> ORDER = registerElement("order", 0xFFFFFF);
    public static final Supplier<Element> CREATION = registerElement("creation", 0xFFFFFF);
    public static final Supplier<Element> SPIRIT = registerElement("spirit", 0xFFFFFF);
    public static final Supplier<Element> COSMIC = registerElement("cosmic", 0xFFFFFF);
//    public static final Supplier<Element> ELDRITCH = registerElement("eldritch", 0xFFFFFF);

    public static Supplier<Element> registerElement(String name, int colour) {
        return ELEMENTS.register(name, () -> new Element(name, colour));
    }

    public static void register(IEventBus eventBus) {
        ELEMENTS.register(eventBus);
    }
}
