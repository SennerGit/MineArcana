package net.sen.minearcana.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sen.minearcana.common.blocks.entities.BloodMarkerBlockEntity;
import net.sen.minearcana.common.registries.MineArcanaBlocks;
import net.sen.minearcana.common.registries.MineArcanaDataComponents;
import net.sen.minearcana.common.utils.RitualPlacementUtil;

import java.util.List;
import java.util.Locale;

public class RitualKnifeItem extends SwordItem {
    public static final int MAX_BLOOD = 10;

    public RitualKnifeItem() {
        super(Tiers.IRON, new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (player.isCrouching()) {
            DamageSources damageSources = level.damageSources();
            player.hurt(damageSources.magic(), 1.0F);
            player.getItemInHand(usedHand).set(MineArcanaDataComponents.BLOOD_AMOUNT,  MAX_BLOOD);
            player.getItemInHand(usedHand).set(MineArcanaDataComponents.BLOOD_ENTITY,  player.getGameProfile().getName());
            return InteractionResultHolder.pass(player.getItemInHand(usedHand));
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof Player player1) {
            stack.set(MineArcanaDataComponents.BLOOD_AMOUNT, MAX_BLOOD);
            stack.set(MineArcanaDataComponents.BLOOD_ENTITY, player1.getGameProfile().getName());
        } else if (entity != null) {
            stack.set(MineArcanaDataComponents.BLOOD_AMOUNT, MAX_BLOOD);
            stack.set(MineArcanaDataComponents.BLOOD_ENTITY, BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString().toLowerCase(Locale.ROOT));
        }


        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (player.isCrouching())
            return super.useOn(context);

        if (!hasBlood(stack))
            return InteractionResult.FAIL;

        int blood = stack.get(MineArcanaDataComponents.BLOOD_AMOUNT);
        if (blood <= 0)
            return InteractionResult.FAIL;

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (!RitualPlacementUtil.canPlace(level, pos))
            return InteractionResult.FAIL;

        BlockPos placePos = pos.above();

        boolean placed = RitualPlacementUtil.placeMarker(level, placePos,
                MineArcanaBlocks.BLOOD_MARKER.get().defaultBlockState());

        if (!placed)
            return InteractionResult.FAIL;

        // Set BE data
        BlockEntity be = level.getBlockEntity(placePos);
        if (be instanceof BloodMarkerBlockEntity marker) {
            marker.setEntityID(stack.get(MineArcanaDataComponents.BLOOD_ENTITY));
            marker.setChanged();
        }

        // Reduce blood
        if (!player.isCreative())
            stack.set(MineArcanaDataComponents.BLOOD_AMOUNT, blood - 1);

        return InteractionResult.SUCCESS;
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (hasBlood(stack)) {
            int bloodAmount = stack.get(MineArcanaDataComponents.BLOOD_AMOUNT);
            String bloodEntity = stack.get(MineArcanaDataComponents.BLOOD_ENTITY);

            tooltipComponents.add(Component.literal(bloodEntity + " Blood: " + bloodAmount + "/" + MAX_BLOOD));
        } else {
            tooltipComponents.add(Component.literal("null Blood: 0/" + MAX_BLOOD));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }


    private boolean hasBlood(ItemStack stack) {
        return stack.get(MineArcanaDataComponents.BLOOD_AMOUNT) != null && stack.get(MineArcanaDataComponents.BLOOD_ENTITY) != null;
    }
}
