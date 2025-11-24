package net.sen.minearcana.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sen.minearcana.common.blocks.entities.BloodMarkerBlockEntity;
import org.jetbrains.annotations.Nullable;

public class BloodMarkerBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    public static final MapCodec<BloodMarkerBlock> CODEC = simpleCodec(BloodMarkerBlock::new);
    protected static final Properties properties = BlockBehaviour.Properties.of().noOcclusion().noCollission().noLootTable().instabreak();

    public BloodMarkerBlock() {
        this(properties);
    }

    public BloodMarkerBlock(Properties properties) {
        super(BlockBehaviour.Properties.of().noCollission().noLootTable());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    /*
    BlockEntity
     */
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BloodMarkerBlockEntity(pos, state);
    }
}