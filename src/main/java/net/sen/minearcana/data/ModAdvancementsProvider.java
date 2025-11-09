package net.sen.minearcana.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.sen.minearcana.common.utils.ModUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementsProvider extends AdvancementProvider {
    public ModAdvancementsProvider(DataGenerator generatorIn, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(generatorIn.getPackOutput(), registries, existingFileHelper, List.of(
                new BaseAdvancements()
        ));
    }

    public static class BaseAdvancements implements AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider pRegistries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {

        }
    }

    private static AdvancementHolder createAdvancementDimensionParent(String tree, String id, Item iconBlock, Item backgroundBlock, ResourceKey<Level> levelResourceKey, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        String newId = new String(tree + "/" + id);

        AdvancementHolder newAdvancement = Advancement.Builder.advancement()
                .display(rootDisplay(
                        iconBlock,
                        advancementLoc(tree, id, "name"),
                        advancementLoc(tree, id, "desc"),
                        Optional.of(ModUtils.getModPath("textures/block/" + Item.getId(backgroundBlock) + ".png"))
                ))
                .addCriterion("has_entered_dimension_" + levelResourceKey.location().getPath(), ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(levelResourceKey))
                .save(saver, ModUtils.getModPath(newId), existingFileHelper);

        return newAdvancement;
    }
    private static AdvancementHolder createAdvancementCraftingParent(String tree, String id, Item iconBlock, Item backgroundBlock, Item goal, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        String newId = new String(tree + "/" + id);

        AdvancementHolder newAdvancement = Advancement.Builder.advancement()
                .display(rootDisplay(
                        iconBlock,
                        advancementLoc(tree, id, "name"),
                        advancementLoc(tree, id, "desc"),
                        Optional.of(ModUtils.getModPath("textures/block/" + Item.getId(backgroundBlock) + ".png"))
                ))
                .addCriterion("has_crafted_" + Item.getId(goal), InventoryChangeTrigger.TriggerInstance.hasItems(goal))
                .save(saver, ModUtils.getModPath(newId), existingFileHelper);

        return newAdvancement;
    }

    private static AdvancementHolder createAdvancementCrafting(String tree, String id, AdvancementHolder parent, Item iconBlock, Item goal, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        String newId = new String(tree + "/" + id);

        AdvancementHolder newAdvancement = Advancement.Builder.advancement()
                .display(rootDisplay(
                        iconBlock,
                        advancementLoc(tree, id, "name"),
                        advancementLoc(tree, id, "desc"),
                        null
                ))
                .parent(parent)
                .addCriterion("has_crafted_" + Item.getId(goal), InventoryChangeTrigger.TriggerInstance.hasItems(goal))
                .save(saver, ModUtils.getModPath(newId), existingFileHelper);

        return newAdvancement;
    }

    protected static DisplayInfo rootDisplay(ItemLike icon, String titleKey, String descKey, Optional<ResourceLocation> background) {
        return new DisplayInfo(
                new ItemStack(icon.asItem()),
                Component.translatable(titleKey),
                Component.translatable(descKey),
                background,
                AdvancementType.TASK,
                false,
                false,
                false
        );
    }

    private static String advancementLoc(String name, String base, String type) {
        return "advancement." + ModUtils.getModId() + "." + name + "." + base + "." + type;
    }
}
