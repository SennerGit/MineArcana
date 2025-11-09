package net.sen.minearcana.common.registries;

import com.google.common.base.Preconditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.sen.minearcana.common.utils.ModUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MineArcanaTags {

    public static class Blocks {
        private static TagKey<Block> createTag(String name) { return TagKey.create(Registries.BLOCK, ModUtils.getModPath(name)); }
    }
    public static class Items {
        private static TagKey<Item> createTag(String name) { return TagKey.create(Registries.ITEM, ModUtils.getModPath(name)); }
    }
    public static class EntityTypes {
        private static TagKey<EntityType<?>> createTag(String name) { return TagKey.create(Registries.ENTITY_TYPE, ModUtils.getModPath(name)); }
    }
    public static class Biomes {
        private static TagKey<Biome> createTag(String name) { return TagKey.create(Registries.BIOME, ModUtils.getModPath(name)); }
    }
    public static class MagicElements {
        public static final TagKey<Item> MAGIC_ELEMENTS = createTag("magic_elements");
        public static final TagKey<Item> CHAOS = createTag("chaos");
        public static final TagKey<Item> ORDER = createTag("order");
        public static final TagKey<Item> CREATION = createTag("creation");
        public static final TagKey<Item> SPIRIT = createTag("spirit");
        public static final TagKey<Item> COSMIC = createTag("cosmic");

        private static TagKey<Item> createTag(String name) { return TagKey.create(Registries.ITEM, ModUtils.getModPath(name)); }
    }
    public static class MagicAspects {
        public static final TagKey<Item> MAGIC_ASPECTS = createTag("magic_aspects");
        public static final TagKey<Item> DIVINE = createTag("divine");
        public static final TagKey<Item> INFERNAL = createTag("infernal");
        public static final TagKey<Item> ASTRAL = createTag("astral");
        public static final TagKey<Item> COSMIC = createTag("cosmic");
        public static final TagKey<Item> LIFE = createTag("life");
        public static final TagKey<Item> DEATH = createTag("death");
        public static final TagKey<Item> EARTH = createTag("earth");
        public static final TagKey<Item> MECHANICAL = createTag("mechanical");
        public static final TagKey<Item> TAINTED = createTag("tainted");

        private static TagKey<Item> createTag(String name) { return TagKey.create(Registries.ITEM, ModUtils.getModPath(name)); }
    }

    /* Code By BluSunrize
     * https://github.com/BluSunrize/ImmersiveEngineering/blob/1.19.2/src/api/java/blusunrize/immersiveengineering/api/IETags.java#L36
     * */
    private static final Map<TagKey<Block>, TagKey<Item>> toItemTag = new HashMap<>();

    static {

    }

    public static TagKey<Item> getItemTag(TagKey<Block> blockTag)
    {
        Preconditions.checkArgument(toItemTag.containsKey(blockTag));
        return toItemTag.get(blockTag);
    }

    public static void forAllBlocktags(BiConsumer<TagKey<Block>, TagKey<Item>> out)
    {
        for(Map.Entry<TagKey<Block>, TagKey<Item>> entry : toItemTag.entrySet())
            out.accept(entry.getKey(), entry.getValue());
    }
}
