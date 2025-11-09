package net.sen.minearcana.data.models;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.ItemLayerModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.sen.minearcana.common.utils.ModUtils;

import java.util.function.Supplier;

public abstract class ModItemModelHelper extends ItemModelProvider {
    public ModItemModelHelper(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ModUtils.getModId(), existingFileHelper);
    }

    @Override
    protected void registerModels() {
        generateItems();
        generateBlocks();
    }

    protected abstract void generateItems();

    protected abstract void generateBlocks();


    protected ItemModelBuilder simpleItem(Supplier<Item> item) {
        return basicItem(item.get());
    }

    protected ItemModelBuilder simpleBlockItem(Supplier<Block> block) {
        return withExistingParent(ModUtils.getBlockId(block.get()),
                ModUtils.getModPath("block/" + ModUtils.getBlockId(block.get())));
    }

    protected ItemModelBuilder simpleBookItem(Supplier<Item> item) {
        return withExistingParent(ModUtils.getItemId(item.get()),
                ModUtils.getMinecraftPath("item/generated")).texture("layer0",
                ModUtils.getModPath("item/" + ModUtils.getItemId(item.get())));
    }

    protected ItemModelBuilder simpleBlockItemUi(Supplier<Block> item) {
        return withExistingParent(ModUtils.getBlockId(item.get()),
                ModUtils.getMinecraftPath("item/generated")).texture("layer0",
                ModUtils.getModPath("item/" + ModUtils.getBlockId(item.get())));
    }

    protected ItemModelBuilder simpleDoorBlock(Supplier<DoorBlock> item) {
        return withExistingParent(ModUtils.getBlockId(item.get()),
                ModUtils.getMinecraftPath("item/generated")).texture("layer0",
                ModUtils.getModPath("item/" + ModUtils.getBlockId(item.get())));
    }

    protected ItemModelBuilder handheldItem(Supplier<Item> item) {
        return withExistingParent(ModUtils.getItemId(item.get()),
                ModUtils.getMinecraftPath("item/handheld")).texture("layer0",
                ModUtils.getModPath("item/" + ModUtils.getItemId(item.get())));
    }

    protected void buttonItem(Supplier<ButtonBlock> block, Supplier<Block> baseBlock) {
        this.withExistingParent(BuiltInRegistries.BLOCK.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture", ModUtils.getModPath("block/" + BuiltInRegistries.BLOCK.getKey(baseBlock.get()).getPath()));
    }

    protected void fenceItem(Supplier<FenceBlock> block, Supplier<Block> baseBlock) {
        this.withExistingParent(BuiltInRegistries.BLOCK.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture", ModUtils.getModPath("block/" + BuiltInRegistries.BLOCK.getKey(baseBlock.get()).getPath()));
    }

    protected void wallItem(Supplier<WallBlock> block, Supplier<Block> baseBlock) {
        this.withExistingParent(BuiltInRegistries.BLOCK.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", ModUtils.getModPath("block/" + BuiltInRegistries.BLOCK.getKey(baseBlock.get()).getPath()));
    }

    protected ItemModelBuilder complexBlock(Block block) {
        return withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), ModUtils.getModPath("block/" + BuiltInRegistries.BLOCK.getKey(block).getPath()));
    }

    protected ItemModelBuilder saplingItem(Supplier<SaplingBlock> item) {
        return withExistingParent(ModUtils.getBlockId(item.get()),
                ModUtils.getMinecraftPath("item/generated")).texture("layer0",
                ModUtils.getModPath("block/" + ModUtils.getBlockId(item.get())));
    }

    protected ItemModelBuilder mushroomItem(Supplier<Block> item) {
        return withExistingParent(ModUtils.getBlockId(item.get()),
                ModUtils.getMinecraftPath("item/generated")).texture("layer0",
                ModUtils.getModPath("block/" + ModUtils.getBlockId(item.get())));
    }

    protected ItemModelBuilder simpleBlockTexture(Supplier<Block> item) {
        return withExistingParent(ModUtils.getBlockId(item.get()),
                ModUtils.getMinecraftPath("item/generated")).texture("layer0",
                ModUtils.getModPath("block/" + ModUtils.getBlockId(item.get())));
    }

    protected void createArmourSet(Supplier<Item> helmet, Supplier<Item> chestplate, Supplier<Item> leggings, Supplier<Item> boots) {
        createArmourPiece(helmet, "helmet");
        createArmourPiece(chestplate, "chestplate");
        createArmourPiece(leggings, "leggings");
        createArmourPiece(boots, "boots");
    }

    protected void createArmourPiece(Supplier<Item> item, String name) {
//        ItemModelBuilder base = this.simpleItem(items);
//        for (ItemModelGenerators.TrimModelData trim : ItemModelGenerators.GENERATED_TRIM_MODELS) {
//
//        }
        createTrim(item, name, TrimMaterials.QUARTZ, 0.1f);
        createTrim(item, name, TrimMaterials.IRON, 0.2f);
        createTrim(item, name, TrimMaterials.NETHERITE, 0.3f);
        createTrim(item, name, TrimMaterials.REDSTONE, 0.4f);
        createTrim(item, name, TrimMaterials.COPPER, 0.5f);
        createTrim(item, name, TrimMaterials.GOLD, 0.6f);
        createTrim(item, name, TrimMaterials.EMERALD, 0.7f);
        createTrim(item, name, TrimMaterials.DIAMOND, 0.8f);
        createTrim(item, name, TrimMaterials.LAPIS, 0.9f);
        createTrim(item, name, TrimMaterials.AMETHYST, 1.0f);
        createArmor(item);
    }

    protected ItemModelBuilder createTrim(Supplier<Item> item, String name, ResourceKey<TrimMaterial> trimMaterials, float indexId) {
        ItemModelBuilder base = this.simpleItem(item);
        ModelFile trimModel = this.withExistingParent(ModUtils.getItemId(item.get()) + "_" + trimMaterials.location().getPath() + "_trim",
                        ModUtils.getMinecraftPath("item/generated"))
                .texture("layer0", ModUtils.getModPath("item/" + ModUtils.getItemId(item.get())))
//                .texture("layer1", this.mcLoc("trims/items/" + name + "_trim"));

                //@Error Need to include trim but throws error because the trim file doesn't exist
//                .texture("layer1", LostWorldsConstants.mcLoc("minecraft", "trims/items/" + name + "_trim_" + trimMaterials.location().getPath()));
                .texture("layer1", ModUtils.getMinecraftPath("trims/items/" + name + "_trim_" + trimMaterials.location().getPath()));

        return base.override().predicate(ModUtils.getMinecraftPath("trim_type"), indexId).model(trimModel).end();
    }

    protected ItemModelBuilder createArmor(Supplier<Item> item) {
        ModelFile quartzLoc = generateArmorModel(getItemTrim(item, "quartz_trim"));
        ModelFile ironLoc = generateArmorModel(getItemTrim(item, "iron_trim"));
        ModelFile netheriteLoc = generateArmorModel(getItemTrim(item, "netherite_trim"));
        ModelFile redstoneLoc = generateArmorModel(getItemTrim(item, "redstone_trim"));
        ModelFile copperLoc = generateArmorModel(getItemTrim(item, "copper_trim"));
        ModelFile goldLoc = generateArmorModel(getItemTrim(item, "gold_trim"));
        ModelFile emeraldLoc = generateArmorModel(getItemTrim(item, "emerald_trim"));
        ModelFile diamondLoc = generateArmorModel(getItemTrim(item, "diamond_trim"));
        ModelFile lapisLoc = generateArmorModel(getItemTrim(item, "lapis_trim"));
        ModelFile amethystLoc = generateArmorModel(getItemTrim(item, "amethyst_trim"));

        return withExistingParent(ModUtils.getItemId(item.get()),
                ModUtils.getMinecraftPath("item/generated"))
                .override().predicate(ModUtils.getModPath("item/" + ModUtils.getItemId(item.get())), 0.1f).model(quartzLoc).end()
                .override().predicate(ModUtils.getModPath("item/" + ModUtils.getItemId(item.get())), 0.2f).model(ironLoc).end()
                .override().predicate(ModUtils.getModPath("item/" + ModUtils.getItemId(item.get())), 0.3f).model(netheriteLoc).end()
                .override().predicate(ModUtils.getModPath("item/" + ModUtils.getItemId(item.get())), 0.4f).model(redstoneLoc).end()
                .override().predicate(ModUtils.getModPath("item/" + ModUtils.getItemId(item.get())), 0.5f).model(copperLoc).end()
                .override().predicate(ModUtils.getModPath("item/" + ModUtils.getItemId(item.get())), 0.6f).model(goldLoc).end()
                .override().predicate(ModUtils.getModPath("item/" + ModUtils.getItemId(item.get())), 0.7f).model(emeraldLoc).end()
                .override().predicate(ModUtils.getModPath("item/" + ModUtils.getItemId(item.get())), 0.8f).model(diamondLoc).end()
                .override().predicate(ModUtils.getModPath("item/" + ModUtils.getItemId(item.get())), 0.9f).model(lapisLoc).end()
                .override().predicate(ModUtils.getModPath("item/" + ModUtils.getItemId(item.get())), 1.0f).model(amethystLoc).end()
                .texture("layer0", ModUtils.getModPath("item/" + ModUtils.getItemId(item.get())));
    }

    protected static String getItemTrim(Supplier<Item> item, String trimType) {
        return ModUtils.getModId() + ":items/" + ModUtils.getItemId(item.get()) + "_" + trimType;
    }

    protected ItemModelBuilder generateArmorModel(String name, ResourceLocation... layers) {
        return buildItemModel(name, "item/generated", 0, layers);
    }

    protected ItemModelBuilder generateArmorModel(String name) {
        return buildItemModel(name, "item/generated", 0);
    }

    protected ItemModelBuilder buildItemModel(String name, String parent, int emissivity, ResourceLocation... layers) {
        ItemModelBuilder builder = withExistingParent(name, parent);
        for (int i = 0; i < layers.length; i++) {
            builder = builder.texture("layer" + i, layers[i]);
        }
        if (emissivity > 0) builder = builder.customLoader(ItemLayerModelBuilder::begin).emissive(emissivity, emissivity, 0).renderType("minecraft:translucent", 0).end();
        return builder;
    }

    protected ItemModelBuilder buildItemModel(String name, String parent, int emissivity) {
        ItemModelBuilder builder = withExistingParent(name, parent);

        if (emissivity > 0) builder = builder.customLoader(ItemLayerModelBuilder::begin).emissive(emissivity, emissivity, 0).renderType("minecraft:translucent", 0).end();
        return builder;
    }
}
