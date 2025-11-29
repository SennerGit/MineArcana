package net.sen.minearcana.common.utils.lightbeams;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.sen.minearcana.client.utils.lightbeam.BeamRenderCache;
import net.sen.minearcana.client.utils.lightbeam.BeamSegment;
import net.sen.minearcana.common.blocks.ArcaneLightReceiverBlock;
import net.sen.minearcana.common.blocks.ArcaneMirrorBlock;

import java.util.*;

/**
 * BeamEngine: per-level engine. Use get(level) for server engine,
 * getClient(level) for client engine.
 *
 * Responsibilities:
 *  - store active beams (server+client)
 *  - tick beams each tick
 *  - on client: push visible BeamSegment to BeamRenderCache
 *
 * Note: tick() must be called regularly (server tick for server engine; client tick or before render for client).
 */
public class BeamEngine {
    private static final Map<Level, BeamEngine> INSTANCES = new HashMap<>();
    private static final Map<Level, BeamEngine> CLIENT_ENGINES = new WeakHashMap<>();

    /**
     * Server-side engine (or authoritative world-side engine).
     */
    public static BeamEngine get(Level level) {
        return INSTANCES.computeIfAbsent(level, l -> new BeamEngine(l, false));
    }

    /**
     * Client-only engine (keeps beams local for rendering).
     */
    public static BeamEngine getClient(Level level) {
        return CLIENT_ENGINES.computeIfAbsent(level, l -> new BeamEngine(l, true));
    }

    private final Level level;
    private final boolean isClient;
    private final List<Beam> activeBeams = new ArrayList<>(); // guarded by 'this'

    private BeamEngine(Level level, boolean isClient) {
        this.level = level;
        this.isClient = isClient;
    }

    /**
     * Add a beam to be simulated. Safe to call from game threads but avoid calling from other threads.
     */
    public synchronized void addBeam(Beam beam) {
        activeBeams.add(beam);
    }

    /**
     * Returns an unmodifiable snapshot of active beams (for debug/inspection).
     */
    public synchronized List<Beam> getActiveBeams() {
        return List.copyOf(activeBeams);
    }

    /**
     * Tick the engine. Must be called once per tick.
     * Client variant clears render cache and pushes segments into BeamRenderCache.
     */
    public void tick() {
        if (isClient) {
            BeamRenderCache.clear(level); // clear previous frame segments
        }

        // Iterate via iterator so we can remove safely
        synchronized (this) {
            Iterator<Beam> it = activeBeams.iterator();
            while (it.hasNext()) {
                Beam b = it.next();
                boolean finished = traceBeam(b);
                if (finished) {
                    it.remove();
                }
            }
        }
    }

    /**
     * Trace a beam forward. Returns true if the beam finished and should be removed.
     *
     * Behavior:
     *  - steps forward up to maxDistance (1 block per step)
     *  - client: push segments for rendering
     *  - mirrors: redirect and continue
     *  - stained glass: spawn a new coloured beam starting at glass; end this beam
     *  - receiver: call receiveBeam and end beam
     *  - solid blocks: stop beam
     */
    private boolean traceBeam(Beam beam) {
        Vec3 pos = beam.getStartVec();
        Vec3 dir = beam.direction().normalize();

        for (int step = 0; step < beam.maxDistance(); step++) {
            Vec3 prev = pos;
            pos = pos.add(dir);
            BlockPos blockPos = BlockPos.containing(pos);
            BlockState state = level.getBlockState(blockPos);

            if (isClient) {
                // store the segment prev->pos for rendering
                BeamRenderCache.addSegment(level, new BeamSegment(prev, pos, beam.colour()));
            }

            // Mirror redirect
            if (state.getBlock() instanceof ArcaneMirrorBlock mirror) {
                dir = mirror.reflect(dir, state);
                continue;
            }

            // Stained glass — spawn a new beam with new colour at this block, stop this one
            if (state.getBlock() instanceof StainedGlassBlock glass) {
                DyeColor newColour = glass.getColor();
                Beam newBeam = new Beam(
                        beam.level(),
                        blockPos,
                        dir,
                        beam.strength(),
                        beam.maxDistance(),
                        newColour,
                        beam.aspect(),
                        beam.createdTick()
                );
                addBeam(newBeam);
                return true;
            }

            // Receiver — deliver and stop
            if (state.getBlock() instanceof ArcaneLightReceiverBlock receiver) {
                receiver.receiveBeam(level, blockPos, beam);
                return true;
            }

            // Solid block stops the beam
            if (state.isSolid()) {
                return true;
            }
        }

        // reached max distance
        return true;
    }
}
