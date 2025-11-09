package net.sen.minearcana.common.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sen.minearcana.common.utils.ModUtils;

import java.util.Collection;
import java.util.Locale;

import static net.sen.minearcana.common.registries.MineArcanaEntityTypes.SPAWN_EGGS;

public class MineArcanaCreativeModeTabs {
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModUtils.getModId());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MINEARCANA_TAB = TABS.register("minearcana_tab", () -> {
        return CreativeModeTab.builder()
                .icon(() -> new ItemStack(Items.STICK))
                .title(Component.translatable(generateName("minearcana_tab")))
                .withSearchBar()
                .displayItems(((itemDisplayParameters, output) -> {
                            //Generated Lists
//                    createTestTubeItemsAlphabetical(output);
                            createSpawnEggsAlphabetical(output);
                        })
                )
                .build();
    });

    private static String generateName(String id) {
        return ("itemgroup." + ModUtils.getModId() + "." + id).toLowerCase(Locale.ROOT);
    }

    private static void createSpawnEggsAlphabetical(CreativeModeTab.Output output) {
        Collection<? extends Item> eggs = SPAWN_EGGS.getEntries().stream().map(DeferredHolder::value).toList();
        eggs.forEach(output::accept);
    }

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}
