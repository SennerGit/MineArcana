package net.sen.minearcana.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sen.minearcana.common.blocks.entities.AspectPipeBlockEntity;
import net.sen.minearcana.common.registries.MineArcanaBlockEntites;
import org.jetbrains.annotations.Nullable;

public class AspectPipeBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    public static final MapCodec<AspectPipeBlock> CODEC = simpleCodec(AspectPipeBlock::new);

    public AspectPipeBlock() {
        super(Properties.of());
    }

    public AspectPipeBlock(Properties properties) {
        super(Properties.of());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    //---Pipe Logic Here---//
    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AspectPipeBlockEntity aspectPipeBlockEntity) {
                aspectPipeBlockEntity.onNetworkChanged();
            }
        }
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);

        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof AspectPipeBlockEntity pipe) {
                pipe.onNetworkChanged();
            }
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof AspectPipeBlockEntity pipe) {
                pipe.onNetworkChanged();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    //---Block Entity Logic Here---//
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AspectPipeBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createAspectPipeTicker(level, blockEntityType, MineArcanaBlockEntites.ASPECT_PIPE.get());
    }

    @javax.annotation.Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createAspectPipeTicker(Level level, BlockEntityType<T> serverType, BlockEntityType<? extends AspectPipeBlockEntity> clientType) {
        return level.isClientSide ? null : createTickerHelper(serverType, clientType, AspectPipeBlockEntity::serverTicker);
    }
}
