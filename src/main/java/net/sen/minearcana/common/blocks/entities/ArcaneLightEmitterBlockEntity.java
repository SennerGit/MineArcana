package net.sen.minearcana.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.sen.minearcana.common.blocks.ArcaneLightEmitterBlock;
import net.sen.minearcana.common.network.BeamSpawnPacket;
import net.sen.minearcana.common.network.MineArcanaPackets;
import net.sen.minearcana.common.registries.MineArcanaBlockEntites;
import net.sen.minearcana.common.utils.lightbeams.Beam;
import net.sen.minearcana.common.utils.lightbeams.BeamEngine;

public class ArcaneLightEmitterBlockEntity extends BlockEntity {

    public ArcaneLightEmitterBlockEntity(BlockPos pos, BlockState state) {
        super(MineArcanaBlockEntites.ARCANA_LIGHT_EMITTER.get(), pos, state);
    }

    /**
     * Server ticker (no continuous server logic for now). Kept for future expansion.
     */
    public static void serverTicker(Level level, BlockPos pos, BlockState state, ArcaneLightEmitterBlockEntity be) {
        // no-op for now
    }

    /**
     * Emit a beam. This method uses level.isClientSide() to decide whether to add to client engine
     * or server engine. IMPORTANT: do not call Minecraft.getInstance() here.
     */
    public void emit(int powerLevel) {
        if (level == null) return;

        Direction facing = getBlockState().getValue(BlockStateProperties.FACING);
        Vec3 dir = new Vec3(facing.getStepX(), facing.getStepY(), facing.getStepZ());

        Beam beam = new Beam(
                level,
                worldPosition,
                dir,
                powerLevel,
                Math.max(1, powerLevel * 50),
                DyeColor.WHITE,
                null,
                level.getGameTime()
        );

        if (level.isClientSide()) {
            // client-level: add to client engine so BeamRenderCache gets populated
            BeamEngine.getClient(level).addBeam(beam);
            // optionally tick immediately for instant feedback; main tick also calls tick()
            BeamEngine.getClient(level).tick();
        } else {
            // server-level: add server beam (server does logic; clients won't render until they receive packet)
//            BeamEngine.get(level).addBeam(beam);

            BeamEngine.get(level).addBeam(beam);

            BeamSpawnPacket pkt = new BeamSpawnPacket(
                    worldPosition,
                    dir,
                    powerLevel,
                    Math.max(1, powerLevel * 50),
                    DyeColor.WHITE.getId(),
                    level.getGameTime()
            );

            for (ServerPlayer player : ((ServerLevel) level).players()) {
                player.connection.send(pkt);
            }

            // TODO: send a network packet to nearby players so their clients add the same beam to their client engines.
            // Without that packet, only client-side emits will show visuals on this client.
        }

        // notify clients of block update (keeps tile/block states in sync)
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
    }
}
