package net.sen.minearcana.common.utils.aspect;

import net.sen.minearcana.common.utils.element.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Aspect implements IAspect {
    private final String name;
    private final int colour;

    public Aspect(String name, int colour) {
        this.name = name;
        this.colour = colour;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getColour() {
        return colour;
    }
}
