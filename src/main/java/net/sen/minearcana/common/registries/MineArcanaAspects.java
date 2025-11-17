package net.sen.minearcana.common.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.utils.aspect.Aspect;

import java.util.function.Supplier;

public class MineArcanaAspects {
    public static final DeferredRegister<Aspect> ASPECTS = DeferredRegister.create(MineArcanaRegistries.ASPECT, MineArcana.MODID);

    public static final Supplier<Aspect> EMPTY = registerAspect("empty", 0xFF4500);

    //Chaos Aspects
    public static final Supplier<Aspect> INFERNAL = registerAspect("infernal", 0xFF4500);
    public static final Supplier<Aspect> TAINT = registerAspect("taint", 0xFF4500);
    public static final Supplier<Aspect> STORM = registerAspect("storm", 0xFF4500);
    public static final Supplier<Aspect> FURY = registerAspect("fury", 0xFF4500);
    public static final Supplier<Aspect> ENTROPY = registerAspect("entropy", 0xFF4500);
    public static final Supplier<Aspect> DEMONIC = registerAspect("demonic", 0xFF4500);

    //Order Aspects
    public static final Supplier<Aspect> JUDGMENT = registerAspect("judgment", 0xFF4500);
    public static final Supplier<Aspect> FROST = registerAspect("frost", 0xFF4500);
    public static final Supplier<Aspect> WATER = registerAspect("water", 0xFF4500);
    public static final Supplier<Aspect> DIVINE = registerAspect("divine", 0xFF4500);

    //Creation Aspects
    public static final Supplier<Aspect> EARTH = registerAspect("earth", 0xFF4500);
    public static final Supplier<Aspect> MECHANICAL = registerAspect("mechanical", 0xFF4500);
    public static final Supplier<Aspect> METALLIC = registerAspect("metallic", 0xFF4500);
    public static final Supplier<Aspect> CRYSTAL = registerAspect("crystal", 0xFF4500);
    public static final Supplier<Aspect> NATURE = registerAspect("nature", 0xFF4500);

    //Spirit Aspects
    public static final Supplier<Aspect> LIFE = registerAspect("life", 0xFF4500);
    public static final Supplier<Aspect> DEATH = registerAspect("death", 0xFF4500);
    public static final Supplier<Aspect> LIMBO = registerAspect("limbo", 0xFF4500);

    //Cosmic Aspects
    public static final Supplier<Aspect> ASTRAL = registerAspect("astral", 0xFF4500);
    public static final Supplier<Aspect> VOID = registerAspect("void", 0xFF4500);
    public static final Supplier<Aspect> GRAVITY = registerAspect("gravity", 0xFF4500);

    public static Supplier<Aspect> registerAspect(String name, int colour) {
        return ASPECTS.register(name, () -> new Aspect(name, colour));
    }

    public static void register(IEventBus eventBus) {
        ASPECTS.register(eventBus);
    }
}
