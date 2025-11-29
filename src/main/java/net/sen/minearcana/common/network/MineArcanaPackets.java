package net.sen.minearcana.common.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.sen.minearcana.MineArcana;

public class MineArcanaPackets {

    public static void register(RegisterPayloadHandlersEvent event) {

        event.registrar(MineArcana.MODID)
                .playToClient(
                        BeamSpawnPacket.TYPE,
                        BeamSpawnPacket.CODEC,
                        BeamSpawnPacketHandler::handle
                );
    }
}
