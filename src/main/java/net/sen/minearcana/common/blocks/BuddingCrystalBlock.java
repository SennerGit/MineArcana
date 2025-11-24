package net.sen.minearcana.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.ArrayList;
import java.util.List;

public class BuddingCrystalBlock extends Block {
    public static final MapCodec<BuddingCrystalBlock> CODEC = simpleCodec(BuddingCrystalBlock::new);
    public static final int GROWTH_CHANCE = 5;
    private static final Direction[] DIRECTIONS = Direction.values();
    private final List<Block> crystal = new ArrayList<>();

    public BuddingCrystalBlock(Properties properties) {
        super(properties
                .noLootTable()
                .mapColor(MapColor.COLOR_PURPLE)
                .forceSolidOn()
                .noOcclusion()
                .sound(SoundType.AMETHYST_CLUSTER)
                .strength(1.5F)
                .lightLevel(p_152632_ -> 5)
                .pushReaction(PushReaction.DESTROY));
    }
    public BuddingCrystalBlock(Properties properties, Block... crystalStages) {
        this(properties
                .noLootTable()
                .mapColor(MapColor.COLOR_PURPLE)
                .forceSolidOn()
                .noOcclusion()
                .sound(SoundType.AMETHYST_CLUSTER)
                .strength(1.5F)
                .lightLevel(p_152632_ -> 5)
                .pushReaction(PushReaction.DESTROY));
        this.crystal.addAll(List.of(crystalStages));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(GROWTH_CHANCE) == 0) {
            Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            BlockPos targetPos = pos.relative(direction);
            BlockState targetState = level.getBlockState(targetPos);

            Block nextStage = null;

            // Stage 0: place small bud
            if (canClusterGrowAtState(targetState)) {
                nextStage = crystal.get(0);
            }
            // Stage 1 → N-1: upgrade bud
            else {
                for (int i = 1; i < crystal.size(); i++) {
                    Block prev = crystal.get(i - 1);
                    Block next = crystal.get(i);

                    if (targetState.is(prev) &&
                            targetState.getValue(AmethystClusterBlock.FACING) == direction) {
                        nextStage = next;
                        break;
                    }
                }
            }

            // Place new or upgraded crystal
            if (nextStage != null) {
                boolean waterlogged = targetState.getFluidState().getType() == Fluids.WATER;

                BlockState newState = nextStage.defaultBlockState()
                        .setValue(AmethystClusterBlock.FACING, direction)
                        .setValue(AmethystClusterBlock.WATERLOGGED, waterlogged);

                level.setBlockAndUpdate(targetPos, newState);
            }
        }
    }

    public static boolean canClusterGrowAtState(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8;
    }
}
