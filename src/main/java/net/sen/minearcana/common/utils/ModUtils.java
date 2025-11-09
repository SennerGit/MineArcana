package net.sen.minearcana.common.utils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.sen.minearcana.MineArcana;

import java.util.Locale;
import java.util.function.Supplier;

public class ModUtils {
    public static String getModId() {
        return MineArcana.MODID.toLowerCase(Locale.ROOT);
    }

    public static String getMinecraftId() {
        return "minecraft".toLowerCase(Locale.ROOT);
    }

    public static String getBlockId(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath().toLowerCase(Locale.ROOT);
    }

    public static String getBlockId(Block block, String extra) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath().toLowerCase(Locale.ROOT) + "_" + extra;
    }

    public static ResourceLocation getBlockPath(Block block) {
        return getModPath(getBlockId(block));
    }

    public static ResourceLocation getItemPath(Item item) {
        return getModPath(getItemId(item));
    }

    public static String getItemId(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath().toLowerCase(Locale.ROOT);
    }

    public static String getItemLikeId(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath().toLowerCase(Locale.ROOT);
    }

    public static String getItemTagId(TagKey<Item> item) {
        return item.location().getNamespace().toLowerCase(Locale.ROOT);
    }

    public static String getCreativeModeId(CreativeModeTab creativeModeTab) {
        return BuiltInRegistries.CREATIVE_MODE_TAB.getKey(creativeModeTab).getPath().toLowerCase(Locale.ROOT);
    }

    public static String getEntityId(EntityType<?> entityType) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath().toLowerCase(Locale.ROOT);
    }

    public static ResourceLocation getModPath(String name) {
        return ResourceLocation.fromNamespaceAndPath(getModId(), name.toLowerCase(Locale.ROOT));
    }

    public static ResourceLocation getMinecraftPath(String name) {
        return ResourceLocation.fromNamespaceAndPath("minecraft", name.toLowerCase(Locale.ROOT));
    }

    public static String getTagId(TagKey<?> tagKey) {
        return Tags.getTagTranslationKey(tagKey).toLowerCase(Locale.ROOT);
    }

    public static ResourceLocation entityRendererLoc(String entityFolder, String entityName) {
        return getModPath("textures/entity/" + entityFolder + "/" + entityName + ".png");
    }
    public static ResourceLocation entityRendererLoc(String name) {
        return getModPath("textures/entity/" + name + "/" + name + ".png");
    }

    public static ResourceLocation registerEntityVariantLocation(Supplier<EntityType<?>> entityType, String name) {
        return entityRendererLoc(getEntityId(entityType.get()) + "_" + name);
    }

    public static ResourceLocation registerEntityVariantLocation(Supplier<EntityType<?>> entityType) {
        return entityRendererLoc(getEntityId(entityType.get()));
    }

    public static ResourceLocation getRendererLocation(EntityType<? extends Entity> entityType) {
        return ModUtils.entityRendererLoc(ModUtils.getEntityId(entityType).toLowerCase(Locale.ROOT));
    }

    public static ResourceLocation getRendererLocation(EntityType<? extends Entity> entityType, String name) {
        return ModUtils.entityRendererLoc(ModUtils.getEntityId(entityType).toLowerCase(Locale.ROOT) + "_" + name.toLowerCase(Locale.ROOT));
    }
}