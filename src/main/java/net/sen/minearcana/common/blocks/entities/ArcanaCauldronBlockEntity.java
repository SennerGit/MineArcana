package net.sen.minearcana.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.recipes.ArcanaCauldronRecipe;
import net.sen.minearcana.common.recipes.ArcanaCauldronRecipeInput;
import net.sen.minearcana.common.registries.MineArcanaBlockEntites;
import net.sen.minearcana.common.registries.MineArcanaRecipes;
import net.sen.minearcana.common.registries.MineArcanaRegistries;
import net.sen.minearcana.common.utils.aspect.Aspect;
import net.sen.minearcana.common.utils.aspect.AspectStack;
import net.sen.minearcana.config.MineArcanaConfig;
import net.sen.minearcana.data.aspects.MagicAspectDataLoader;

import java.util.*;

public class ArcanaCauldronBlockEntity extends BlockEntity  {
    public static final int MAX_FLUID = 3 * FluidType.BUCKET_VOLUME;
    private FluidStack fluidStack = FluidStack.EMPTY;
    private final List<AspectStack> aspects = new ArrayList<>();
    private int temperature; // Current temp
    private int baseTemp;
    private int minTemp;
    private int maxTemp;
    private int sourceTemp; // heat from block below
    ItemStack inputItem;

    private Item outputPotion;
    private List<AspectStack> outputAspect;

    public static final String FLUID_TAG = "Fluid";
    public static final String ASPECT_TAG = "Aspects";
    public static final String TEMPERATURE_TAG = "Temperature";
    public static final String TEMPERATURE_BASE_TAG = "BaseTemperature";
    public static final String TEMPERATURE_MIN_TAG = "MinTemperature";
    public static final String TEMPERATURE_MAX_TAG = "MaxTemperature";
    public static final String OUTPUT_POTION_TAG = "OutputPotion";
    public static final String OUTPUT_ASPECT_TAG = "OutputAspect";

    public ArcanaCauldronBlockEntity(BlockPos pos, BlockState blockState) {
        super(MineArcanaBlockEntites.ARCANA_CAULDRON.get(), pos, blockState);
    }

    /**
    TICKER
     */
    public static void serverTick(Level level, BlockPos pos, BlockState state, ArcanaCauldronBlockEntity be) {
        if (level == null || level.isClientSide) return;

        be.updateHeatSource(level.getBlockState(pos.below()));
        be.updateTemperature();

        // Optional: auto-check recipes
        Optional<ArcanaCauldronRecipe> recipe = be.findMatchingRecipe(be.inputItem);
        if (recipe.isPresent()) {
            // Could increment cookingProgress or show animation
        }
    }

    /**
     * FLUIDS
     */
    public boolean addWater(FluidStack stack) {
        if (stack.isEmpty()) return false;

        ResourceLocation fluidId = BuiltInRegistries.FLUID.getKey(stack.getFluid());
        if (fluidId == null) return false;

        if (fluidStack.getAmount() >= MAX_FLUID) {
            fluidStack.setAmount(MAX_FLUID);
            return false;
        }

        if (!fluidStack.isEmpty() && !fluidStack.getFluid().equals(stack.getFluid())) {
            // Block already contains a different fluid — refuse
            return false;
        }

        // If already full
        if (!fluidStack.isEmpty() && fluidStack.getAmount() >= MAX_FLUID) {
            fluidStack.setAmount(MAX_FLUID);
            return false;
        }

        // Add or top off fluid
        if (fluidStack.isEmpty()) {
            for (String entry : MineArcanaConfig.COMMON.fluidEntries.get()) {
                String[] parts = entry.split(",");
                if (parts.length < 4 || parts.length > 5) continue; // skip invalid entries

                ResourceLocation key = ResourceLocation.tryParse(parts[0]);
                if (!fluidId.equals(key)) continue;

                int parsedBase, parsedMin, parsedMax;
                try {
                    parsedBase = Integer.parseInt(parts[1]);
                    parsedMin = Integer.parseInt(parts[2]);
                    parsedMax = Integer.parseInt(parts[3]);
                } catch (NumberFormatException e) {
                    MineArcana.LOGGER.error("Invalid numbers for fluid");
                    continue; // skip invalid numbers
                }

                fluidStack = new FluidStack(stack.getFluid(), Math.min(stack.getAmount(), MAX_FLUID));
                baseTemp = parsedBase;
                minTemp = parsedMin;
                maxTemp = parsedMax;
                temperature = Math.max(temperature, baseTemp);

                setChanged();

                return true;
            }
        }

        if (fluidStack.getFluid().equals(stack.getFluid())) {
            int fill = Math.min(stack.getAmount(), MAX_FLUID - fluidStack.getAmount());
            if (fill <= 0) return false;
            fluidStack.setAmount(fluidStack.getAmount() + fill);
            setChanged();
            return true;
        }

        return false;
    }

