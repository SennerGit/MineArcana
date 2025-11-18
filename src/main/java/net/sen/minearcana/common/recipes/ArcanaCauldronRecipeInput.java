package net.sen.minearcana.common.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public record ArcanaCauldronRecipeInput(
        ItemStack input,
        FluidStack fluid,
        List<AspectRequirement> aspects,
        int temperature
) implements RecipeInput {
    public static final MapCodec<ArcanaCauldronRecipeInput> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ItemStack.CODEC.fieldOf("itemInput").forGetter(ArcanaCauldronRecipeInput::input),
                    FluidStack.CODEC.fieldOf("fluid").forGetter(ArcanaCauldronRecipeInput::fluid),
                    com.mojang.serialization.Codec.list(AspectRequirement.CODEC).fieldOf("aspects").forGetter(ArcanaCauldronRecipeInput::aspects),
                    com.mojang.serialization.Codec.INT.fieldOf("temperature").forGetter(ArcanaCauldronRecipeInput::temperature)
            ).apply(instance, ArcanaCauldronRecipeInput::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ArcanaCauldronRecipeInput> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ArcanaCauldronRecipeInput decode(RegistryFriendlyByteBuf buffer) {
            ItemStack input = ItemStack.STREAM_CODEC.decode(buffer);
            FluidStack fluid = FluidStack.STREAM_CODEC.decode(buffer);
            int size = buffer.readVarInt();
            List<AspectRequirement> aspects = new ArrayList<>(size);
            for (int i = 0; i < size; i++) aspects.add(AspectRequirement.STREAM_CODEC.decode(buffer));
            int temp = buffer.readVarInt();
            return new ArcanaCauldronRecipeInput(input, fluid, aspects, temp);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, ArcanaCauldronRecipeInput value) {
            ItemStack.STREAM_CODEC.encode(buffer, value.input());
            FluidStack.STREAM_CODEC.encode(buffer, value.fluid());
            buffer.writeVarInt(value.aspects().size());
            for (AspectRequirement ar : value.aspects()) AspectRequirement.STREAM_CODEC.encode(buffer, ar);
            buffer.writeVarInt(value.temperature());
        }
    };

    @Override
    public ItemStack getItem(int index) {
        return index == 0 ? input : ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 1;
    }
}
