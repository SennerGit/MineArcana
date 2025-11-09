package net.sen.minearcana.data.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
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
    }
}
