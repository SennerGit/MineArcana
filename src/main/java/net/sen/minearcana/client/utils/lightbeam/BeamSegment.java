package net.sen.minearcana.client.utils.lightbeam;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

public record BeamSegment(Vec3 start, Vec3 end, DyeColor colour) {}
