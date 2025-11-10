package net.sen.minearcana.data.aspects;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class MagicAspectDataProvider implements DataProvider {
    protected final PackOutput.PathProvider pathProvider;
    protected final PackOutput.PathProvider tagPathProvider;
    protected final CompletableFuture<HolderLookup.Provider> lookupProvider;
    protected final String modId;
    @Nullable
    protected final ExistingFileHelper existingFileHelper;

    // aspectId → (item → value)
    protected final Map<ResourceLocation, Map<ResourceLocation, Integer>> aspectEntries = new HashMap<>();
    protected final Map<ResourceLocation, Set<ResourceLocation>> tagEntries = new HashMap<>();

    public MagicAspectDataProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            CompletableFuture<?> unused,
            String modId,
            @Nullable ExistingFileHelper existingFileHelper
    ) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "magic_aspects");
        this.tagPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "tags/item");
        this.lookupProvider = lookupProvider;
        this.modId = modId;
        this.existingFileHelper = existingFileHelper;
    }

    protected abstract void addAspects(HolderLookup.Provider provider);

    protected AspectBuilder addAspect(String aspectName, TagKey<Item>... tagKeys) {
        // Create aspect entry map
        ResourceLocation aspectId = ResourceLocation.fromNamespaceAndPath(modId, aspectName);
        Map<ResourceLocation, Integer> entries = new LinkedHashMap<>();
        aspectEntries.put(aspectId, entries);

        // Collect tag locations for this aspect
        Set<ResourceLocation> aspectTags = new LinkedHashSet<>();
        for (TagKey<Item> tagKey : tagKeys) {
            aspectTags.add(tagKey.location());
            // Ensure the tag exists in tagEntries
            tagEntries.computeIfAbsent(tagKey.location(), k -> new LinkedHashSet<>());
        }

        return new AspectBuilder(entries, aspectTags);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return lookupProvider.thenCompose(provider -> {
            aspectEntries.clear();
            tagEntries.clear();

            addAspects(provider);

            List<CompletableFuture<?>> saves = new ArrayList<>();
            for (var entry : aspectEntries.entrySet()) {
                saves.add(saveAspect(output, entry.getKey(), entry.getValue()));
            }

            for (var entry : tagEntries.entrySet()) {
                saves.add(saveTag(output, entry.getKey(), entry.getValue()));
            }

            return CompletableFuture.allOf(saves.toArray(CompletableFuture[]::new));
        });
    }

    private CompletableFuture<?> saveAspect(CachedOutput output, ResourceLocation aspectId, Map<ResourceLocation, Integer> itemsAndValues) {
        JsonObject root = new JsonObject();
        JsonArray valuesArray = new JsonArray();

        for (var entry : itemsAndValues.entrySet()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("item", entry.getKey().toString());
            obj.addProperty("value", entry.getValue());
            valuesArray.add(obj);
        }

        root.add("values", valuesArray);
        Path path = pathProvider.json(aspectId);
        return DataProvider.saveStable(output, root, path);
    }

    private CompletableFuture<?> saveTag(CachedOutput output, ResourceLocation tagId, Set<ResourceLocation> items) {
        JsonObject root = new JsonObject();
        JsonArray values = new JsonArray();

        for (ResourceLocation item : items) {
            values.add(item.toString()); // <- add as string, not as JSON object
        }

        root.add("values", values);

        Path path = tagPathProvider.json(tagId); // writes to data/minearcana/tags/items/<tag>.json
        return DataProvider.saveStable(output, root, path);
    }


    @Override
    public String getName() {
        return "Magic Aspect Data Provider for " + modId;
    }

    public class AspectBuilder {
        private final Map<ResourceLocation, Integer> entries;
        private final Set<ResourceLocation> aspectTags;

        private AspectBuilder(Map<ResourceLocation, Integer> entries, Set<ResourceLocation> aspectTags) {
            this.entries = entries;
            this.aspectTags = aspectTags;
        }

        /** Add a single item with a custom value */
        public AspectBuilder addItem(Item item, int value) {
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
            if (itemId == null) throw new IllegalArgumentException("Item not registered: " + item);
            entries.put(itemId, value);

            // Add this item to all tags for this aspect
            for (ResourceLocation tagId : aspectTags) {
                tagEntries.computeIfAbsent(tagId, k -> new LinkedHashSet<>()).add(itemId);
            }

            return this;
        }

        /** Add a single item with default value = 1 */
        public AspectBuilder addItem(Item item) {
            return addItem(item, 1);
        }
    }
}
