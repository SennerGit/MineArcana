package net.sen.minearcana.common.registries;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sen.minearcana.common.utils.ModUtils;

import java.util.Locale;
import java.util.function.Supplier;

public class MineArcanaBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ModUtils.getModId());
    private static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(ModUtils.getModId());

    public static final Supplier<Block> CAULDRON = createBlock("cauldron");
    public static final Supplier<Block> ASPECT_EXTRACTOR = createBlock("aspect_extractor");
    public static final Supplier<Block> ASPECT_CONDENSER = createBlock("aspect_condenser");
    public static final Supplier<Block> ASPECT_MIXER = createBlock("aspect_mixer");
    public static final Supplier<Block> ASPECT_TANK = createBlock("aspect_tank");

    private static Supplier<Block> createBlock(String name) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    }

    private static Supplier<Block> createBlock(String name, BlockBehaviour.Properties properties) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new Block(properties));
    }

    private static Supplier<Block> createBlock(String name, RotatedPillarBlock block) {
        return createBlock(name, block);
    }

    private static Supplier<Block> createShortGrass(String name) {
        return createBlock(name, () -> new TallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)));
    }

    private static Supplier<Block> createTallGrass(String name) {
        return createBlock(name, () -> new TallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS)));
    }

    private static Supplier<RotatedPillarBlock> createLog(String name) {
        return createRotatedPillarBlock(name, MapColor.WOOD, MapColor.PODZOL, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG));
    }

    private static Supplier<RotatedPillarBlock> createRotatedPillarBlock(String name, MapColor topMapColor, MapColor sideMapColor, BlockBehaviour.Properties properties) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new RotatedPillarBlock(
                properties
        ));
    }

    private static Supplier<SlabBlock> createWoodSlab(String name) {
        return createSlab(name, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB));
    }
    private static Supplier<SlabBlock> createSlab(String name, BlockBehaviour.Properties properties) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new SlabBlock(properties));
    }

    private static Supplier<WallBlock> createWall(String name, BlockBehaviour.Properties properties) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new WallBlock(properties));
    }

    private static Supplier<LeavesBlock> createLeaves(String name) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new LeavesBlock(
                BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)
        ));
    }

    private static Supplier<StairBlock> createLegacyStairs(String name, Supplier<Block> baseBlock) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new StairBlock(baseBlock.get().defaultBlockState(), BlockBehaviour.Properties.ofLegacyCopy(baseBlock.get())));
    }

    private static Supplier<StairBlock> createStairs(String name, Supplier<Block> baseBlock) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new StairBlock(baseBlock.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(baseBlock.get())));
    }

    private static Supplier<PressurePlateBlock> createWoodPressurePlate(String name, BlockSetType blockSetType) {
        return createPressurePlate(name, blockSetType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE));
    }

    private static Supplier<PressurePlateBlock> createPressurePlate(String name, BlockSetType blockSetType, BlockBehaviour.Properties properties) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new PressurePlateBlock(blockSetType, properties));
    }

    private static Supplier<DoorBlock> createWoodDoor(String name, BlockSetType blockSetType) {
        return createDoor(name, blockSetType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE));
    }

    private static Supplier<DoorBlock> createDoor(String name, BlockSetType blockSetType, BlockBehaviour.Properties properties) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new DoorBlock(blockSetType, properties));
    }

    private static Supplier<TrapDoorBlock> createWoodTrapdoor(String name, BlockSetType blockSetType) {
        return createTrapdoor(name, blockSetType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE));
    }

    private static Supplier<TrapDoorBlock> createTrapdoor(String name, BlockSetType blockSetType, BlockBehaviour.Properties properties) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new TrapDoorBlock(blockSetType, properties));
    }

    private static Supplier<FenceBlock> createWoodFence(String name) {
        return createFence(name, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE));
    }

    private static Supplier<FenceBlock> createFence(String name, BlockBehaviour.Properties properties) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new FenceBlock(properties));
    }

    private static Supplier<FenceGateBlock> createWoodFenceGate(String name, WoodType woodType) {
        return createFenceGate(name, woodType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE));
    }

    private static Supplier<FenceGateBlock> createFenceGate(String name, WoodType woodType, BlockBehaviour.Properties properties) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new FenceGateBlock(woodType, properties));
    }

    private static Supplier<ButtonBlock> createWoodButton(String name, BlockSetType blockSetType) {
        return createButton(name, blockSetType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE));
    }

    private static Supplier<ButtonBlock> createButton(String name, BlockSetType blockSetType, BlockBehaviour.Properties properties) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new ButtonBlock(blockSetType, 30, properties));
    }

    private static Supplier<StandingSignBlock> createWoodSign(String name, WoodType woodType) {
        return createSign(name, woodType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE));
    }

    private static Supplier<StandingSignBlock> createSign(String name, WoodType woodType, BlockBehaviour.Properties properties) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new StandingSignBlock(woodType, properties));
    }

    private static Supplier<WallSignBlock> createWoodWallSign(String name, WoodType woodType) {
        return createWallSign(name, woodType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE));
    }

    private static Supplier<WallSignBlock> createWallSign(String name, WoodType woodType, BlockBehaviour.Properties properties) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new WallSignBlock(woodType, properties));
    }

    private static Supplier<CeilingHangingSignBlock> createWoodHangingSign(String name, WoodType woodType) {
        return createHangingSign(name, woodType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE));
    }

    private static Supplier<CeilingHangingSignBlock> createHangingSign(String name, WoodType woodType, BlockBehaviour.Properties properties) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new CeilingHangingSignBlock(woodType, properties));
    }

    private static Supplier<WallHangingSignBlock> createWoodWallHangingSign(String name, WoodType woodType) {
        return createWallHangingSign(name, woodType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE));
    }

    private static Supplier<WallHangingSignBlock> createWallHangingSign(String name, WoodType woodType, BlockBehaviour.Properties properties) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new WallHangingSignBlock(woodType, properties));
    }

    private static Supplier<SaplingBlock> createSapling(String name, TreeGrower treeGrower) {
        return createBlock(name.toLowerCase(Locale.ROOT), () -> new SaplingBlock(treeGrower, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
    }

    private static <T extends Block> Supplier<T> createBlock(String name, Supplier<T> block) {
        Supplier<T> toReturn = BLOCKS.register(name.toLowerCase(Locale.ROOT), block);
        createBlockItem(name.toLowerCase(Locale.ROOT), toReturn);
        return toReturn;
    }

    private static <T extends Block> Supplier<Item> createBlockItem(String name, Supplier<T> block) {
        return BLOCK_ITEMS.register(name.toLowerCase(Locale.ROOT), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static Boolean ocelotOrParrot(BlockState p_50822_, BlockGetter p_50823_, BlockPos p_50824_, EntityType<?> p_50825_) {
        return (boolean)(p_50825_ == EntityType.OCELOT || p_50825_ == EntityType.PARROT);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ITEMS.register(eventBus);
    }
}
