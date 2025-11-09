package net.sen.minearcana.data.loottable;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.sen.minearcana.common.utils.ModUtils;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public abstract class BlockLootTableHelper extends BlockLootSubProvider {
    public BlockLootTableHelper(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    protected void dropSelf(Supplier<? extends Block> block) {
        super.dropSelf(block.get());
    }

    protected void dropFlower(Supplier<? extends Block> flower, Supplier<? extends Block> pots) {
        this.dropSelf(flower);
        this.pots(pots);
    }

    protected void slab(Supplier<? extends Block> slab) {
        this.add(slab.get(), this::createSlabItemTable);
    }

    protected void leaves(Supplier<? extends Block> leaves) {
        this.add(leaves.get(), block -> createLeavesDrops(block, leaves.get(), NORMAL_LEAVES_SAPLING_CHANCES));
    }

    protected void door(Supplier<? extends Block> door) {
        this.add(door.get(), this::createDoorTable);
    }

    protected void sign(Supplier<? extends Block> block_sign, Supplier<? extends Block> block_sign_wall, Supplier<? extends Item> item_sign) {
        this.add(block_sign.get(), block -> createSingleItemTable(item_sign.get()));
        this.add(block_sign_wall.get(), block -> createSingleItemTable(item_sign.get()));
    }

    protected void wall_sign(Supplier<? extends Block> block_hanging_sign, Supplier<? extends Block> block_hanging_sign_wall, Supplier<? extends Item> item_hanging_sign) {
        this.add(block_hanging_sign.get(), block -> createSingleItemTable(item_hanging_sign.get()));
        this.add(block_hanging_sign_wall.get(), block -> createSingleItemTable(item_hanging_sign.get()));
    }

    protected void crops(Supplier<? extends Block> crop_block, Supplier<? extends Item> fruit, Supplier<? extends Item> seeds, Property<Integer> property, int age) {
        LootItemCondition.Builder loot_item_condition$builder1 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(crop_block.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, age));
        this.add(crop_block.get(), this.createCropDrops(crop_block.get(), fruit.get(), seeds.get(), loot_item_condition$builder1));

    }

    protected void bulbCrops(Supplier<? extends Block> crop_block, Supplier<? extends Item> fruit, Property<Integer> property, int age) {
        LootItemCondition.Builder loot_item_condition$builder1 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(crop_block.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, age));
        this.add(crop_block.get(), this.createBulbCropDrops(crop_block.get(), fruit.get(), loot_item_condition$builder1));

    }

    protected void wildCrops(Supplier<? extends Block> crop_block, Supplier<? extends Item> wildFruit, float chance, Supplier<? extends Item> fruit, Property<Integer> property, int age) {
        LootItemCondition.Builder loot_item_condition$builder1 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(crop_block.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, age));
        this.add(crop_block.get(), this.createWildCropDrops(crop_block.get(), wildFruit.get(), chance, fruit.get(), loot_item_condition$builder1));

    }

    protected LootTable.Builder createWildCropDrops(Block pCropBlock, Item pGrownWildCropItem, float chance, Item pGrownCropItem, LootItemCondition.Builder pDropGrownCropCondition) {
//        return this.applyExplosionDecay(
//                pCropBlock,
//                LootTable
//                        .lootTable().withPool(
//                                LootPool.lootPool().setRolls()
//                                        .add(
//                                                LootItem.lootTableItem(pGrownCropItem)
//                                                        .when(pDropGrownCropCondition)
//                                                        .otherwise(
//                                                                LootItem.lootTableItem(pGrownWildCropItem)
//                                                        )
//                                        )
//                        )
//        );

//        this.add(Blocks.POTATOES,
        return this.applyExplosionDecay(
                pCropBlock,
                LootTable.lootTable().withPool(
                                LootPool.lootPool()
                                        .add(
                                                LootItem.lootTableItem(pGrownWildCropItem)
                                        )
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .when(
                                                pDropGrownCropCondition
                                        )
                                        .add(
                                                LootItem.lootTableItem(pGrownWildCropItem)
                                                        .apply(
                                                                ApplyBonusCount.addBonusBinomialDistributionCount(
                                                                        this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE),
                                                                        0.5714286F,
                                                                        3
                                                                )
                                                        )
                                        )
                        ).withPool(
                                LootPool.lootPool()
                                        .when(pDropGrownCropCondition)
                                        .add(
                                                LootItem.lootTableItem(pGrownCropItem)
                                                        .when(LootItemRandomChanceCondition.randomChance(chance)
                                                        )
                                        )
                        )
        );

    }


    protected LootTable.Builder createBulbCropDrops(Block pCropBlock, Item pGrownCropItem, LootItemCondition.Builder pDropGrownCropCondition) {
        return this.applyExplosionDecay(
                pCropBlock,
                LootTable.lootTable().withPool(
                                LootPool.lootPool()
                                        .add(
                                                LootItem.lootTableItem(pGrownCropItem)
                                        )
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .when(
                                                pDropGrownCropCondition
                                        )
                                        .add(
                                                LootItem.lootTableItem(pGrownCropItem)
                                                        .apply(
                                                                ApplyBonusCount.addBonusBinomialDistributionCount(
                                                                        this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE),
                                                                        0.5714286F,
                                                                        3
                                                                )
                                                        )
                                        )
                        )
        );
    }

    protected void grassDrop(Supplier<Block> grassBlock) {
        this.add(grassBlock.get(), BlockLootSubProvider::createShearsOnlyDrop);
    }

