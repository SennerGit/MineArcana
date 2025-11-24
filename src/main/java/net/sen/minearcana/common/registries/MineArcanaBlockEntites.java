package net.sen.minearcana.common.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sen.minearcana.common.blocks.entities.*;
import net.sen.minearcana.common.utils.ModUtils;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class MineArcanaBlockEntites {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ModUtils.getModId());

    public static final Supplier<BlockEntityType<ArcanaCauldronBlockEntity>> ARCANA_CAULDRON = createBlockEntity("cauldron", ArcanaCauldronBlockEntity::new, MineArcanaBlocks.ARCANA_CAULDRON);
    public static final Supplier<BlockEntityType<AspectExtractorBlockEntity>> ASPECT_EXTRACTOR = createBlockEntity("aspect_extractor", AspectExtractorBlockEntity::new, MineArcanaBlocks.ASPECT_EXTRACTOR);
    public static final Supplier<BlockEntityType<AspectMixerBlockEntity>> ASPECT_MIXER = createBlockEntity("aspect_mixer", AspectMixerBlockEntity::new, MineArcanaBlocks.ASPECT_MIXER);
    public static final Supplier<BlockEntityType<AspectTankBlockEntity>> ASPECT_TANK = createBlockEntity("aspect_tank", AspectTankBlockEntity::new, MineArcanaBlocks.ASPECT_TANK);
    public static final Supplier<BlockEntityType<AspectPipeBlockEntity>> ASPECT_PIPE = createBlockEntity("aspect_pipe", AspectPipeBlockEntity::new, MineArcanaBlocks.ASPECT_PIPE);
    public static final Supplier<BlockEntityType<BloodMarkerBlockEntity>> BLOOD_MARKER = createBlockEntity("blood_marker", BloodMarkerBlockEntity::new, MineArcanaBlocks.BLOOD_MARKER);
    public static final Supplier<BlockEntityType<AltarBlockEntity>> ALTAR_BLOCK = createBlockEntity("altar_block", AltarBlockEntity::new, MineArcanaBlocks.ALTAR_BLOCK);
    public static final Supplier<BlockEntityType<ArcaneLightEmitterBlockEntity>> ARCANA_LIGHT_EMITTER = createBlockEntity("arcane_light_emitter", ArcaneLightEmitterBlockEntity::new, MineArcanaBlocks.ARCANA_LIGHT_EMITTER);
    public static final Supplier<BlockEntityType<ArcaneLightReceiverBlockEntity>> ARCANA_LIGHT_RECEIVER = createBlockEntity("arcane_light_receiver", ArcaneLightReceiverBlockEntity::new, MineArcanaBlocks.ARCANA_LIGHT_RECEIVER);

    @SuppressWarnings("DataFlowIssue")
    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> createBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Collection<? extends Supplier<? extends Block>> blocks) {
        return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(supplier, blocks.stream().map(Supplier::get).toList().toArray(new Block[0])).build(null));
    }
    @SafeVarargs
    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> createBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<? extends Block>... blocks) {
        return createBlockEntity(name, supplier, List.of(blocks));
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
