package net.sen.minearcana.client.utils.lightbeam;

import net.minecraft.world.level.Level;

import java.util.*;

public class BeamRenderCache {

    private static final Map<Level, List<BeamSegment>> CACHE = new WeakHashMap<>();

    /** Clears all segments for the given level */
    public static void clear(Level level) {
        List<BeamSegment> list = CACHE.get(level);
        if (list != null) {
            list.clear();
        }
    }

    /** Add a segment to the level cache */
    public static void addSegment(Level level, BeamSegment segment) {
        CACHE.computeIfAbsent(level, l -> new ArrayList<>()).add(segment);
    }

    /** Get all segments for a level */
    public static List<BeamSegment> getSegments(Level level) {
        // Return a *copy* so the renderer cannot break the internal cache
        return new ArrayList<>(CACHE.getOrDefault(level, Collections.emptyList()));
    }
}
