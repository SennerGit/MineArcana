package net.sen.minearcana.common.utils.aspect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.common.util.DataComponentUtil;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.registries.MineArcanaAspects;
import net.sen.minearcana.common.registries.MineArcanaRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AspectStack implements MutableDataComponentHolder {
    public static final Codec<Holder<Aspect>> ASPECT_HOLDER_CODEC =
            MineArcanaRegistries.ASPECT.holderByNameCodec();

    public static final Codec<AspectStack> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ASPECT_HOLDER_CODEC.fieldOf("id").forGetter(AspectStack::getHolder),
                    Codec.INT.fieldOf("amount").forGetter(AspectStack::getAmount)
            ).apply(instance, AspectStack::new)
    );

    public static final Codec<List<AspectStack>> LIST_CODEC = Codec.list(CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, AspectStack> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public AspectStack decode(RegistryFriendlyByteBuf buffer) {
            int amount = buffer.readVarInt();
            if (amount <= 0) return empty();

            int aspectId = buffer.readVarInt();
            if (aspectId < 0) return empty();

            Holder<Aspect> holder;
            try {
                holder = MineArcanaRegistries.ASPECT.getHolder(aspectId).get();
            } catch (IndexOutOfBoundsException e) {
                return empty();
            }
            return new AspectStack(holder, amount);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, AspectStack stack) {
            buffer.writeVarInt(stack.getAmount());
            if (stack.isEmpty()) {
                buffer.writeVarInt(-1);
                return;
            }
            buffer.writeVarInt(MineArcanaRegistries.ASPECT.getId(stack.getHolder().value()));
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, List<AspectStack>> LIST_STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, List<AspectStack>>() {
        @Override
        public List<AspectStack> decode(RegistryFriendlyByteBuf buffer) {
            int size = buffer.readVarInt();
            List<AspectStack> list = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                list.add(STREAM_CODEC.decode(buffer));
            }
            return list;
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, List<AspectStack> value) {
            buffer.writeVarInt(value.size());
            for (AspectStack stack : value) {
                STREAM_CODEC.encode(buffer, stack);
            }
        }
    };


    public static AspectStack empty() {
        return new AspectStack(Holder.direct(MineArcanaAspects.EMPTY.get()), 0);
    }

    private final Holder<Aspect> holder;
    private int amount;
    private final PatchedDataComponentMap components;

    // Constructors
    public AspectStack(Holder<Aspect> holder, int amount) {
        this.holder = holder;
        this.amount = amount;
        this.components = new PatchedDataComponentMap(DataComponentMap.EMPTY);
    }

    public AspectStack(Aspect aspect, int amount) {
        this.holder = Holder.direct(aspect); // create holder for standalone aspects
        this.amount = amount;
        this.components = new PatchedDataComponentMap(DataComponentMap.EMPTY);
    }

    // Accessors
    public Aspect getAspect() {
        return holder.value();
    }

    public Holder<Aspect> getHolder() {
        return holder;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void grow(int add) {
        this.amount += add;
    }

    public void shrink(int remove) {
        this.amount -= remove;
    }

    public boolean isEmpty() {
        return this.amount <= 0 || holder.unwrapKey().filter(k -> k.equals(MineArcanaAspects.EMPTY)).isPresent();

//        return this.amount <= 0 || holder.value() == Holder.direct(MineArcanaAspects.EMPTY.get());
    }

    // Matching
    public boolean is(TagKey<Aspect> tag) {
        return holder.is(tag);
    }

    public boolean is(Aspect aspect) {
        return holder.value() == aspect;
    }

    public boolean is(Holder<Aspect> otherHolder) {
        return holder == otherHolder;
    }

    public boolean is(Predicate<Holder<Aspect>> predicate) {
        return predicate.test(holder);
    }

    public Stream<TagKey<Aspect>> getTags() {
        return holder.tags();
    }

    // Copy / split
    public AspectStack copy() {
        if (isEmpty()) return empty();
        return new AspectStack(holder, amount);
    }

    public AspectStack copyWithAmount(int newAmount) {
        AspectStack copy = copy();
        copy.setAmount(newAmount);
        return copy;
    }

    public AspectStack split(int splitAmount) {
        int take = Math.min(splitAmount, this.amount);
        AspectStack result = copyWithAmount(take);
        shrink(take);
        return result;
    }

    // Utility
    public static boolean isSameAspect(AspectStack a, AspectStack b) {
        return isSameAspect(a.getAspect(), b.getAspect());
    }
    public static boolean isSameAspect(Aspect a, AspectStack b) {
        return isSameAspect(a, b.getAspect());
    }
    public static boolean isSameAspect(AspectStack a, Aspect b) {
        return isSameAspect(a.getAspect(), b);
    }

    public static boolean isSameAspect(Aspect a, Aspect b) {
        return a == b;
    }

    public static boolean isSameAspectSameAmount(AspectStack a, AspectStack b) {
        return isSameAspect(a, b) && a.amount == b.amount;
    }

    // MutableDataComponentHolder implementations
    @Override
    public <T> @Nullable T set(DataComponentType<? super T> type, @Nullable T component) {
        return components.set(type, component);
    }

    @Override
    public <T> @Nullable T remove(DataComponentType<? extends T> type) {
        return components.remove(type);
    }

    @Override
    public void applyComponents(DataComponentPatch patch) {
        components.applyPatch(patch);
    }

    @Override
    public void applyComponents(DataComponentMap map) {
        components.setAll(map);
    }

    @Override
    public DataComponentMap getComponents() {
        return components;
    }

    // Parse a CompoundTag into an AspectStack
    public static AspectStack parseOptional(HolderLookup.Provider registries, CompoundTag tag) {
        if (tag == null || tag.isEmpty()) return empty();

        try {
            // Decode using the Codec
            return CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag)
                    .resultOrPartial(err -> MineArcana.LOGGER.error("Failed to parse AspectStack: {}", err))
                    .orElse(empty());
        } catch (Exception e) {
            MineArcana.LOGGER.error("Exception parsing AspectStack", e);
            return empty();
        }
    }

    // Save this AspectStack into a Tag
    public CompoundTag save(HolderLookup.Provider registries) {
        if (isEmpty()) return new CompoundTag();

        try {
            // Encode using the Codec
            return (CompoundTag) DataComponentUtil.wrapEncodingExceptions(this, CODEC, registries, new CompoundTag());
        } catch (Exception e) {
            MineArcana.LOGGER.error("Failed to save AspectStack: {}", e);
            return new CompoundTag();
        }
    }

}
