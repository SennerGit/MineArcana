package net.sen.minearcana.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.sen.minearcana.client.renderer.BeamWorldRenderer;
import net.sen.minearcana.common.utils.lightbeams.BeamEngine;
import net.sen.minearcana.data.aspects.MagicAspectDataLoader;

import java.util.*;

public class MineArcanaClientEventHandler {
    // ------------------- Registration -------------------
    public static void MineArcanaClientEventHandlerRegistry(IEventBus bus) {
        // Tooltip listener
        NeoForge.EVENT_BUS.addListener(MineArcanaClientEventHandler::onToolTip);
        NeoForge.EVENT_BUS.addListener(MineArcanaClientEventHandler::onRenderWorld);
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
        List<Component> names = new ArrayList<>();

        for (TagKey<Item> tag : BuiltInRegistries.ITEM.getTagNames().toList()) {
            ResourceLocation tagLocation = tag.location();

            if (!tagLocation.getPath().startsWith("element/") && !tagLocation.getNamespace().contains("element")) {
                continue;
            }

            if (stack.is(tag)) {
                String modid = tagLocation.getNamespace();
                String elementName = tagLocation.getPath();
                String translation = "element." + modid + "." + elementName;
                names.add(Component.translatable(translation).withStyle(style -> style.withColor(0xFFFFFF)));
            }
        }

        if (!names.isEmpty()) {
            MutableComponent joined = Component.literal("");

            for (int i = 0; i < names.size(); i++) {
                joined = joined.append(names.get(i));
                if (i < names.size() - 1) joined = joined.append(Component.literal(", "));
            }

            tooltip.add(
                    Component.literal("Elements: ")
                            .withStyle(style -> style.withColor(0xFFD700)) // gold
                            .append(joined)
            );
        }
    }

    // ------------------- Aspects -------------------
    private static void addMagicAspectTooltip(ItemStack stack, List<Component> tooltip) {
        Map<String, Integer> aspects = MagicAspectDataLoader.ITEM_ASPECT_VALUES
                .getOrDefault(stack.getItem(), Collections.emptyMap());

        if (aspects.isEmpty()) return;

        // Header
        tooltip.add(Component.literal("Aspects:")
                .withColor(0xFF00FF)); // magenta

        // Each aspect entry
        aspects.forEach((aspectId, value) -> {
            // Translation key support, e.g. "aspect.minearcana.infernal"
            String[] parts = aspectId.split(":");
            String modid = parts.length > 1 ? parts[0] : "minecraft";
            String name = parts.length > 1 ? parts[1] : aspectId;

            String translationKey = "aspect." + modid + "." + name;

            // If not translated, the translatable component will fall back to key text
            MutableComponent aspectText = Component.translatable(translationKey)
                    .withColor(0x00FFFF);

            MutableComponent line = Component.literal("  ")
                    .append(aspectText)
                    .append(Component.literal(" x" + value));

            tooltip.add(line);
        });
    }

    // ------------------- Render -------------------
    public static void onRenderWorld(RenderLevelStageEvent event) {
        BeamWorldRenderer.onRenderWorld(event);

        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (mc.level != null) {
            BeamEngine.getClient(level).tick();
        }
    }
}
