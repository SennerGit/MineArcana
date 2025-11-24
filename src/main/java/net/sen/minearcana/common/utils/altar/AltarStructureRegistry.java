package net.sen.minearcana.common.utils.altar;

import net.sen.minearcana.common.registries.MineArcanaBlocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AltarStructureRegistry {
    private static final Map<AltarType, AltarStructure> STRUCTURES = new HashMap<>();

    public static void register(AltarType type, AltarStructure structure) {
        STRUCTURES.put(type, structure);
    }

    public static AltarStructure get(AltarType type) {
        return STRUCTURES.get(type);
    }

    public static Map<AltarType, AltarStructure> getAll() {
        return Map.copyOf(STRUCTURES);
    }

    public static void init() {
        // Example: Tier 1 = pedestals, Tier 2 = catalysts/markers
        register(AltarType.CEREMONIAL, new AltarStructure(Map.of(
                1, List.of(
                        List.of(OffsetBlock.ofBlock(3, 0, -1, MineArcanaBlocks.ARCANA_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(3, 0, 0, MineArcanaBlocks.ARCANA_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(3, 0, 1, MineArcanaBlocks.ARCANA_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(-3, 0, -1, MineArcanaBlocks.ARCANA_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(-3, 0, 0, MineArcanaBlocks.ARCANA_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(-3, 0, 1, MineArcanaBlocks.ARCANA_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(-1, 0, 3, MineArcanaBlocks.ARCANA_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(0, 0, 3, MineArcanaBlocks.ARCANA_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(1, 0, 3, MineArcanaBlocks.ARCANA_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(-1, 0, -3, MineArcanaBlocks.ARCANA_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(0, 0, -3, MineArcanaBlocks.ARCANA_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(1, 0, -3, MineArcanaBlocks.ARCANA_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(2, 0, -2, MineArcanaBlocks.ARCANA_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(2, 0, 2, MineArcanaBlocks.ARCANA_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(-2, 0, -2, MineArcanaBlocks.ARCANA_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(-2, 0, 2, MineArcanaBlocks.ARCANA_MARKER.get()))
                )
        )));

        register(AltarType.SACRIFICIAL, new AltarStructure(Map.of(
                1, List.of(
                        List.of(OffsetBlock.ofBlock(3, 0, -1, MineArcanaBlocks.BLOOD_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(3, 0, 0, MineArcanaBlocks.BLOOD_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(3, 0, 1, MineArcanaBlocks.BLOOD_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(-3, 0, -1, MineArcanaBlocks.BLOOD_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(-3, 0, 0, MineArcanaBlocks.BLOOD_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(-3, 0, 1, MineArcanaBlocks.BLOOD_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(-1, 0, 3, MineArcanaBlocks.BLOOD_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(0, 0, 3, MineArcanaBlocks.BLOOD_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(1, 0, 3, MineArcanaBlocks.BLOOD_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(-1, 0, -3, MineArcanaBlocks.BLOOD_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(0, 0, -3, MineArcanaBlocks.BLOOD_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(1, 0, -3, MineArcanaBlocks.BLOOD_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(2, 0, -2, MineArcanaBlocks.BLOOD_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(2, 0, 2, MineArcanaBlocks.BLOOD_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(-2, 0, -2, MineArcanaBlocks.BLOOD_MARKER.get())),
                        List.of(OffsetBlock.ofBlock(-2, 0, 2, MineArcanaBlocks.BLOOD_MARKER.get()))
                )
        )));
    }
}
