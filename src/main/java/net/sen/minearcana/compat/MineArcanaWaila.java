package net.sen.minearcana.compat;

import net.sen.minearcana.common.blocks.ArcanaCauldronBlock;
import net.sen.minearcana.common.blocks.entities.ArcanaCauldronBlockEntity;
import net.sen.minearcana.compat.waila.ArcanaCauldronComponentProvider;
import snownee.jade.api.*;

@WailaPlugin
public class MineArcanaWaila implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(ArcanaCauldronComponentProvider.INSTANCE, ArcanaCauldronBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(ArcanaCauldronComponentProvider.INSTANCE, ArcanaCauldronBlock.class);
    }
}
