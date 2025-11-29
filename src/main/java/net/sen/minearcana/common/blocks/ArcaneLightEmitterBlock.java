package net.sen.minearcana.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.sen.minearcana.common.blocks.entities.ArcaneLightEmitterBlockEntity;
import net.sen.minearcana.common.registries.MineArcanaBlockEntites;
import org.jetbrains.annotations.Nullable;

public class ArcaneLightEmitterBlock extends BaseEntityBlock {
    public static final MapCodec<ArcaneLightEmitterBlock> CODEC = simpleCodec(ArcaneLightEmitterBlock::new);

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public ArcaneLightEmitterBlock() {
        this(BlockBehaviour.Properties.of());
    }

    public ArcaneLightEmitterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    /**
     * Called when neighbor changes -> update POWERED and call the BE emit method on both sides.
     * Note: emit(...) in the BE takes care of client/server behavior.
     */
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                Block block, BlockPos fromPos, boolean moving) {

        int power = level.getBestNeighborSignal(pos);
        boolean currentlyPowered = state.getValue(POWERED);
        boolean shouldBePowered = power > 0;

        if (currentlyPowered != shouldBePowered) {
            // update the blockstate (use flags = 3 to notify clients)
            level.setBlock(pos, state.setValue(POWERED, shouldBePowered), 3);
        }

        // Always call emit when the block becomes powered so client visuals can trigger as well.
        if (shouldBePowered) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ArcaneLightEmitterBlockEntity emitter) {
                emitter.emit(power);
            }
        }
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ArcaneLightEmitterBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {

        // we only tick server-side here for server logic; client rendering is handled in client tick/render events
        return level.isClientSide ? null :
                createTickerHelper(type, MineArcanaBlockEntites.ARCANA_LIGHT_EMITTER.get(),
                        ArcaneLightEmitterBlockEntity::serverTicker);
    }
}
