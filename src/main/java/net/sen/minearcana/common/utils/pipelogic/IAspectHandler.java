package net.sen.minearcana.common.utils.pipelogic;

import net.sen.minearcana.common.utils.aspect.Aspect;
import net.sen.minearcana.common.utils.aspect.AspectStack;

public interface IAspectHandler {
    AspectStack getAspect();
    void insertAspect(AspectStack stack);
    AspectStack extractAspect(int amount);
    boolean canInsert();
    boolean canExtract();
}