    public boolean removeWater(FluidStack stack) {
        if (fluidStack.isEmpty() || fluidStack.getAmount() < stack.getAmount()) return false;
        if (!aspects.isEmpty()) return false; // Can't remove if aspects are inside

        fluidStack.setAmount(fluidStack.getAmount() - stack.getAmount());
        if (fluidStack.getAmount() <= 0) {
            fluidStack = FluidStack.EMPTY;
            aspects.clear();
        }

        setChanged();
        return true;
    }

    public FluidStack getFluid() {
        return fluidStack;
    }

    /**
    TEMPERATURES
     */
    private void updateHeatSource(BlockState below) {
        sourceTemp = 0;

        if (below == null) return;

        Block block = below.getBlock();
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        if (id == null) return;

        for (String entry : MineArcanaConfig.COMMON.heatSourceEntries.get()) {
            String[] parts = entry.split(",");
            if (parts.length != 2) continue;

            ResourceLocation key = ResourceLocation.parse(parts[0]);
            int level;
            try {
                level = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                continue;
            }

            if (id.equals(key)) {
                // Only count if lit (for campfires/furnaces)
                if (below.hasProperty(BlockStateProperties.LIT) && !below.getValue(BlockStateProperties.LIT)) {
                    level = 0;
                }

                sourceTemp = level;
                break;
            }
        }
    }

    private void updateTemperature() {
        // Desired target = base + source (but never exceed maxTemp)
        int target = baseTemp + Math.max(0, sourceTemp);
        if (target > maxTemp) target = maxTemp;

        // move temperature toward target smoothly
        final float changeRate = 0.05f; // adjust to taste
        float diff = target - temperature;
        float delta = diff * changeRate;

        // if very small change, snap
        if (Math.abs(diff) < 0.5f) {
            temperature = target;
        } else {
            temperature = Math.round(temperature + delta);
        }

        // clamp to [minTemp, maxTemp]
        if (temperature > maxTemp) temperature = maxTemp;
        if (temperature < minTemp) temperature = minTemp;

        setChanged();
    }

    public int getTemperature() {
        return temperature;
    }

    public int getBaseTemperature() {
        return baseTemp;
    }

    public int getMinTemperature() {
        return minTemp;
    }

    public int getMaxTemperature() {
        return maxTemp;
    }

