package net.sen.minearcana.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.sen.minearcana.common.screens.WandCraftingStationMenu;
import org.jetbrains.annotations.Nullable;

public class WandCraftingStationBlock extends Block {

    public static final MapCodec<WandCraftingStationBlock> CODEC =
            simpleCodec(WandCraftingStationBlock::new);

    private static final Component CONTAINER_TITLE =
            Component.translatable("container.minearcana.wand_crafting_station");

    // ---------------------------
    // Constructor
    // ---------------------------
    public WandCraftingStationBlock(Properties properties) {
        super(properties);
    }

    public WandCraftingStationBlock() {
        this(BlockBehaviour.Properties.of());
    }

    @Override
    protected MapCodec<? extends WandCraftingStationBlock> codec() {
        return CODEC;
    }

    // ---------------------------
    // Interaction Logic
    // ---------------------------
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        MenuProvider provider = this.getMenuProvider(state, level, pos);
        if (provider != null) {
            player.openMenu(provider);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
                (windowId, inv, player) ->
                        new WandCraftingStationMenu(windowId, inv, ContainerLevelAccess.create(level, pos)),
                CONTAINER_TITLE
        );
    }
}
