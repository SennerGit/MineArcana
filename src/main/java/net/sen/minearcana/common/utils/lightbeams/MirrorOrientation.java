package net.sen.minearcana.common.utils.lightbeams;

import net.minecraft.util.StringRepresentable;

public enum MirrorOrientation implements StringRepresentable {
    STRAIGHT,   // reflects left-right
    DIAGONAL_NE_SW, // like "\" diagonal mirror
    DIAGONAL_NW_SE; // like "/" diagonal mirror

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}
