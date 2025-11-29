package net.sen.minearcana.common.utils.lightbeams;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.sen.minearcana.common.utils.aspect.Aspect;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Immutable beam descriptor. Use getStartVec() / getEndVec() to obtain world-space endpoints
 * for rendering. The "origin" is the BlockPos where the beam originates (center of block).
 */
public final class Beam {
    private final Level level;
    private final BlockPos origin;
    private final Vec3 direction;
    private final int strength;
    private final int maxDistance;
    private final DyeColor colour;
    @Nullable
    private final Aspect aspect;
    private final long createdTick;

    public Beam(Level level, BlockPos origin, Vec3 direction, int strength, int maxDistance, DyeColor colour, @Nullable Aspect aspect, long createdTick) {
        this.level = Objects.requireNonNull(level, "level");
        this.origin = Objects.requireNonNull(origin, "origin");
        this.direction = Objects.requireNonNull(direction, "direction").normalize();
        this.strength = strength;
        this.maxDistance = Math.max(0, maxDistance);
        this.colour = Objects.requireNonNull(colour, "colour");
        this.aspect = aspect;
        this.createdTick = createdTick;
    }

    public Level level() {
        return level;
    }

    public BlockPos origin() {
        return origin;
    }

    public Vec3 direction() {
        return direction;
    }

    public int strength() {
        return strength;
    }

    public int maxDistance() {
        return maxDistance;
    }

    public DyeColor colour() {
        return colour;
    }

    @Nullable
    public Aspect aspect() {
        return aspect;
    }

    public long createdTick() {
        return createdTick;
    }

    /**
     * World-space start vector (center of origin block).
     */
    public Vec3 getStartVec() {
        return origin.getCenter();
    }

    /**
     * End vector computed by start + direction * distance (distance = maxDistance).
     * Useful for renderers that want a single long line endpoint.
     */
    public Vec3 getEndVec() {
        return getStartVec().add(direction.scale(maxDistance));
    }
}
