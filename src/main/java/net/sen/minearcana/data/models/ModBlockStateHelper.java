package net.sen.minearcana.data.models;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.utils.ModUtils;

import java.util.Locale;
import java.util.function.Supplier;

public abstract class ModBlockStateHelper extends BlockStateProvider {
    public ModBlockStateHelper(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ModUtils.getModId(), exFileHelper);
    }

    protected String name(Block block) {
        return key(block).getPath();
    }

    protected ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
        //        return Registries.BLOCK.registryKey().getKey(block);
    }

    protected void blockItem(Supplier<?> blockRegistryObject) {
        simpleBlockItem((Block) blockRegistryObject.get(), new ModelFile.UncheckedModelFile(ModUtils.getModPath("block/" + ModUtils.getBlockId((Block) blockRegistryObject.get()))));
    }

    protected void simpleBlock(Supplier<Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get());
    }

    protected void flatBlock(Supplier<Block> blockSupplier) {
        Block block = blockSupplier.get();
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();

        simpleBlock(block,
                models().singleTexture(
                        name,
                        ResourceLocation.fromNamespaceAndPath("minecraft", "block/carpet"),
                        "wool",
                        ResourceLocation.fromNamespaceAndPath(MineArcana.MODID, "block/" + name)
                )
        );
    }

    protected void blockItem(Supplier<?> blockRegistryObject, String appendix) {
        simpleBlockItem((Block) blockRegistryObject.get(), new ModelFile.UncheckedModelFile(ModUtils.getModPath("block/" + ModUtils.getBlockId((Block) blockRegistryObject.get()) + appendix)));
    }

    protected void blockWithItem(Supplier<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    protected void blockCrystalCluster(Supplier<Block> blockSupplier) {
        Block block = blockSupplier.get();
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();

        simpleBlockWithItem(block,
                models().singleTexture(
                        name,
                        ResourceLocation.fromNamespaceAndPath("minecraft", "block/cross"),
                        "cross",
                        ResourceLocation.fromNamespaceAndPath(MineArcana.MODID, "block/" + name)
                )
        );
    }

    protected void colouredBlockWithItem(DeferredBlock<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().singleTexture(BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath(),
                        ModUtils.getMinecraftPath("block/cube_all"),
                        "all", blockTexture(blockRegistryObject.get())).renderType("solid"));
    }

    protected void makeCarpet(DeferredBlock<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().carpet(BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath(),
                        blockTexture(blockRegistryObject.get()))
        );
    }

    protected void leavesBlock(Supplier<LeavesBlock> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().cubeAll(BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    protected void saplingBlock(Supplier<SaplingBlock> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(),
                models().cross(BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    protected void hangingSignBlock(Block signBlock, Block wallSignBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(name(signBlock), texture);
        hangingSignBlock(signBlock, wallSignBlock, sign);
    }

    protected void hangingSignBlock(Block signBlock, Block wallSignBlock, ModelFile sign) {
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }

    protected void makeFlower(DeferredBlock<Block> flowerBlock, DeferredBlock<FlowerPotBlock> potBlock) {
        simpleBlock(flowerBlock.get(),
                models().cross(blockTexture(flowerBlock.get()).getPath(),
                        blockTexture(flowerBlock.get())).renderType("cutout"));
        simpleBlock(potBlock.get(),
                models().withExistingParent(potBlock.getId().getPath(),
                                mcLoc("block/flower_pot_cross")).renderType("cutout")
                        .texture("plant", blockTexture(flowerBlock.get())));
    }

    protected void makeVegetation(DeferredBlock<Block> vegBlock) {
        simpleBlock(vegBlock.get(),
                models().cross(blockTexture(vegBlock.get()).getPath(),
                        blockTexture(vegBlock.get())).renderType("cutout"));
    }

    protected void makeMushroom(DeferredBlock<Block> mushroomBlock, DeferredBlock<FlowerPotBlock> potBlock) {
        simpleBlock(mushroomBlock.get(),
                models().cross(BuiltInRegistries.BLOCK.getKey(mushroomBlock.get()).getPath(),
                        blockTexture(mushroomBlock.get())).renderType("cutout"));
        simpleBlock(potBlock.get(),
                models().withExistingParent(potBlock.getId().getPath(),
                                mcLoc("block/flower_pot_cross")).renderType("cutout")
                        .texture("plant", blockTexture(mushroomBlock.get())));
    }

    protected void makeMushroom(DeferredBlock<Block> mushroomBlock) {
        simpleBlock(mushroomBlock.get(),
                models().cross(blockTexture(mushroomBlock.get()).getPath(),
                        blockTexture(mushroomBlock.get())).renderType("cutout"));
    }

    protected void makeVine(DeferredBlock<Block> vineBlock) {
        simpleBlock(vineBlock.get(),
                models().withExistingParent(vineBlock.getId().getPath(),
                                mcLoc("block/vine")).renderType("vine")
                        .texture("vine", blockTexture(vineBlock.get()))
        );
    }

    protected ResourceLocation modBlockResourceLocation(DeferredBlock<Block> block) {
        return modBlockResourceLocation(block.getId().getPath());
    }

    protected ResourceLocation modBlockResourceLocation(DeferredBlock<Block> block, String addedData) {
        return modBlockResourceLocation(block.getId().getPath() + addedData);
    }

    protected ResourceLocation modBlockResourceLocation(String block) {
        ResourceLocation resourceLocation;
        resourceLocation = ModUtils.getModPath("block/" + block);
        return resourceLocation;
    }

    /*
     * ChaosAwakens
     * https://github.com/ChaosAwakens/ChaosAwakens/tree/6bb21a2e15361e3aa3a15ebc1d427e5f746019cd
     */

    protected void grassBlock(DeferredBlock<Block> block, ResourceLocation particle, ResourceLocation bottom, ResourceLocation top, ResourceLocation side, ResourceLocation overlay) {
        grassBlock(block.get(), models().getExistingFile(modBlockResourceLocation(block.getId().getPath())));
    }

    protected void grassBlock(Block block, ModelFile model) {
        grassBlock(block, new ConfiguredModel(model));
    }

    protected void grassBlock(Block block, ConfiguredModel... model) {
        getVariantBuilder(block).partialState().addModels(model);
    }

    /**
     * This function simplifies the generation of block states for wood blocks.
     * @param log The log block.
     * @param wood The wood block.
     * @param strippedLog The stripped log block.
     * @param strippedWood The stripped wood block.
     * @param planks The planks block.
     * @param planksStairs The stair block.
     * @param planksSlab The slab block.
     * @param planksPressurePlate The pressureplate block.
     * @param planksFence The fence block.
     * @param planksFenceGate The fence gate block.
     * @param planksDoor The door block.
     * @param planksTrapDoor The trap door block.
     * @param planksButton The button block.
     * @param planksSapling The sapling block.
     * @param planksLeaves The leaves block.
     * @param planksSign The sign block.
     * @param planksWallSign The wall sign block.
     * @param planksHangingSign The hanging sign block.
     * @param planksHangingWallSign The hanging wall sign block.
     */
    protected void woodBlockGroup(Supplier<RotatedPillarBlock> log, Supplier<RotatedPillarBlock> wood, Supplier<RotatedPillarBlock> strippedLog,
                                  Supplier<RotatedPillarBlock> strippedWood, Supplier<Block> planks, Supplier<StairBlock> planksStairs,
                                  Supplier<SlabBlock> planksSlab, Supplier<PressurePlateBlock> planksPressurePlate, Supplier<FenceBlock> planksFence,
                                  Supplier<FenceGateBlock> planksFenceGate, Supplier<DoorBlock> planksDoor, Supplier<TrapDoorBlock> planksTrapDoor,
                                  Supplier<ButtonBlock> planksButton, Supplier<LeavesBlock> planksLeaves,
                                  Supplier<StandingSignBlock> planksSign, Supplier<WallSignBlock> planksWallSign,
                                  Supplier<CeilingHangingSignBlock> planksHangingSign, Supplier<WallHangingSignBlock> planksHangingWallSign, Supplier<SaplingBlock> planksSapling) {
        woodBlockGroup(
                log, wood, strippedLog, strippedWood, planks, planksStairs, planksSlab, planksPressurePlate, planksFence, planksFenceGate,
                planksDoor, planksTrapDoor, planksButton, planksLeaves, planksSign, planksWallSign, planksHangingSign, planksHangingWallSign
                );
        saplingBlock(planksSapling);
    }

    protected void woodBlockGroup(Supplier<RotatedPillarBlock> log, Supplier<RotatedPillarBlock> wood, Supplier<RotatedPillarBlock> strippedLog,
                               Supplier<RotatedPillarBlock> strippedWood, Supplier<Block> planks, Supplier<StairBlock> planksStairs,
                               Supplier<SlabBlock> planksSlab, Supplier<PressurePlateBlock> planksPressurePlate, Supplier<FenceBlock> planksFence,
                               Supplier<FenceGateBlock> planksFenceGate, Supplier<DoorBlock> planksDoor, Supplier<TrapDoorBlock> planksTrapDoor,
                               Supplier<ButtonBlock> planksButton, Supplier<LeavesBlock> planksLeaves,
                               Supplier<StandingSignBlock> planksSign, Supplier<WallSignBlock> planksWallSign,
                               Supplier<CeilingHangingSignBlock> planksHangingSign, Supplier<WallHangingSignBlock> planksHangingWallSign) {

        axisBlock(log.get(), blockTexture(log.get()), ResourceLocation.fromNamespaceAndPath(blockTexture(log.get()).getNamespace(), blockTexture(log.get()).getPath() + "_top"));
        axisBlock(wood.get(), blockTexture(log.get()), blockTexture(log.get()));
        axisBlock(strippedLog.get(), blockTexture(strippedLog.get()), ResourceLocation.fromNamespaceAndPath(blockTexture(strippedLog.get()).getNamespace(), blockTexture(log.get()).getPath() + "_top"));
        axisBlock(strippedWood.get(), blockTexture(strippedLog.get()), blockTexture(strippedLog.get()));

        blockItem(log);
        blockItem(wood);
        blockItem(strippedLog);
        blockItem(strippedWood);
        blockWithItem(planks);

        stairsBlock(planksStairs.get(), blockTexture(planks.get()));
        slabBlock((planksSlab.get()), blockTexture(planks.get()), blockTexture(planks.get()));
        pressurePlateBlock(planksPressurePlate.get(), blockTexture(planks.get()));
        fenceBlock(planksFence.get(), blockTexture(planks.get()));
        fenceGateBlock(planksFenceGate.get(), blockTexture(planks.get()));
        doorBlockWithRenderType(planksDoor.get(), modLoc("block/" + ModUtils.getBlockId(planksDoor.get()) + "_bottom"), modLoc("block/" + ModUtils.getBlockId(planksDoor.get()) + "_top"), "cutout");
        trapdoorBlockWithRenderType(planksTrapDoor.get(), modLoc("block/" + ModUtils.getBlockId(planksTrapDoor.get())), true, "cutout");
        buttonBlock(planksButton.get(), blockTexture(planks.get()));

        blockItem(planksStairs);
        blockItem(planksSlab);
        blockItem(planksFenceGate);
        blockItem(planksPressurePlate);
        blockItem(planksTrapDoor, "_bottom");
        leavesBlock(planksLeaves);
        signBlock(((StandingSignBlock) planksSign.get()),((WallSignBlock) planksWallSign.get()),
                blockTexture(planks.get()));
        hangingSignBlock(planksHangingSign.get(), planksHangingWallSign.get(),
                blockTexture(planks.get()));
    }

//    /**
//     * This function simplifies the generation of block states for stone blocks.
//     * @param stone The stone block.
//     * @param cobblestone The cobblestone block.
//     * @param stoneBricks The stone bricks block.
//     * @param stoneSmooth The smooth stone block.
//     * @param cobblestoneMossy The mossy cobblestone block.
//     * @param stoneBricksMossy The mossy stone bricks block.
//     * @param stoneBricksCracked The cracked stone bricks block.
//     * @param stoneBricksChiseled The chiseled stone bricks block.
//     * @param stoneStairs The stone stairs block.
//     * @param cobblestoneStairs The cobblestone stairs block.
//     * @param stoneBricksStairs The stone bricks stairs block.
//     * @param stoneSlabs The stone slab block.
//     * @param cobblestoneSlabs The cobblestone slab block.
//     * @param stoneBricksSlabs The stone bricks block.
//     * @param stoneWall The stone wall block.
//     * @param cobblestoneWall The cobblestone wall block.
//     * @param stoneBricksWall The stone bricks wall block.
//     * @param stonePressurePlate The stone pressureplate block.
//     * @param stoneButton The stone button block.
//     */
//    protected void StoneBlockGroup(DeferredBlock<Block> stone, DeferredBlock<Block> cobblestone, DeferredBlock<Block> stoneBricks,
//                                DeferredBlock<Block> stoneSmooth, DeferredBlock<Block> cobblestoneMossy, DeferredBlock<Block> stoneBricksMossy,
//                                DeferredBlock<Block> stoneBricksCracked, DeferredBlock<Block> stoneBricksChiseled,
//                                DeferredBlock<StairBlock> stoneStairs, DeferredBlock<StairBlock> cobblestoneStairs, DeferredBlock<StairBlock> stoneBricksStairs,
//                                DeferredBlock<SlabBlock> stoneSlabs, DeferredBlock<SlabBlock> cobblestoneSlabs, DeferredBlock<SlabBlock> stoneBricksSlabs,
//                                DeferredBlock<WallBlock> stoneWall, DeferredBlock<WallBlock> cobblestoneWall, DeferredBlock<WallBlock> stoneBricksWall,
//                                DeferredBlock<PressurePlateBlock> stonePressurePlate, DeferredBlock<ButtonBlock> stoneButton) {
//
//        blockWithItem(stone);
//        blockWithItem(cobblestone);
//        blockWithItem(stoneBricks);
//        blockWithItem(stoneSmooth);
//        blockWithItem(cobblestoneMossy);
//        blockWithItem(stoneBricksMossy);
//        blockWithItem(stoneBricksCracked);
//        blockWithItem(stoneBricksChiseled);
//
//        stairsBlock((StairBlock) stoneStairs.get(), blockTexture(stone.get()));
//        stairsBlock((StairBlock) cobblestoneStairs.get(), blockTexture(cobblestone.get()));
//        stairsBlock((StairBlock) stoneBricksStairs.get(), blockTexture(stoneBricks.get()));
//        slabBlock(((SlabBlock) stoneSlabs.get()), blockTexture(stone.get()), blockTexture(stone.get()));
//        slabBlock(((SlabBlock) cobblestoneSlabs.get()), blockTexture(cobblestone.get()), blockTexture(cobblestone.get()));
//        slabBlock(((SlabBlock) stoneBricksSlabs.get()), blockTexture(stoneBricks.get()), blockTexture(stoneBricks.get()));
//        wallBlock((WallBlock) stoneWall.get(), blockTexture(stone.get()));
//        wallBlock((WallBlock) cobblestoneWall.get(), blockTexture(stone.get()));
//        wallBlock((WallBlock) stoneBricksWall.get(), blockTexture(stone.get()));
//        pressurePlateBlock((PressurePlateBlock) stonePressurePlate.get(), blockTexture(stone.get()));
//        buttonBlock((ButtonBlock) stoneButton.get(), blockTexture(stone.get()));
//
//        blockItem(stoneStairs);
//        blockItem(stoneSlabs);
//        blockItem(stonePressurePlate);
////        blockItem(stoneButton);
////        blockItem(stoneWall);
//        blockItem(cobblestoneStairs);
//        blockItem(cobblestoneSlabs);
////        blockItem(cobblestoneWall);
//        blockItem(stoneBricksStairs);
//        blockItem(stoneBricksSlabs);
////        blockItem(stoneBricksWall);
//    }

    protected void makePortalBlock(Supplier<Block> block, EnumProperty<Direction.Axis> AXIS) {
        String path = BuiltInRegistries.BLOCK.getKey(block.get()).getPath().toLowerCase(Locale.ROOT);
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(ModUtils.getModId(), "block/" + path);
        ResourceLocation loc_ew = ResourceLocation.fromNamespaceAndPath(ModUtils.getModId(), "block/" + path + "_ew");
        ResourceLocation loc_ns = ResourceLocation.fromNamespaceAndPath(ModUtils.getModId(), "block/" + path + "_ns");

        BlockModelBuilder model_ew = models()
                .getBuilder(loc_ew.toString())
                .texture("particle", loc)
                .texture("portal", loc)

                .element()
                .from(6, 0, 0)
                .to(10, 16, 16)
                .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#portal").end()
                .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#portal").end()
                .end();

        BlockModelBuilder model_ns = models()
                .getBuilder(loc_ns.toString())
                .texture("particle", loc)
                .texture("portal", loc)

                .element()
                .from(0, 0, 6)
                .to(16, 16, 10)
                .face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#portal").end()
                .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#portal").end()
                .end();

        getVariantBuilder(block.get())
                .forAllStates(state -> {
                    switch (state.getValue(AXIS)) {
                        case X -> {
                            return ConfiguredModel.builder().modelFile(model_ns).build();
                        }

                        case Z -> {
                            return ConfiguredModel.builder().modelFile(model_ew).build();
                        }
                    }

                    return null;
                });
    }
}