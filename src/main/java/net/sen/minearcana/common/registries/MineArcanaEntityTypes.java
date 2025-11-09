package net.sen.minearcana.common.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sen.minearcana.common.utils.ModUtils;

import java.util.function.Supplier;

public class MineArcanaEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, ModUtils.getModId());
    public static final DeferredRegister<Item> SPAWN_EGGS = DeferredRegister.create(Registries.ITEM, ModUtils.getModId());

    //Create Entities Here
    //Normal

    //Bosses

    private static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> make(String name, EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height, int primary, int secondary) {
        return make(name, factory, classification, width, height, primary, secondary, 0.0F);
    }

    private static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> make(String name, EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height, int primary, int secondary, float ridingOffset) {
        return make(name, factory, classification, width, height, false, primary, secondary, ridingOffset);
    }

    //Same as below, but with riding offset set to 0.0F;
    private static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> make(String name, EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height, boolean fireproof, int primary, int secondary) {
        return make(name, factory, classification, width, height, fireproof, primary, secondary, 0.0F);
    }

    private static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> make(String name, EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height, boolean fireproof, int primary, int secondary, float ridingOffset) {
        return build(name, makeBuilder(factory, classification, width, height, 80, 3, ridingOffset), fireproof, primary, secondary);
    }

    //Same as below, but with riding offset set to 0.0F;
    private static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> make(String name, EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height, float eyeHeight, boolean fireproof, int primary, int secondary) {
        return make(name, factory, classification, width, height, eyeHeight, fireproof, primary, secondary, 0.0F);
    }

    private static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> make(String name, EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height, float eyeHeight, boolean fireproof, int primary, int secondary, float ridingOffset) {
        return build(name, makeBuilder(factory, classification, width, height, 80, 3, ridingOffset).eyeHeight(eyeHeight), fireproof, primary, secondary);
    }

    private static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> buildNoEgg(String name, EntityType.Builder<E> builder, boolean fireproof) {
        if (fireproof) builder.fireImmune();
        return ENTITIES.register(ModUtils.getModPath(name).getPath(), () -> builder.build(name));
    }

    @SuppressWarnings("unchecked")
    private static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> build(String name, EntityType.Builder<E> builder, boolean fireproof, int backgroundColor, int highlightColor) {
        if (fireproof) builder.fireImmune();
        DeferredHolder<EntityType<?>, EntityType<E>> entityType = ENTITIES.register(ModUtils.getModPath(name).getPath(), () -> builder.build(name));
        SPAWN_EGGS.register(ModUtils.getModPath(name + "_spawn_egg").getPath(), () -> new DeferredSpawnEggItem(() -> (EntityType<? extends Mob>) entityType.get(), backgroundColor, highlightColor, new Item.Properties()));
        return entityType;
    }

    private static <E extends Entity> EntityType.Builder<E> makeBuilder(EntityType.EntityFactory<E> factory, MobCategory category, float width, float height, int range, int interval, float ridingOffset) {
        return EntityType.Builder.of(factory, category)
                .sized(width, height)
                .setTrackingRange(range)
                .updateInterval(interval)
                .setShouldReceiveVelocityUpdates(true)
                .ridingOffset(ridingOffset);
    }

    private static Supplier<DeferredSpawnEggItem> createSpawnEgg(String name, Supplier<? extends EntityType<? extends Mob>> entity, int backgroundColor, int highlightColor) {
        return SPAWN_EGGS.register(name, () -> new DeferredSpawnEggItem(entity, backgroundColor, highlightColor, new Item.Properties()));
    }

    @SubscribeEvent
    public static void onEntitySpawnPlacements(RegisterSpawnPlacementsEvent event)
    {
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(final EntityAttributeCreationEvent event)
    {
    }

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
        SPAWN_EGGS.register(eventBus);
    }
}