    /**
    ASPECTS
     */
    public boolean processItem(ItemStack stack) {
        if (fluidStack.isEmpty()) {
            MineArcana.LOGGER.debug("There is no fluid");
            return false;
        }
        if (fluidStack.getAmount() < FluidType.BUCKET_VOLUME) {
            MineArcana.LOGGER.debug("Not enough fluid");
            return false;
        }
        if (temperature < 100) {
            MineArcana.LOGGER.debug("Heat too low");
            return false;
        }

        Map<String, Integer> aspects = MagicAspectDataLoader.ITEM_ASPECT_VALUES.getOrDefault(stack.getItem(), Collections.emptyMap());

        if (aspects.isEmpty()) {
            MineArcana.LOGGER.debug("No Aspects");
            return false;
        }

        for (Map.Entry<String, Integer> entry : aspects.entrySet()) {
            int value = entry.getValue();

            ResourceLocation aspectKey;
            try {
                aspectKey = ResourceLocation.parse(entry.getKey());
            } catch (Exception e) {
                MineArcana.LOGGER.error("Invalid aspect key in data: {}", entry.getKey());
                return false;
            }

            AspectStack aspectStack = new AspectStack(MineArcanaRegistries.ASPECT.get(aspectKey), value * stack.getCount());

            addAspect(aspectStack);
        }
        return true;
    }

    public List<AspectStack> getAspects() {
        return Collections.unmodifiableList(aspects);
    }

    public AspectStack findAspect(Aspect aspect) {
        for (AspectStack stack : aspects) {
            if (stack.getAspect().equals(aspect)) {
                return stack;
            }
        }
        return AspectStack.empty();
    }

    public boolean addAspect(AspectStack aspectStack) {
        for (AspectStack existing : aspects) {
            if (existing.getAspect().equals(aspectStack.getAspect())) {
                existing.setAmount(existing.getAmount() + aspectStack.getAmount());

                setChanged();
                return true;
            }
        }
        // No existing aspect matched, add new one
        aspects.add(aspectStack);

        setChanged();
        return true;
    }

    public boolean removeAspect(AspectStack aspectStack) {
        for (int i = 0; i < aspects.size(); i++) {
            AspectStack stack = aspects.get(i);
            if (stack.getAspect().equals(aspectStack.getAspect())) {
                int remaining = stack.getAmount() - aspectStack.getAmount();
                if (remaining > 0) {
                    stack.setAmount(remaining);
                } else {
                    aspects.remove(i);
                }
                setChanged();

                return true;
            }
        }
        return false;
    }

    public void empty() {
        fluidStack = FluidStack.EMPTY;
        aspects.clear();
        setChanged();
    }
    
    /**
    RECIPE
     */
    private RecipeManager getRecipeManager() {
        if (level == null) return null;
        if (!(level instanceof ServerLevel serverLevel)) return null;
        return serverLevel.getServer().getRecipeManager();
    }

    public Optional<ArcanaCauldronRecipe> findMatchingRecipe(ItemStack itemStack) {
        RecipeManager recipeManager = getRecipeManager();
        if (recipeManager == null) return Optional.empty();
        this.inputItem = itemStack;

        // Query all recipes of our type
        return recipeManager.getAllRecipesFor(MineArcanaRecipes.ARCANA_CAULDRON_RECIPE_TYPE.get())
                .stream()
                .map(holder -> holder.value()) // <-- unwrap the RecipeHolder
                .filter(recipe -> recipe.matches(getCurrentRecipeInput(), level))
                .findFirst();
    }

    private ArcanaCauldronRecipeInput getCurrentRecipeInput() {
        return new ArcanaCauldronRecipeInput(
                inputItem,
                fluidStack,
                new ArrayList<>(aspects), // copy to avoid mutation
                temperature
        );
    }

    public ItemStack extractPotion() {
        Optional<ArcanaCauldronRecipe> optionalRecipe = findMatchingRecipe(inputItem);
        if (optionalRecipe.isEmpty()) return ItemStack.EMPTY;

        ArcanaCauldronRecipe recipe = optionalRecipe.get();
        ArcanaCauldronRecipeInput input = recipe.input();

        // Remove scaled aspects
        for (AspectStack required : input.aspect()) {
            int scaledAmount = required.getAmount() * fluidStack.getAmount() / FluidType.BUCKET_VOLUME;
            removeAspect(new AspectStack(required.getAspect(), scaledAmount));
        }

        // Remove fluid (e.g., 1/3 of bucket per potion)
        removeWater(new FluidStack(fluidStack.getFluid(), FluidType.BUCKET_VOLUME / 3));

        setChanged();

        return recipe.assemble(getCurrentRecipeInput(), level.registryAccess());
    }


