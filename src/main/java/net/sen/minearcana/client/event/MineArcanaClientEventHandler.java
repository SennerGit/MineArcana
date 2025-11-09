package net.sen.minearcana.client.event;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

public class MineArcanaClientEventHandler {
    public static void MineArcanaClientEventHandlerRegistry(IEventBus eventBus) {
        eventBus.addListener(MineArcanaClientEventHandler::onToolTip);
    }

    private static void onToolTip(ItemTooltipEvent event) {
        addMagicElementTooltip(event);
        addMagicAspectTooltip(event);
    }

    private static void addMagicElementTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        List<Component> tooltip = event.getToolTip();

        for (TagKey<Item> tagKey : itemStack.getTags().toList()) {
        }
    }

    private static void addMagicAspectTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        List<Component> tooltip = event.getToolTip();

        for (TagKey<Item> tagKey : itemStack.getTags().toList()) {
        }
    }
}
