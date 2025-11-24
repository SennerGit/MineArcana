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
 * BeamEngine is responsible for simulating all active beams each tick.
 * On the client it also forwards visible beam segments into BeamRenderCache.
 */
public class BeamEngine {

    /** Per-world BeamEngines — server side */
    private static final Map<Level, BeamEngine> INSTANCES = new HashMap<>();

    /** Client-only BeamEngines stored weakly to avoid memory leaks */
    private static final Map<Level, BeamEngine> CLIENT_ENGINES = new WeakHashMap<>();


    /** SERVER version */
    public static BeamEngine get(Level level) {
        return INSTANCES.computeIfAbsent(level, BeamEngine::new);
    }

    /** CLIENT version */
    public static BeamEngine getClient(Level level) {
        return CLIENT_ENGINES.computeIfAbsent(level, BeamEngine::new);
    }

    // =========================================================

    private final Level level;
    private final List<Beam> activeBeams = new ArrayList<>();

    private BeamEngine(Level level) {
        this.level = level;
    }

    // =========================================================
    // Public API
    // =========================================================

    public void addBeam(Beam beam) {
        activeBeams.add(beam);
    }

    /** Called every tick */
    public void tick() {

        // If client: clear old segments before calculating new ones
        if (level.isClientSide()) {
            BeamRenderCache.clear(level);
        }

        Iterator<Beam> iterator = activeBeams.iterator();

        while (iterator.hasNext()) {
            Beam beam = iterator.next();

            boolean finished = traceBeam(beam);

            if (finished) {
                iterator.remove();
            }
        }
    }

    // =========================================================
    // Beam simulation core
    // =========================================================

    /**
     * Traces one beam forward.
     *
     * @return true if the beam is finished and should be removed.
     */
    private boolean traceBeam(Beam beam) {

        Vec3 currentPos = beam.origin().getCenter();
        Vec3 dir = beam.direction().normalize();

        for (int step = 0; step < beam.maxDistance(); step++) {

            Vec3 lastPos = currentPos;
            currentPos = currentPos.add(dir);

            BlockPos blockPos = BlockPos.containing(currentPos);
            BlockState state = level.getBlockState(blockPos);

            // CLIENT: store segment for rendering
            if (level.isClientSide()) {
                BeamRenderCache.addSegment(
                        level,
                        new BeamSegment(lastPos, currentPos, beam.colour())
                );
            }

            // -----------------------------------------------------
            //  MIRROR REDIRECTION
            // -----------------------------------------------------
            if (state.getBlock() instanceof ArcaneMirrorBlock mirror) {
                dir = mirror.reflect(dir, state);
                continue; // Continue beam with new direction
            }

            // -----------------------------------------------------
            //  GLASS — recolour and stop this beam
            // -----------------------------------------------------
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
                return true; // Original beam ends
            }

            // -----------------------------------------------------
            // RECEIVER HIT — produce redstone signal
            // -----------------------------------------------------
            if (state.getBlock() instanceof ArcaneLightReceiverBlock receiver) {
                receiver.receiveBeam(level, blockPos, beam);
                return true;
            }

            // -----------------------------------------------------
            // SOLID BLOCK — stop
            // -----------------------------------------------------
            if (state.isSolid()) {
                return true;
            }
        }

        // Beam reached maxDistance naturally
        return true;
    }
}
