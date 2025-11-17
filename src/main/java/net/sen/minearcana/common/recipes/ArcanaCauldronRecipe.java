package net.sen.minearcana.common.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.sen.minearcana.common.registries.MineArcanaRecipes;
import net.sen.minearcana.common.utils.aspect.AspectStack;

import java.util.List;

public record ArcanaCauldronRecipe(ArcanaCauldronRecipeInput input, ItemStack output) implements Recipe<ArcanaCauldronRecipeInput> {

    @Override
    public boolean matches(ArcanaCauldronRecipeInput inventory, Level level) {
        if (!inventory.fluid().getFluid().equals(this.input.fluid().getFluid())) return false;

        int temp = inventory.temperature();
        if (temp < input().temperature()) return false;

        List<AspectStack> aspects = inventory.aspect();

        for (AspectStack required : input().aspect()) {
            boolean found = false;
            for (AspectStack stored : aspects) {
                if (stored.getAspect().equals(required.getAspect()) && stored.getAmount() >= required.getAmount()) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }

        return true;
    }

    @Override
    public ItemStack assemble(ArcanaCauldronRecipeInput input, HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output.copy();
    }

    public Item getInputItem() {
        return input().input().getItem();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MineArcanaRecipes.ARCANA_CAULDRON_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return MineArcanaRecipes.ARCANA_CAULDRON_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ArcanaCauldronRecipe> {
        public static final MapCodec<ArcanaCauldronRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        ArcanaCauldronRecipeInput.CODEC.fieldOf("input").forGetter(ArcanaCauldronRecipe::input),
                        ItemStack.CODEC.fieldOf("output").forGetter(ArcanaCauldronRecipe::output)
                ).apply(inst, ArcanaCauldronRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ArcanaCauldronRecipe> STREAM_CODEC =
                new StreamCodec<>() {
                    @Override
                    public ArcanaCauldronRecipe decode(RegistryFriendlyByteBuf buffer) {
                        ArcanaCauldronRecipeInput input = ArcanaCauldronRecipeInput.STREAM_CODEC.decode(buffer);
                        ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
                        return new ArcanaCauldronRecipe(input, output);
                    }

                    @Override
                    public void encode(RegistryFriendlyByteBuf buffer, ArcanaCauldronRecipe recipe) {
                        ArcanaCauldronRecipeInput.STREAM_CODEC.encode(buffer, recipe.input());
                        ItemStack.STREAM_CODEC.encode(buffer, recipe.output());
                    }
                };

        @Override
        public MapCodec<ArcanaCauldronRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ArcanaCauldronRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
