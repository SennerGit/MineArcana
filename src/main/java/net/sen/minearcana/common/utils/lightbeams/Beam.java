package net.sen.minearcana.common.utils.lightbeams;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sen.minearcana.common.utils.aspect.Aspect;

import javax.annotation.Nullable;

public record Beam(
        Level level,
        BlockPos origin,
        Vec3 direction,
        int strength,
        int maxDistance,
        DyeColor colour,   // Enum you define
        @Nullable Aspect aspect,   // Future feature
        long createdTick
) {}

