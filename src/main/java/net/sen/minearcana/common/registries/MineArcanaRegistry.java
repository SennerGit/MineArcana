package net.sen.minearcana.common.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.*;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.utils.aspect.Aspect;
import net.sen.minearcana.common.utils.element.Element;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class MineArcanaRegistry {
    // ------------------- ELEMENT REGISTRY -------------------
    public static final Registry<Element> ELEMENT = new RegistryBuilder<>(Keys.ELEMENT).sync(true).create();

    // ------------------- ASPECT REGISTRY -------------------
    public static final Registry<Aspect> ASPECT = new RegistryBuilder<>(Keys.ASPECT).sync(true).create();

    public static void register(NewRegistryEvent event) {
        event.register(ELEMENT);
        event.register(ASPECT);
    }

    public static final class Keys {
        public static final ResourceKey<Registry<Element>> ELEMENT = createRegistryKey("element");
        public static final ResourceKey<Registry<Aspect>> ASPECT = createRegistryKey("aspect");

        private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
            return ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MineArcana.MODID, name));
        }
    }
}