//    protected static LootTable.Builder tallGrassDrop(Block originalBlock, Block newBlock) {
//        LootPoolEntryContainer.Builder<?> builder = LootItem.lootTableItem(newBlock).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))).when(SHEARS);
//        return LootTable.lootTable().withPool(LootPool.lootPool().add(builder).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(originalBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER))).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(originalBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER).build()).build()), new BlockPos(0, 1, 0)))).withPool(LootPool.lootPool().add(builder).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(originalBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER))).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(originalBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).build()).build()), new BlockPos(0, -1, 0))));
//    }

    protected void dropOther(Supplier<? extends Block> brokenBlock, ItemLike droppedBlock) {
        super.dropOther(brokenBlock.get(), droppedBlock);
    }

    protected void dropOther(Supplier<? extends Block> brokenBlock, Supplier<? extends ItemLike> droppedBlock) {
        super.dropOther(brokenBlock.get(), droppedBlock.get());
    }

    protected void dropAsSilk(Supplier<? extends Block> block) {
        super.dropWhenSilkTouch(block.get());
    }

    protected void dropWithSilk(Supplier<? extends Block> block, Supplier<? extends ItemLike> drop) {
        add(block.get(), (result) -> createSingleItemTableWithSilkTouch(result, drop.get()));
    }

    protected void ore(Supplier<? extends Block> block, Supplier<? extends Item> drop) {
        super.add(block.get(), (result) -> createOreDrop(result, drop.get()));
    }

    protected void ore(Supplier<? extends Block> block, Item drop) {
        super.add(block.get(), (result) -> createOreDrop(result, drop));
    }

    protected void stone(Supplier<? extends Block> stone, Supplier<? extends Block> cobblestone) {
        this.add(stone.get(), block -> createSingleItemTableWithSilkTouch(stone.get(), cobblestone.get()));
    }

    protected void pots(Supplier<? extends Block> pottedFlower) {
        this.add(pottedFlower.get(), createPotFlowerItemTable(pottedFlower.get()));
    }

    protected void clusterOre(Supplier<? extends Block> block, Item drop, float minDrop, float maxDrop) {
        this.add(block.get(), (ore) -> createSilkTouchDispatchTable(ore, applyExplosionDecay(ore, LootItem.lootTableItem(drop).apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrop, maxDrop))).apply(ApplyBonusCount.addOreBonusCount(this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE))))));
    }

    protected void clusterOre(Supplier<? extends Block> block, Supplier<? extends Item> drop, float minDrop, float maxDrop) {
        this.add(block.get(), (ore) -> createSilkTouchDispatchTable(ore, applyExplosionDecay(ore, LootItem.lootTableItem(drop.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrop, maxDrop))).apply(ApplyBonusCount.addOreBonusCount(this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE))))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.entrySet().stream().filter(e -> e.getKey().location().getNamespace().equals(ModUtils.getModId())).map(Map.Entry::getValue).toList();
    }
}
