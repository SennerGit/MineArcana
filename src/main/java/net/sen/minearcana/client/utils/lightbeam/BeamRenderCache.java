package net.sen.minearcana.client.utils.lightbeam;

import net.minecraft.world.level.Level;

import java.util.*;

/**
 * Client-side per-level cache of BeamSegments produced by the client BeamEngine.
 * The renderer should call BeamRenderCache.getSegments(level) each frame and iterate
 * over the returned list to draw beam quads/lines.
 *
 * We return copies to protect internal storage from concurrent modification by the renderer.
 */
public final class BeamRenderCache {
    private static final Map<Level, List<BeamSegment>> CACHE = new WeakHashMap<>();

    private BeamRenderCache() {}

    public static void clear(Level level) {
        List<BeamSegment> list = CACHE.get(level);
        if (list != null) list.clear();
    }

    public static void addSegment(Level level, BeamSegment segment) {
        CACHE.computeIfAbsent(level, l -> new ArrayList<>()).add(segment);
    }

    /**
     * Returns a copy of the segment list for the given level (safe for iteration).
     */
    public static List<BeamSegment> getSegments(Level level) {
        return new ArrayList<>(CACHE.getOrDefault(level, Collections.emptyList()));
    }
}
