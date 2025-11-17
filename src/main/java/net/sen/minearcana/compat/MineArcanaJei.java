package net.sen.minearcana.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.recipes.ArcanaCauldronRecipe;
import net.sen.minearcana.common.registries.MineArcanaBlocks;
import net.sen.minearcana.common.registries.MineArcanaRecipes;
import net.sen.minearcana.compat.jei.*;

import java.util.List;

@JeiPlugin
public class MineArcanaJei implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(MineArcana.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ArcanaCauldronRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<ArcanaCauldronRecipe> arcanaCauldronRecipeCategories = recipeManager.getAllRecipesFor(MineArcanaRecipes.ARCANA_CAULDRON_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(ArcanaCauldronRecipeCategory.ARCANA_CAULDRON_RECIPE_TYPE, arcanaCauldronRecipeCategories);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
//        registration.addRecipeClickArea(ArcanaCauldronScreen.class, 74, 30, 22, 20, ArcanaCauldronRecipeCategory.ARCANA_CAULDRON_RECIPE_TYPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(MineArcanaBlocks.ARCANA_CAULDRON.get().asItem()), ArcanaCauldronRecipeCategory.ARCANA_CAULDRON_RECIPE_TYPE);
    }
}
