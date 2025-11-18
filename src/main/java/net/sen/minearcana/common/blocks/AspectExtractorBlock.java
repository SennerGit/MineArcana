package net.sen.minearcana.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sen.minearcana.common.blocks.entities.AspectExtractorBlockEntity;
import net.sen.minearcana.common.registries.MineArcanaBlockEntites;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class AspectExtractorBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    public static final MapCodec<AspectExtractorBlock> CODEC = simpleCodec(AspectExtractorBlock::new);

    public AspectExtractorBlock() {
        super(BlockBehaviour.Properties.of());
    }

    public AspectExtractorBlock(Properties properties) {
        super(BlockBehaviour.Properties.of());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    //---Block Interaction Logic Here---//
    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof AspectExtractorBlockEntity blockEntity) {
                blockEntity.drops();
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof AspectExtractorBlockEntity blockEntity) {
            if (blockEntity.inventoryHandler.getStackInSlot(0).isEmpty() && !stack.isEmpty()) {
                blockEntity.inventoryHandler.insertItem(0, stack.copy(), false);
                stack.shrink(1);
                return ItemInteractionResult.SUCCESS;
            } else if (stack.isEmpty()) {
                ItemStack itemStack = blockEntity.inventoryHandler.extractItem(0, 1, false);
                player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
                blockEntity.clearContents();
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.SUCCESS;
    }

    //---Block Entity Logic Here---//
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AspectExtractorBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createAspectExtractorTicker(level, blockEntityType, MineArcanaBlockEntites.ASPECT_EXTRACTOR.get());
    }

    @javax.annotation.Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createAspectExtractorTicker(Level level, BlockEntityType<T> serverType, BlockEntityType<? extends AspectExtractorBlockEntity> clientType) {
        return level.isClientSide ? null : createTickerHelper(serverType, clientType, AspectExtractorBlockEntity::serverTicker);
    }
}
