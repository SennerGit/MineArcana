package net.sen.minearcana.common.utils.pipelogic;

import net.minecraft.util.StringRepresentable;

public enum PipeMode implements StringRepresentable {
    PIPE("pipe"),
    INPUT("input"),
    OUTPUT("output"),
    BOTH("both"),
    DISABLED("disabled");

    private final String serialized;

    PipeMode(String serialized) {
        this.serialized = serialized;
    }

    @Override
    public String getSerializedName() {
        return serialized;
    }
}
