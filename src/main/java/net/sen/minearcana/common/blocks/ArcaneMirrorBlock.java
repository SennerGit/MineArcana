package net.sen.minearcana.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.sen.minearcana.common.utils.lightbeams.MirrorOrientation;

import static net.sen.minearcana.common.utils.lightbeams.MirrorOrientation.*;

public class ArcaneMirrorBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<MirrorOrientation> ORIENTATION = EnumProperty.create("orientation", MirrorOrientation.class);

    public ArcaneMirrorBlock() {
        super(BlockBehaviour.Properties.of());
        this.registerDefaultState(this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(ORIENTATION, MirrorOrientation.STRAIGHT)
        );
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            MirrorOrientation current = state.getValue(ORIENTATION);
            MirrorOrientation next = switch (current) {
                case STRAIGHT -> MirrorOrientation.DIAGONAL_NE_SW;
                case DIAGONAL_NE_SW -> MirrorOrientation.DIAGONAL_NW_SE;
                default -> MirrorOrientation.STRAIGHT;
            };

            level.setBlock(pos, state.setValue(ORIENTATION, next), 3);
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    public Vec3 reflect(Vec3 incoming, BlockState state) {

        Direction facing = state.getValue(FACING);
        MirrorOrientation type = state.getValue(ORIENTATION);

        // Normal vector of the mirror surface
        Vec3 normal = Vec3.atLowerCornerOf(facing.getNormal()).normalize();

        // For diagonal mirrors, override normal
        if (type == DIAGONAL_NE_SW) {
            // "\" diagonal (NE <-> SW)
            normal = new Vec3(1, 0, -1).normalize();
        }
        else if (type == MirrorOrientation.DIAGONAL_NW_SE) {
            // "/" diagonal (NW <-> SE)
            normal = new Vec3(1, 0, 1).normalize();
        }

        // D - 2 * dot(D,N) * N
        double dot = incoming.dot(normal);
        Vec3 reflection = incoming.subtract(normal.scale(2 * dot));

        return reflection.normalize();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ORIENTATION);
    }
}
