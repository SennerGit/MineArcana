package net.sen.minearcana.data.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.sen.minearcana.common.registries.MineArcanaAspects;
import net.sen.minearcana.common.utils.aspect.AspectStack;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends ModRecipeHelper {

    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(pOutput, registries);
    }

    @Override
    void miscRecipes(RecipeOutput recipeOutput) {

    }

    @Override
    void pestleAndMotarRecipes(RecipeOutput recipeOutput) {
    }

    @Override
    void stoneRecipes(RecipeOutput recipeOutput) {

    }

    @Override
    void foodRecipes(RecipeOutput recipeOutput) {

    }

    @Override
    void metalRecipes(RecipeOutput recipeOutput) {

    }

    @Override
    void woodRecipes(RecipeOutput recipeOutput) {

    }

    @Override
    void flowerRecipes(RecipeOutput recipeOutput) {

    }

    @Override
    void vanilla(RecipeOutput output) {

    }

    @Override
    void alchemyRecipes(RecipeOutput recipeOutput) {
        this.createCauldronRecipe(
                recipeOutput,
                Potions.REGENERATION,
                new FluidStack(Fluids.WATER, 1000),
                100,
                new AspectStack(MineArcanaAspects.LIFE.get(), 3),
                new AspectStack(MineArcanaAspects.DIVINE.get(), 3)
        );
    }
}
