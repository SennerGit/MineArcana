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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;
import net.sen.minearcana.common.recipes.ArcanaCauldronRecipe;
import net.sen.minearcana.common.recipes.ArcanaCauldronRecipeInput;
import net.sen.minearcana.common.recipes.AspectRequirement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ArcanaCauldronRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final ItemStack result;
    private final Item input;
    private final int count;
    private final FluidStack fluidStack;
    private final int temperature;
    private final List<AspectRequirement> aspects;
    private final float experience;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private String group;

    private ArcanaCauldronRecipeBuilder(RecipeCategory category, ItemStack result, ItemLike input, int count, FluidStack fluidStack, int temperature, List<AspectRequirement> aspects, float experience) {
        this.category = category;
        this.result = result;
        this.input = input.asItem();
        this.count = count;
        this.fluidStack = fluidStack;
        this.temperature = temperature;
        this.aspects = aspects;
        this.experience = experience;
    }

    public static ArcanaCauldronRecipeBuilder brewing(ItemStack result, int count, FluidStack fluidStack, int temperature, List<AspectRequirement> aspects, float experience) {
        return new ArcanaCauldronRecipeBuilder(RecipeCategory.BREWING, result, Items.GLASS_BOTTLE, count, fluidStack, temperature, aspects, experience);
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(String groupName) {
        this.group = groupName;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        Advancement.Builder advancement = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);

        ArcanaCauldronRecipe recipe = new ArcanaCauldronRecipe(
                new ArcanaCauldronRecipeInput(
                        new ItemStack(this.input, this.count),
                        this.fluidStack,
                        this.aspects,
                        this.temperature
                ),
                this.result.copy()
        );

        recipeOutput.accept(id, recipe, advancement.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }
}
