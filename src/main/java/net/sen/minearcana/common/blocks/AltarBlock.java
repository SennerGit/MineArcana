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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sen.minearcana.common.blocks.entities.AltarBlockEntity;
import net.sen.minearcana.common.registries.MineArcanaBlockEntites;
import net.sen.minearcana.common.utils.altar.AltarType;
import org.jetbrains.annotations.Nullable;

public class AltarBlock extends BaseEntityBlock {
    public static final MapCodec<ArcanaCauldronBlock> CODEC = simpleCodec(ArcanaCauldronBlock::new);
    public static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public AltarBlock() {
        super(BlockBehaviour.Properties.of().strength(2.0f).noOcclusion());
    }

    @Override
    public MapCodec<ArcanaCauldronBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    //---Block Entity Logic Here---//
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AltarBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createAltarTicker(level, blockEntityType, MineArcanaBlockEntites.ALTAR_BLOCK.get());
    }

    @javax.annotation.Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createAltarTicker(Level level, BlockEntityType<T> serverType, BlockEntityType<? extends AltarBlockEntity> clientType) {
        return level.isClientSide ? null : createTickerHelper(serverType, clientType, AltarBlockEntity::serverTicker);
    }
}
