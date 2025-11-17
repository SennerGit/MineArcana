package net.sen.minearcana.data.recipes;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;

import net.sen.minearcana.common.recipes.ArcanaCauldronRecipe;
import net.sen.minearcana.common.recipes.ArcanaCauldronRecipeInput;
import net.sen.minearcana.common.utils.aspect.AspectStack;

import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ArcanaCauldronRecipeBuilder implements RecipeBuilder {

    private final RecipeCategory category;

    private final ItemStack inputItem;
    private final ItemStack result;

    private final FluidStack fluid;
    private final List<AspectStack> aspects;
    private final int temperature;
    private final float experience;

    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable private String group;

    private ArcanaCauldronRecipeBuilder(
            RecipeCategory category,
            ItemStack inputItem,
            ItemStack result,
            FluidStack fluid,
            int temperature,
            List<AspectStack> aspects,
            float experience
    ) {
        this.category = category;
        this.inputItem = inputItem;
        this.result = result;
        this.fluid = fluid;
        this.temperature = temperature;
        this.aspects = aspects;
        this.experience = experience;
    }

    // Factory
    public static ArcanaCauldronRecipeBuilder brewing(
            ItemLike input,
            int count,
            ItemStack result,
            FluidStack fluid,
            int temperature,
            List<AspectStack> aspects,
            float xp
    ) {
        return new ArcanaCauldronRecipeBuilder(
                RecipeCategory.BREWING,
                new ItemStack(input, count),
                result,
                fluid,
                temperature,
                aspects,
                xp
        );
    }

    // ------------------------------------
    // Required RecipeBuilder Methods
    // ------------------------------------
    @Override
    public ArcanaCauldronRecipeBuilder unlockedBy(String key, Criterion<?> criterion) {
        this.criteria.put(key, criterion);
        return this;
    }

    @Override
    public ArcanaCauldronRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return result.getItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {

        // -------- Advancement --------
        Advancement.Builder adv = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(adv::addCriterion);

        // -------- Build Recipe --------
        ArcanaCauldronRecipe recipe = new ArcanaCauldronRecipe(
                new ArcanaCauldronRecipeInput(
                        inputItem,
                        fluid,
                        aspects,
                        temperature
                ),
                result.copy()
        );

        // -------- Save --------
        output.accept(
                id,
                recipe,
                adv.build(id.withPrefix("recipes/" + category.getFolderName() + "/"))
        );
    }
}
