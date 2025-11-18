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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.recipes.ArcanaCauldronRecipe;
import net.sen.minearcana.common.recipes.AspectRequirement;
import net.sen.minearcana.common.registries.MineArcanaBlocks;
import net.sen.minearcana.common.registries.MineArcanaRegistries;
import net.sen.minearcana.common.utils.aspect.AspectStack;

import javax.annotation.Nullable;
import java.util.List;

public class ArcanaCauldronRecipeCategory implements IRecipeCategory<ArcanaCauldronRecipe> {

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(MineArcana.MODID, "arcana_cauldron");
    public static final RecipeType<ArcanaCauldronRecipe> ARCANA_CAULDRON_RECIPE_TYPE =
            new RecipeType<>(UID, ArcanaCauldronRecipe.class);

    private final IDrawable icon;
    private final IDrawable background;

    public ArcanaCauldronRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 60);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(MineArcanaBlocks.ARCANA_CAULDRON.get()));
    }

    @Override
    public RecipeType<ArcanaCauldronRecipe> getRecipeType() {
        return ARCANA_CAULDRON_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block." + MineArcana.MODID + ".arcana_cauldron");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ArcanaCauldronRecipe recipe, IFocusGroup focuses) {
        // Item input slot
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 22)
                .addItemStack(recipe.input().input());

        // Fluid input slot (render visually)
        FluidStack fluid = recipe.input().fluid();
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 10)
                .addFluidStack(fluid.getFluid(), fluid.getAmount()); // width=16, height=32

        // Output slot
        builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 22)
                .addItemStack(recipe.output());
    }

    @Override
    public void draw(ArcanaCauldronRecipe recipe, IRecipeSlotsView recipeSlotsView,
                     GuiGraphics guiGraphics, double mouseX, double mouseY) {

        Font font = Minecraft.getInstance().font;
        int x = 10;
        int y = 5;

        // Draw temperature
        guiGraphics.drawString(font, "Temp: " + recipe.input().temperature(), x, y, 0xFFFFFF);

        // Draw aspects
        y += 12;
        guiGraphics.drawString(font, "Aspects:", x, y, 0xFFFFFF);

        y += 12;
        List<AspectRequirement> requirements = recipe.input().aspects();
        for (AspectRequirement req : requirements) {
            AspectStack stack = new AspectStack(MineArcanaRegistries.ASPECT.get(req.aspectId()), req.amount());
            String line = stack.getAspect().toString() + " x" + stack.getAmount();
            guiGraphics.drawString(font, line, x, y, 0xAAAAFF);
            y += 10;
        }
    }
}
