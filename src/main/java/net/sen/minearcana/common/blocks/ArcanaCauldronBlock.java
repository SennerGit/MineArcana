package net.sen.minearcana.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.sen.minearcana.common.blocks.entities.ArcanaCauldronBlockEntity;
import net.sen.minearcana.common.registries.MineArcanaBlockEntites;
import org.jetbrains.annotations.Nullable;

public class ArcanaCauldronBlock extends BaseEntityBlock {
    public static final MapCodec<ArcanaCauldronBlock> CODEC = simpleCodec(ArcanaCauldronBlock::new);
    private static final int SIDE_THICKNESS = 2;
    private static final int LEG_WIDTH = 4;
    private static final int LEG_HEIGHT = 3;
    private static final int LEG_DEPTH = 2;
    protected static final int FLOOR_LEVEL = 4;
    private static final VoxelShape INSIDE = box(2.0, 4.0, 2.0, 14.0, 16.0, 14.0);
    public static final VoxelShape SHAPE = Shapes.join(
            Shapes.block(),
            Shapes.or(box(0.0, 0.0, 4.0, 16.0, 3.0, 12.0), box(4.0, 0.0, 0.0, 12.0, 3.0, 16.0), box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0), INSIDE),
            BooleanOp.ONLY_FIRST
    );

    public ArcanaCauldronBlock() {
        this(BlockBehaviour.Properties.of());
    }

    public ArcanaCauldronBlock(Properties properties) {
        super(BlockBehaviour.Properties.of().noOcclusion());
    }

    @Override
    public MapCodec<ArcanaCauldronBlock> codec() {
        return CODEC;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof ArcanaCauldronBlockEntity cauldronBE)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // Handle filling the cauldron with a fluid bucket
        if (stack.getItem() instanceof BucketItem bucket) {
            Fluid fluidInBucket = bucket.content;
            if (cauldronBE.addWater(new FluidStack(fluidInBucket, FluidType.BUCKET_VOLUME))) {
                if (!player.isCreative()) {
                    stack.shrink(1);
                    player.addItem(new ItemStack(Items.BUCKET));
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        // Handle emptying the cauldron into a bucket
        if (stack.getItem() == Items.BUCKET) {
            FluidStack fluidInCauldron = cauldronBE.getFluid();
            if (cauldronBE.removeWater(new FluidStack(fluidInCauldron.getFluid(), FluidType.BUCKET_VOLUME))) {
                if (!player.isCreative()) {
                    stack.shrink(1);
                    player.addItem(new ItemStack(fluidInCauldron.getFluid().getBucket()));
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }


        cauldronBE.findMatchingRecipe(stack).ifPresent(recipe -> {
            if (stack.getItem() == recipe.getInputItem()) {
                    ItemStack potion = cauldronBE.extractPotion();
                    if (!potion.isEmpty()) {
                        if (!player.isCreative()) stack.shrink(1);
                        if (!player.addItem(potion)) player.drop(potion, false);
                    }
            }
        });

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide || !(entity instanceof ItemEntity itemEntity)) return;

        // Only trigger if the entity is an item above the block
        if (!(level.getBlockEntity(pos) instanceof ArcanaCauldronBlockEntity cauldronBE)) return;

        ItemStack stack = itemEntity.getItem();
        if (cauldronBE.processItem(stack)) {
            // Remove the item from the world
            itemEntity.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return INSIDE;
    }

    //---Block Entity Logic Here---//
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ArcanaCauldronBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createArcanaCauldronTicker(level, blockEntityType, MineArcanaBlockEntites.ARCANA_CAULDRON.get());
    }

    @javax.annotation.Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createArcanaCauldronTicker(
            Level level, BlockEntityType<T> serverType, BlockEntityType<? extends ArcanaCauldronBlockEntity> clientType
    ) {
        return level.isClientSide ? null : createTickerHelper(serverType, clientType, ArcanaCauldronBlockEntity::serverTick);
    }
}
