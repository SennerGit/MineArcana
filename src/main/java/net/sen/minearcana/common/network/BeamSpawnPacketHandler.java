package net.sen.minearcana.common.network;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.sen.minearcana.common.utils.lightbeams.Beam;
import net.sen.minearcana.common.utils.lightbeams.BeamEngine;

public class BeamSpawnPacketHandler {

    public static void handle(BeamSpawnPacket pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level == null) return;

            Beam beam = new Beam(
                    level,
                    pkt.origin(),
                    pkt.direction(),
                    pkt.power(),
                    pkt.length(),
                    DyeColor.byId(pkt.colorId()),
                    null,
                    pkt.gameTime()
            );

            BeamEngine.getClient(level).addBeam(beam);
        });
    }
}
