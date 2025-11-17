package net.sen.minearcana.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.sen.minearcana.common.utils.aspect.AspectStack;

import java.util.List;

public record ArcanaCauldronRecipeInput(
        ItemStack input,
        FluidStack fluid,
        List<AspectStack> aspect,
        int temperature
) implements RecipeInput {

    // ItemStack + Fluid + Aspects + Temperature
    public static final MapCodec<ArcanaCauldronRecipeInput> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            ItemStack.CODEC.fieldOf("itemInput").forGetter(ArcanaCauldronRecipeInput::input),
                            FluidStack.CODEC.fieldOf("fluid").forGetter(ArcanaCauldronRecipeInput::fluid),
                            AspectStack.LIST_CODEC.fieldOf("aspect").forGetter(ArcanaCauldronRecipeInput::aspect),
                            Codec.INT.fieldOf("temperature").forGetter(ArcanaCauldronRecipeInput::temperature)
                    ).apply(instance, ArcanaCauldronRecipeInput::new)
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, ArcanaCauldronRecipeInput> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public ArcanaCauldronRecipeInput decode(RegistryFriendlyByteBuf buffer) {
                    ItemStack input = ItemStack.STREAM_CODEC.decode(buffer);
                    FluidStack fluid = FluidStack.STREAM_CODEC.decode(buffer);
                    List<AspectStack> aspects = AspectStack.LIST_STREAM_CODEC.decode(buffer);
                    int temp = buffer.readVarInt();
                    return new ArcanaCauldronRecipeInput(input, fluid, aspects, temp);
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buffer, ArcanaCauldronRecipeInput value) {
                    ItemStack.STREAM_CODEC.encode(buffer, value.input());
                    FluidStack.STREAM_CODEC.encode(buffer, value.fluid());
                    AspectStack.LIST_STREAM_CODEC.encode(buffer, value.aspect());
                    buffer.writeVarInt(value.temperature());
                }
            };

    // -------- REQUIRED METHODS -------- //
    @Override
    public ItemStack getItem(int index) {
        return index == 0 ? input : ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 1;
    }
}
