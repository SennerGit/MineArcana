package net.sen.minearcana.compat;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.recipes.ArcanaCauldronRecipe;
import net.sen.minearcana.common.registries.MineArcanaBlocks;
import net.sen.minearcana.common.registries.MineArcanaRecipes;
import net.sen.minearcana.compat.jei.ArcanaCauldronRecipeCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JeiPlugin
public class MineArcanaJei implements IModPlugin {
    public static IJeiHelpers jeihelper;
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MineArcana.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        jeihelper = registration.getJeiHelpers();
        registration.addRecipeCategories(new ArcanaCauldronRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel world = Objects.requireNonNull(Minecraft.getInstance().level);

        List<ArcanaCauldronRecipe> arcanaCauldronRecipes = registerRecipeList(world, MineArcanaRecipes.ARCANA_CAULDRON_RECIPE_TYPE.get());
        registration.addRecipes(ArcanaCauldronRecipeCategory.ARCANA_CAULDRON_RECIPE_TYPE, arcanaCauldronRecipes);
    }


    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        // Add the Arcana Cauldron block as a recipe catalyst
        registration.addRecipeCatalyst(
                new ItemStack(MineArcanaBlocks.ARCANA_CAULDRON.get().asItem()),
                ArcanaCauldronRecipeCategory.ARCANA_CAULDRON_RECIPE_TYPE
        );
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        IModPlugin.super.registerGuiHandlers(registration);
    }

    public static <T extends Recipe<?>> List<T> registerRecipeList(ClientLevel world, RecipeType recipeType) {
        List<RecipeHolder<T>> recipes = ImmutableList.copyOf(world.getRecipeManager().getAllRecipesFor(recipeType));
        List<T> allClean = new ArrayList<>();
        recipes.forEach(arcanaCauldronRecipeRecipeHolder -> {
            allClean.add(arcanaCauldronRecipeRecipeHolder.value());
        });

        return allClean;
    }
}