package net.sen.minearcana.data.aspects;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.item.Item;
import net.sen.minearcana.MineArcana;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MagicAspectDataLoader extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new Gson();

    // aspectName -> (itemId -> value)
    private static final Map<String, Map<String, Integer>> RAW_ASPECT_DATA = new HashMap<>();

    // item -> (aspectName -> value)
    public static final Map<Item, Map<String, Integer>> ITEM_ASPECT_VALUES = new HashMap<>();

    public MagicAspectDataLoader() {
        super(GSON, "magic_aspects");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, net.minecraft.util.profiling.ProfilerFiller profiler) {
        RAW_ASPECT_DATA.clear();
        ITEM_ASPECT_VALUES.clear();

        // 1️⃣ Parse JSON
        jsonMap.forEach((id, json) -> {
            try {
                JsonObject root = json.getAsJsonObject();
                JsonArray valuesArray = root.getAsJsonArray("values");

                Map<String, Integer> aspectData = new HashMap<>();
                for (JsonElement element : valuesArray) {
                    JsonObject entry = element.getAsJsonObject();
                    if (!entry.has("item") || !entry.has("value")) continue;

                    String itemId = entry.get("item").getAsString();
                    int value = entry.get("value").getAsInt();
                    aspectData.put(itemId, value);
                }

                RAW_ASPECT_DATA.put(id.toString(), aspectData);
            } catch (Exception e) {
                MineArcana.LOGGER.error("Failed to parse aspect JSON for {}: {}", id, e.getMessage());
            }
        });

        // 2️⃣ Map items to aspects
        for (Map.Entry<String, Map<String, Integer>> entry : RAW_ASPECT_DATA.entrySet()) {
            String aspect = entry.getKey();
            Map<String, Integer> items = entry.getValue();

            for (Map.Entry<String, Integer> itemEntry : items.entrySet()) {
                String itemId = itemEntry.getKey();
                int value = itemEntry.getValue();

                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(itemId));
                if (item != null) {
                    ITEM_ASPECT_VALUES.computeIfAbsent(item, k -> new HashMap<>())
                            .put(aspect, value);
                } else {
                    MineArcana.LOGGER.warn("Unknown item ID '{}' in aspect '{}'", itemId, aspect);
                }
            }
        }

        MineArcana.LOGGER.info("Loaded {} aspect definitions for {} items",
                RAW_ASPECT_DATA.size(), ITEM_ASPECT_VALUES.size());
    }
}
