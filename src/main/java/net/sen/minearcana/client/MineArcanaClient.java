package net.sen.minearcana.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.sen.minearcana.MineArcana;

@Mod(value = MineArcana.MODID, dist = Dist.CLIENT)
public class MineArcanaClient {
    public static void MineArcanaClientRegistry(IEventBus eventBus) {
        eventBus.addListener(MineArcanaClient::onClientSetup);
        eventBus.addListener(MineArcanaClient::renderEntities);
        eventBus.addListener(MineArcanaClient::registerLayer);
        eventBus.addListener(MineArcanaClient::registerScreens);
    }

    public static void onClientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
        });
    }

    private static void renderEntities(EntityRenderersEvent.RegisterRenderers  event) {
    }

    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }

    public static void registerScreens(RegisterMenuScreensEvent event) {
    }
}
