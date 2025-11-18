package net.sen.minearcana.common.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.recipes.ArcanaCauldronRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MineArcanaRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MineArcana.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MineArcana.MODID);

    public static final Supplier<RecipeSerializer<ArcanaCauldronRecipe>> ARCANA_CAULDRON_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("arcana_cauldron", ArcanaCauldronRecipe.Serializer::new);
    public static final Supplier<RecipeType<ArcanaCauldronRecipe>> ARCANA_CAULDRON_RECIPE_TYPE = RECIPE_TYPES.register("arcana_cauldron", () -> new RecipeType<ArcanaCauldronRecipe>() {
        @Override
        public String toString() {
            return "arcana_cauldron";
        }
    });

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }
}
