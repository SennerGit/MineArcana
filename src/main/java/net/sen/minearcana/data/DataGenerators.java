package net.sen.minearcana.data;

import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.data.language.*;
import net.sen.minearcana.data.loottable.*;
import net.sen.minearcana.data.models.*;
import net.sen.minearcana.data.recipes.*;
import net.sen.minearcana.data.tags.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = MineArcana.MODID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        PackOutput output = generator.getPackOutput();

        //Automating stuff
        addArmorTrims(helper);

        //Client Data
        generator.addProvider(event.includeClient(), new ModLanguageEnUsProvider(output, "en_us"));
        generator.addProvider(event.includeClient(), new ModBlockModelProvider(output, helper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, helper));
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, helper));
        generator.addProvider(event.includeClient(), new ModGlobalLootModifierProvider(output, lookupProvider));
        generator.addProvider(event.includeClient(), new ModSoundProvider(output, helper));

        //Server Data
        DatapackBuiltinEntriesProvider datapackProvider = new ModRegistriesProvider(output, lookupProvider);
        CompletableFuture<HolderLookup.Provider> registryProvider = datapackProvider.getRegistryProvider();

        generator.addProvider(event.includeServer(), datapackProvider);
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, lookupProvider ));
        generator.addProvider(event.includeServer(), new ModLootTableProvider(output, lookupProvider ));

        //Tags
        BlockTagsProvider blockTags = new ModBlockTagGenerator(output, lookupProvider , helper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ModItemTagGenerator(output, lookupProvider , blockTags.contentsGetter(), helper));
        generator.addProvider(event.includeServer(), new ModPaintingVariantTagProvider(output, lookupProvider , helper));
        generator.addProvider(event.includeServer(), new ModPoiTypeTagProvider(output, lookupProvider , helper));
        generator.addProvider(event.includeServer(), new ModFluidTagsProvider(output, lookupProvider , helper));
        generator.addProvider(event.includeServer(), new ModBiomeTagProvider(output, lookupProvider , helper));

        //pack.meta
        generator.addProvider(true, new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(
                Component.literal("MineArcana Datapack"),
                DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
                Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE))
        )));
    }

    private static void addArmorTrims(ExistingFileHelper helper) {
        addTrimToArmor(helper, "boots_trim_");
        addTrimToArmor(helper, "chestplate_trim_");
        addTrimToArmor(helper, "helmet_trim_");
        addTrimToArmor(helper, "leggings_trim_");
    }

    private static void addTrimToArmor(ExistingFileHelper existingFileHelper, String armorPiece) {
        existingFileHelper.trackGenerated(ResourceLocation.withDefaultNamespace(armorPiece + TrimMaterials.QUARTZ.location().getPath()), PackType.CLIENT_RESOURCES, ".png", "textures/trims/items");
        existingFileHelper.trackGenerated(ResourceLocation.withDefaultNamespace(armorPiece + TrimMaterials.IRON.location().getPath()), PackType.CLIENT_RESOURCES, ".png", "textures/trims/items");
        existingFileHelper.trackGenerated(ResourceLocation.withDefaultNamespace(armorPiece + TrimMaterials.NETHERITE.location().getPath()), PackType.CLIENT_RESOURCES, ".png", "textures/trims/items");
        existingFileHelper.trackGenerated(ResourceLocation.withDefaultNamespace(armorPiece + TrimMaterials.REDSTONE.location().getPath()), PackType.CLIENT_RESOURCES, ".png", "textures/trims/items");
        existingFileHelper.trackGenerated(ResourceLocation.withDefaultNamespace(armorPiece + TrimMaterials.COPPER.location().getPath()), PackType.CLIENT_RESOURCES, ".png", "textures/trims/items");
        existingFileHelper.trackGenerated(ResourceLocation.withDefaultNamespace(armorPiece + TrimMaterials.GOLD.location().getPath()), PackType.CLIENT_RESOURCES, ".png", "textures/trims/items");
        existingFileHelper.trackGenerated(ResourceLocation.withDefaultNamespace(armorPiece + TrimMaterials.EMERALD.location().getPath()), PackType.CLIENT_RESOURCES, ".png", "textures/trims/items");
        existingFileHelper.trackGenerated(ResourceLocation.withDefaultNamespace(armorPiece + TrimMaterials.DIAMOND.location().getPath()), PackType.CLIENT_RESOURCES, ".png", "textures/trims/items");
        existingFileHelper.trackGenerated(ResourceLocation.withDefaultNamespace(armorPiece + TrimMaterials.LAPIS.location().getPath()), PackType.CLIENT_RESOURCES, ".png", "textures/trims/items");
        existingFileHelper.trackGenerated(ResourceLocation.withDefaultNamespace(armorPiece + TrimMaterials.AMETHYST.location().getPath()), PackType.CLIENT_RESOURCES, ".png", "textures/trims/items");
    }
}
