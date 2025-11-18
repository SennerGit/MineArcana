package net.sen.minearcana.config;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineArcanaConfig {
    public static class Server {
        public Server(ModConfigSpec.Builder builder) {

        }
    }

    public static class Common {
        public final ModConfigSpec.ConfigValue<List<? extends String>> fluidEntries;
        public final ModConfigSpec.ConfigValue<List<? extends String>> heatSourceEntries;

        public Common(ModConfigSpec.Builder builder) {
            builder.push("cauldron");
                builder.comment("List of allowed cauldron fluids and their temperature properties");
                fluidEntries = builder.defineList("fluids", List.of(
                        addCauldronFluid(Fluids.WATER, 25, 100),
                        addCauldronFluid(Fluids.LAVA, 1200, 1200)
                ), o -> o instanceof String);

                builder.comment("List of heat sources and their output levels");
                heatSourceEntries = builder.defineList("heat_sources", List.of(
                        addCauldronHeatSource(Blocks.CAMPFIRE, 150),
                        addCauldronHeatSource(Blocks.LAVA, 1200),
                        addCauldronHeatSource(Blocks.FIRE, 600)
                ), o -> o instanceof String);
            builder.pop();

            builder.build();
        }

        private static String addCauldronFluid(Fluid fluid, int baseTemp, int maxTemp) {
            ResourceLocation id = BuiltInRegistries.FLUID.getKey(fluid);
            if (id == null) id = ResourceLocation.fromNamespaceAndPath("minecraft", "empty");
            return String.format("%s,%d,%d", id.toString(), baseTemp, maxTemp);
        }

        private static String addCauldronHeatSource(Block block, int heatLevel) {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
            if (id == null) id = ResourceLocation.fromNamespaceAndPath("minecraft", "air");
            return String.format("%s,%d", id.toString(), heatLevel);
        }
    }

    public static class Client {
        public Client(ModConfigSpec.Builder builder) {

        }
    }

    public static final ModConfigSpec SERVER_SPEC;
    public static final Server SERVER;

    public static final ModConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    public static final ModConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;

    static {
        final Pair<Server, ModConfigSpec> serverSpecPair = new ModConfigSpec.Builder().configure(MineArcanaConfig.Server::new);
        SERVER_SPEC = serverSpecPair.getRight();
        SERVER = serverSpecPair.getLeft();

        final Pair<Common, ModConfigSpec> commonSpecPair = new ModConfigSpec.Builder().configure(MineArcanaConfig.Common::new);
        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();

        final Pair<Client, ModConfigSpec> clientSpecPair = new ModConfigSpec.Builder().configure(MineArcanaConfig.Client::new);
        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }
}
