package net.sen.minearcana.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.DyeColor;
import net.sen.minearcana.MineArcana;

public record BeamSpawnPacket(
        BlockPos origin,
        Vec3 direction,
        int power,
        int length,
        int colorId,
        long gameTime
) implements CustomPacketPayload {

    public static final Type<BeamSpawnPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(MineArcana.MODID, "beam_spawn"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BeamSpawnPacket> CODEC =
            StreamCodec.of(BeamSpawnPacket::write, BeamSpawnPacket::read);

    private static void write(RegistryFriendlyByteBuf buf, BeamSpawnPacket pkt) {
        buf.writeBlockPos(pkt.origin);
        buf.writeDouble(pkt.direction.x);
        buf.writeDouble(pkt.direction.y);
        buf.writeDouble(pkt.direction.z);
        buf.writeInt(pkt.power);
        buf.writeInt(pkt.length);
        buf.writeInt(pkt.colorId);
        buf.writeLong(pkt.gameTime);
    }

    private static BeamSpawnPacket read(RegistryFriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        Vec3 dir = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        int power = buf.readInt();
        int length = buf.readInt();
        int color = buf.readInt();
        long time = buf.readLong();
        return new BeamSpawnPacket(pos, dir, power, length, color, time);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
