package net.sen.minearcana.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record AspectRequirement(ResourceLocation aspectId, int amount) {
    public static final Codec<AspectRequirement> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("aspect").forGetter(AspectRequirement::aspectId),
                    com.mojang.serialization.Codec.INT.fieldOf("amount").forGetter(AspectRequirement::amount)
            ).apply(instance, AspectRequirement::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, AspectRequirement> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public AspectRequirement decode(RegistryFriendlyByteBuf buffer) {
            ResourceLocation id = buffer.readResourceLocation();
            int amt = buffer.readVarInt();
            return new AspectRequirement(id, amt);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, AspectRequirement value) {
            buffer.writeResourceLocation(value.aspectId());
            buffer.writeVarInt(value.amount());
        }
    };
}
