package net.sen.minearcana.compat;

import net.sen.minearcana.common.blocks.*;
import net.sen.minearcana.common.blocks.entities.*;
import net.sen.minearcana.compat.waila.*;
import snownee.jade.api.*;

@WailaPlugin
public class MineArcanaWaila implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(ArcanaCauldronComponentProvider.INSTANCE, ArcanaCauldronBlockEntity.class);
        registration.registerBlockDataProvider(AspectExtractorComponentProvider.INSTANCE, AspectExtractorBlockEntity.class);
        registration.registerBlockDataProvider(AspectTankComponentProvider.INSTANCE, AspectTankBlockEntity.class);
        registration.registerBlockDataProvider(BloodMarkerComponentProvider.INSTANCE, BloodMarkerBlockEntity.class);
        registration.registerBlockDataProvider(AltarComponentProvider.INSTANCE, AltarBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(ArcanaCauldronComponentProvider.INSTANCE, ArcanaCauldronBlock.class);
        registration.registerBlockComponent(AspectExtractorComponentProvider.INSTANCE, AspectExtractorBlock.class);
        registration.registerBlockComponent(AspectTankComponentProvider.INSTANCE, AspectTankBlock.class);
        registration.registerBlockComponent(BloodMarkerComponentProvider.INSTANCE, BloodMarkerBlock.class);
        registration.registerBlockComponent(AltarComponentProvider.INSTANCE, AltarBlock.class);
    }
}
