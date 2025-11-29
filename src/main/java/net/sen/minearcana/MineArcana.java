package net.sen.minearcana;

import com.google.common.reflect.Reflection;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.sen.minearcana.client.MineArcanaClient;
import net.sen.minearcana.client.event.MineArcanaClientEventHandler;
import net.sen.minearcana.common.event.MineArcanaEventHandler;
import net.sen.minearcana.common.registries.*;
import net.sen.minearcana.common.utils.altar.AltarStructureRegistry;
import net.sen.minearcana.config.MineArcanaConfig;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MineArcana.MODID)
public class MineArcana {
    public static final String MODID = "minearcana";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MineArcana(IEventBus eventBus, ModContainer modContainer, Dist dist) {
        if (dist.isClient()) {
            MineArcanaClient.MineArcanaClientRegistry(eventBus);
            MineArcanaClientEventHandler.MineArcanaClientEventHandlerRegistry(eventBus);
        }

        //Event Handlers
        MineArcanaEventHandler.MineArcanaEventHandlerRegistry(eventBus);

        //Vanilla Registries
        MineArcanaBlocks.register(eventBus);
        MineArcanaItems.register(eventBus);
        MineArcanaBlockEntites.register(eventBus);
        MineArcanaEntityTypes.register(eventBus);
        MineArcanaCreativeModeTabs.register(eventBus);
        MineArcanaRecipes.register(eventBus);
        MineArcanaDataComponents.register(eventBus);
        MineArcanaMenuTypes.register(eventBus);

        //Custom Registries
        MineArcanaElements.register(eventBus);
        MineArcanaAspects.register(eventBus);

        eventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.SERVER, MineArcanaConfig.SERVER_SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, MineArcanaConfig.COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, MineArcanaConfig.CLIENT_SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        preInit(event);
        init(event);
        postInit(event);
    }

    private void preInit(FMLCommonSetupEvent event) {
        Reflection.initialize(
        );
    }

    private void init(FMLCommonSetupEvent event) {
        event.enqueueWork(AltarStructureRegistry::init);
    }

    private void postInit(FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
