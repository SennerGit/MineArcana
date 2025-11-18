package net.sen.minearcana.common.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.sen.minearcana.common.registries.MineArcanaRecipes;

import java.util.List;

public record ArcanaCauldronRecipe(ArcanaCauldronRecipeInput input, ItemStack output) implements Recipe<ArcanaCauldronRecipeInput> {

    @Override
    public boolean matches(ArcanaCauldronRecipeInput inventory, Level level) {
        if (!inventory.fluid().getFluid().equals(this.input.fluid().getFluid())) return false;
        if (inventory.temperature() < input().temperature()) return false;

        List<AspectRequirement> required = input().aspects();
        List<AspectRequirement> present = inventory.aspects();

        // For matching we compare by ResourceLocation equality + amount
        for (AspectRequirement req : required) {
            boolean found = false;
            for (AspectRequirement have : present) {
                if (have.aspectId().equals(req.aspectId()) && have.amount() >= req.amount()) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }

        return true;
    }

    @Override
    public ItemStack assemble(ArcanaCauldronRecipeInput p_267088_, HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_44001_, int p_44002_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output.copy();
    }

    public ItemStack getOutput() {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MineArcanaRecipes.ARCANA_CAULDRON_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return MineArcanaRecipes.ARCANA_CAULDRON_RECIPE_TYPE.get();
    }

    // Codec / stream codec for serializer
    public static class Serializer implements RecipeSerializer<ArcanaCauldronRecipe> {
        public static final MapCodec<ArcanaCauldronRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        ArcanaCauldronRecipeInput.CODEC.fieldOf("input").forGetter(ArcanaCauldronRecipe::input),
                        ItemStack.CODEC.fieldOf("output").forGetter(ArcanaCauldronRecipe::output)
                ).apply(inst, ArcanaCauldronRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, ArcanaCauldronRecipe> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public ArcanaCauldronRecipe decode(RegistryFriendlyByteBuf buffer) {
                ArcanaCauldronRecipeInput i = ArcanaCauldronRecipeInput.STREAM_CODEC.decode(buffer);
                ItemStack out = ItemStack.STREAM_CODEC.decode(buffer);
                return new ArcanaCauldronRecipe(i, out);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, ArcanaCauldronRecipe value) {
                ArcanaCauldronRecipeInput.STREAM_CODEC.encode(buffer, value.input());
                ItemStack.STREAM_CODEC.encode(buffer, value.output());
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
