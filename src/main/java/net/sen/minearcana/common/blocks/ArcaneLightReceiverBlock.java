package net.sen.minearcana.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sen.minearcana.common.blocks.entities.ArcaneLightReceiverBlockEntity;
import net.sen.minearcana.common.registries.MineArcanaBlockEntites;
import net.sen.minearcana.common.utils.lightbeams.Beam;
import org.jetbrains.annotations.Nullable;

public class ArcaneLightReceiverBlock extends BaseEntityBlock {
    public static final MapCodec<ArcaneLightReceiverBlock> CODEC = simpleCodec(ArcaneLightReceiverBlock::new);
    protected static final Properties properties = Properties.of();

    // 0–15 redstone power
    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public ArcaneLightReceiverBlock() {
        this(properties);
        registerDefaultState(defaultBlockState().setValue(POWER, 0));
    }

    public ArcaneLightReceiverBlock(Properties properties) {
        super(Properties.of());
    }

    public void receiveBeam(Level level, BlockPos pos, Beam beam) {
        if (level.isClientSide) return;

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof ArcaneLightReceiverBlockEntity receiver) {
            receiver.onBeamHit(beam.strength());
        }
    }


    // Redstone

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
        return state.getValue(POWER);
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
        return state.getValue(POWER);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(POWER);
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
        return new ArcaneLightReceiverBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createArcaneLightReceiverTicker(level, blockEntityType, MineArcanaBlockEntites.ARCANA_LIGHT_RECEIVER.get());
    }

    @javax.annotation.Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createArcaneLightReceiverTicker(Level level, BlockEntityType<T> serverType, BlockEntityType<? extends ArcaneLightReceiverBlockEntity> clientType) {
        return level.isClientSide ? null : createTickerHelper(serverType, clientType, ArcaneLightReceiverBlockEntity::serverTicker);
    }
}
