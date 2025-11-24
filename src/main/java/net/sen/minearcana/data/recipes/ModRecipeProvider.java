package net.sen.minearcana.data.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.sen.minearcana.common.registries.MineArcanaAspects;
import net.sen.minearcana.common.registries.MineArcanaBlocks;
import net.sen.minearcana.common.registries.MineArcanaItems;
import net.sen.minearcana.common.utils.ModUtils;
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
    void toolsRecipes(RecipeOutput pRecipeOutput) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, MineArcanaItems.RITUAL_KNIFE.get(), 1)
                .requires(Items.IRON_SWORD)
                .requires(MineArcanaItems.ARCANE_DUST.get())
                .unlockedBy("has_arcane_dust", has(MineArcanaItems.ARCANE_DUST.get()))
                .save(pRecipeOutput, ModUtils.getModPath("ritual_knife_from_iron_sword"));
    }

    @Override
    void machineRecipes(RecipeOutput pRecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MineArcanaBlocks.ARCANA_CAULDRON.get(), 1)
                .define('A', MineArcanaItems.ARCANE_DUST.get())
                .define('C', Items.CAULDRON)
                .pattern("AAA")
                .pattern("ACA")
                .pattern("AAA")
                .unlockedBy("has_arcane_dust", has(MineArcanaItems.ARCANE_DUST.get()))
                .save(pRecipeOutput, ModUtils.getModPath("arcana_cauldron"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MineArcanaBlocks.ASPECT_EXTRACTOR.get(), 1)
                .define('I', Items.IRON_INGOT)
                .define('A', MineArcanaItems.ARCANE_DUST.get())
                .define('F', Items.FURNACE)
                .pattern("IAI")
                .pattern("AFA")
                .pattern("IAI")
                .unlockedBy("has_arcane_dust", has(MineArcanaItems.ARCANE_DUST.get()))
                .save(pRecipeOutput, ModUtils.getModPath("aspect_extractor"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MineArcanaBlocks.ASPECT_PIPE.get(), 12)
                .define('I', Items.IRON_INGOT)
                .define('A', MineArcanaItems.ARCANE_DUST.get())
                .pattern("III")
                .pattern("AAA")
                .pattern("III")
                .unlockedBy("has_arcane_dust", has(MineArcanaItems.ARCANE_DUST.get()))
                .save(pRecipeOutput, ModUtils.getModPath("aspect_pipe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MineArcanaBlocks.ASPECT_TANK.get(), 1)
                .define('I', Items.IRON_INGOT)
                .define('A', MineArcanaItems.ARCANE_DUST.get())
                .define('G', Items.GLASS_PANE)
                .pattern("IAI")
                .pattern("AGA")
                .pattern("IAI")
                .unlockedBy("has_arcane_dust", has(MineArcanaItems.ARCANE_DUST.get()))
                .save(pRecipeOutput, ModUtils.getModPath("aspect_tank"));
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