    /**
     * SAVING/LOADING
     */
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        // Load fluid
        if (tag.contains(FLUID_TAG)) {
            fluidStack = FluidStack.parseOptional(registries, tag.getCompound(FLUID_TAG));

            //Load Temperatures
            baseTemp = tag.contains(TEMPERATURE_BASE_TAG) ? tag.getInt(TEMPERATURE_BASE_TAG) : 0;
            minTemp = tag.contains(TEMPERATURE_MIN_TAG) ? tag.getInt(TEMPERATURE_MIN_TAG) : 0;
            maxTemp = tag.contains(TEMPERATURE_MAX_TAG) ? tag.getInt(TEMPERATURE_MAX_TAG) : 0;
            temperature = tag.contains(TEMPERATURE_TAG) ? tag.getInt(TEMPERATURE_TAG) : 0;

            if (tag.contains(OUTPUT_POTION_TAG)) {
                outputPotion = BuiltInRegistries.ITEM.get(ResourceLocation.parse(tag.getString(OUTPUT_POTION_TAG)));
            }
            
            // Load aspects
            aspects.clear();
            outputAspect.clear();
            
            if (tag.contains(ASPECT_TAG)) {
                CompoundTag aspectTag = tag.getCompound(ASPECT_TAG);

                for (String key : aspectTag.getAllKeys()) {
                    try {
                        ResourceLocation aspectId = ResourceLocation.parse(key);
                        int value = aspectTag.getInt(key);

                        AspectStack aspectStack = new AspectStack(
                                MineArcanaRegistries.ASPECT.get(aspectId),
                                value
                        );

                        aspects.add(aspectStack);

                    } catch (Exception e) {
                        MineArcana.LOGGER.error("Failed loading aspect: {}", key, e);
                    }
                }
            }
            
            if (tag.contains(OUTPUT_ASPECT_TAG)) {
                CompoundTag aspectTag = tag.getCompound(OUTPUT_ASPECT_TAG);

                for (String key : aspectTag.getAllKeys()) {
                    try {
                        ResourceLocation aspectId = ResourceLocation.parse(key);
                        int value = aspectTag.getInt(key);

                        AspectStack aspectStack = new AspectStack(
                                MineArcanaRegistries.ASPECT.get(aspectId),
                                value
                        );

                        outputAspect.add(aspectStack);

                    } catch (Exception e) {
                        MineArcana.LOGGER.error("Failed loading output aspect: {}", key, e);
                    }
                }
            }
        } else {
            fluidStack = FluidStack.EMPTY;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        // Save fluid
        if (!fluidStack.isEmpty()) {
            tag.put(FLUID_TAG, fluidStack.save(registries));

            //Save Temperature
            tag.putInt(TEMPERATURE_BASE_TAG, baseTemp);
            tag.putInt(TEMPERATURE_MIN_TAG, minTemp);
            tag.putInt(TEMPERATURE_MAX_TAG, maxTemp);
            tag.putInt(TEMPERATURE_TAG, temperature);
            
            //Item
            tag.putString(OUTPUT_POTION_TAG, outputPotion.toString());

            // Save aspects
            if (!aspects.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                aspects.forEach(aspect -> compoundtag.putInt(
                        MineArcanaRegistries.ASPECT.getKey(aspect.getAspect()).toString(),
                        aspect.getAmount()
                ));

                tag.put(ASPECT_TAG, compoundtag);
            }
            if (!outputAspect.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                outputAspect.forEach(aspect -> compoundtag.putInt(
                        MineArcanaRegistries.ASPECT.getKey(aspect.getAspect()).toString(),
                        aspect.getAmount()
                ));

                tag.put(OUTPUT_ASPECT_TAG, compoundtag);
            }
        }
    }
}
