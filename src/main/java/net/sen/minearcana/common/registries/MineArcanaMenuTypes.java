package net.sen.minearcana.common.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.screens.*;

import java.util.function.Supplier;

public class MineArcanaMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MineArcana.MODID);

    public static final Supplier<MenuType<WandCraftingStationMenu>> WAND_CRAFTING_STATION_MENU = createMenu("wand_crafting_station_menu", WandCraftingStationMenu::new);

    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> createMenu(String name, MenuType.MenuSupplier<T> factory) {
        return MENUS.register(name, () -> new MenuType<>(factory, FeatureFlags.VANILLA_SET));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
