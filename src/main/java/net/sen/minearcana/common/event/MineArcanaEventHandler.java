package net.sen.minearcana.common.event;

import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.registries.MineArcanaRegistries;
import net.sen.minearcana.common.utils.lightbeams.BeamEngine;
import net.sen.minearcana.data.aspects.MagicAspectDataLoader;

public class MineArcanaEventHandler {
    public static void MineArcanaEventHandlerRegistry(IEventBus eventBus) {
        // Add data loader for magic aspects
        eventBus.addListener(MineArcanaEventHandler::registerPackets);
        eventBus.addListener(MineArcanaEventHandler::registerRegistries);
        NeoForge.EVENT_BUS.addListener(MineArcanaEventHandler::addDataLoaders);
        NeoForge.EVENT_BUS.addListener(MineArcanaEventHandler::onWorldTick);
    }
    private static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MineArcana.MODID).versioned("0.1.0").optional();
    }

    private static void addDataLoaders(AddReloadListenerEvent event) {
        event.addListener(new MagicAspectDataLoader());
    }

    private static void registerRegistries(NewRegistryEvent event) {
        MineArcanaRegistries.register(event);
    }

    public static void onWorldTick(LevelTickEvent.Post event) {
        Level level = event.getLevel();
        if (level.isClientSide()) return;

        BeamEngine.get(level).tick();
    }
}
