package net.sen.minearcana.common.utils.element;

import java.util.function.Supplier;

public class Element implements IElement{
    private final String name;
    private final int colour;

    public Element(String name, int colour) {
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
