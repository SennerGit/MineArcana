package net.sen.minearcana.client.event;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.sen.minearcana.common.registries.MineArcanaTags;
import net.sen.minearcana.data.aspects.MagicAspectDataLoader;

import java.util.*;

public class MineArcanaClientEventHandler {

    // ------------------- Elements -------------------
    private static final Map<TagKey<Item>, String> MAGIC_ELEMENT_ICONS = new LinkedHashMap<>();
    static {
        MAGIC_ELEMENT_ICONS.put(MineArcanaTags.MagicElements.CHAOS, "§6Chaos");
        MAGIC_ELEMENT_ICONS.put(MineArcanaTags.MagicElements.ORDER, "§6Order");
        MAGIC_ELEMENT_ICONS.put(MineArcanaTags.MagicElements.CREATION, "§6Creation");
        MAGIC_ELEMENT_ICONS.put(MineArcanaTags.MagicElements.SPIRIT, "§6Spirit");
        MAGIC_ELEMENT_ICONS.put(MineArcanaTags.MagicElements.COSMIC, "§6Cosmic");
    }

    // ------------------- Aspects -------------------
    private static final Map<TagKey<Item>, String> MAGIC_ASPECT_ICONS = new LinkedHashMap<>();
    static {
        MAGIC_ASPECT_ICONS.put(MineArcanaTags.MagicAspects.DIVINE, "Divine");
        MAGIC_ASPECT_ICONS.put(MineArcanaTags.MagicAspects.INFERNAL, "Infernal");
        MAGIC_ASPECT_ICONS.put(MineArcanaTags.MagicAspects.ASTRAL, "Astral");
        MAGIC_ASPECT_ICONS.put(MineArcanaTags.MagicAspects.COSMIC, "Cosmic");
        MAGIC_ASPECT_ICONS.put(MineArcanaTags.MagicAspects.LIFE, "Life");
        MAGIC_ASPECT_ICONS.put(MineArcanaTags.MagicAspects.DEATH, "Death");
        MAGIC_ASPECT_ICONS.put(MineArcanaTags.MagicAspects.EARTH, "Earth");
        MAGIC_ASPECT_ICONS.put(MineArcanaTags.MagicAspects.MECHANICAL, "Mechanical");
        MAGIC_ASPECT_ICONS.put(MineArcanaTags.MagicAspects.TAINTED, "Tainted");
    }

    // ------------------- Registration -------------------
    public static void MineArcanaClientEventHandlerRegistry(IEventBus bus) {
        // Tooltip listener
        NeoForge.EVENT_BUS.addListener(MineArcanaClientEventHandler::onToolTip);
    }

    // ------------------- Tooltip -------------------
    private static void onToolTip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> tooltip = event.getToolTip();

        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.literal("§7[Hold §eShift§7 for Arcana details]"));
            return;
        }

        addMagicElementTooltip(stack, tooltip);
        addMagicAspectTooltip(stack, tooltip);
    }

    // ------------------- Elements -------------------
    private static void addMagicElementTooltip(ItemStack stack, List<Component> tooltip) {
        Set<String> names = new LinkedHashSet<>();

        for (Map.Entry<TagKey<Item>, String> entry : MAGIC_ELEMENT_ICONS.entrySet()) {
            if (stack.is(entry.getKey())) {
                names.add(entry.getValue());
            }
        }

        if (!names.isEmpty()) {
            String joined = String.join(", ", names);
            tooltip.add(
                    Component.literal("Elements: ")
                            .withStyle(style -> style.withColor(0xFFD700)) // gold
                            .append(Component.literal(joined).withStyle(style -> style.withColor(0xFFFFFF))) // white
            );
        }
    }

    // ------------------- Aspects -------------------
    private static void addMagicAspectTooltip(ItemStack stack, List<Component> tooltip) {
        Map<String, Integer> aspects = MagicAspectDataLoader.ITEM_ASPECT_VALUES.getOrDefault(stack.getItem(), Collections.emptyMap());
        if (!aspects.isEmpty()) {
            tooltip.add(Component.literal("Aspects:").withStyle(style -> style.withColor(0xFF00FF))); // magenta header
            aspects.forEach((aspect, value) -> {
                tooltip.add(Component.literal("  " + aspect + " x" + value).withStyle(style -> style.withColor(0x00FFFF))); // cyan values
            });
        }
    }
}
