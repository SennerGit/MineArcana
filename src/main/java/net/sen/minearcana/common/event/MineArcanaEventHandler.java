package net.sen.minearcana.common.event;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.sen.minearcana.data.aspects.MagicAspectDataLoader;

public class MineArcanaEventHandler {
    public static void MineArcanaEventHandlerRegistry(IEventBus eventBus) {
        // Add data loader for magic aspects
        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            event.addListener(new MagicAspectDataLoader());
        });
    }
}
