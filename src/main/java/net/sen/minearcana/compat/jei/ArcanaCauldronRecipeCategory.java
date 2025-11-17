package net.sen.minearcana.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.recipes.ArcanaCauldronRecipe;
import net.sen.minearcana.common.registries.MineArcanaBlocks;
import net.sen.minearcana.common.utils.aspect.AspectStack;

import javax.annotation.Nullable;

public class ArcanaCauldronRecipeCategory implements IRecipeCategory<ArcanaCauldronRecipe> {

    public static final ResourceLocation UID =
            ResourceLocation.fromNamespaceAndPath(MineArcana.MODID, "arcana_cauldron");

    public static final RecipeType<ArcanaCauldronRecipe> ARCANA_CAULDRON_RECIPE_TYPE =
            new RecipeType<>(UID, ArcanaCauldronRecipe.class);

    private final IDrawable icon;
    private final IDrawable background;

    public ArcanaCauldronRecipeCategory(IGuiHelper helper) {
        // Simple JEI background: 150×60 blank panel
        this.background = helper.createBlankDrawable(150, 60);

        this.icon = helper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK,
                new ItemStack(MineArcanaBlocks.ARCANA_CAULDRON.get())
        );
    }

    @Override
    public RecipeType<ArcanaCauldronRecipe> getRecipeType() {
        return ARCANA_CAULDRON_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(
                "block." + MineArcana.MODID + "." +
                        BuiltInRegistries.BLOCK.getKey(MineArcanaBlocks.ARCANA_CAULDRON.get()).getPath()
        );
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    /**
     * Layout:
     * [Item Input] [Fluid] [Aspects ...] -> [Output]
     */
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ArcanaCauldronRecipe recipe, IFocusGroup focuses) {

        // Item input (usually glass bottle)
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 22)
                .addItemStack(recipe.input().input());

        // Fluid input
        FluidStack fs = recipe.input().fluid();
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 22)
                .addFluidStack(fs.getFluid(), fs.getAmount());

        // Output potion
        builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 22)
                .addItemStack(recipe.output());
    }

    @Override
    public void draw(ArcanaCauldronRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int x = 10;
        int y = 5;
        Font font = Minecraft.getInstance().font;

        // Draw temperature
        guiGraphics.drawString(font,
                "Temp: " + recipe.input().temperature(),
                x, y,
                0xFFFFFF);

        // Draw aspects
        y += 12;
        guiGraphics.drawString(font,
                "Aspects:",
                x, y,
                0xFFFFFF);

        y += 12;
        for (AspectStack asp : recipe.input().aspect()) {
            String line = asp.getAspect().toString() + " x" + asp.getAmount();
            guiGraphics.drawString(font, line, x, y, 0xAAAAFF);
            y += 10;
        }
    }
}
