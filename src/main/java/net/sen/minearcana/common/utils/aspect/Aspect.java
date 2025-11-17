package net.sen.minearcana.common.utils.aspect;

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
