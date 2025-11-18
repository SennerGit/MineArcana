package net.sen.minearcana.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.recipes.ArcanaCauldronRecipe;
import net.sen.minearcana.common.recipes.ArcanaCauldronRecipeInput;
import net.sen.minearcana.common.recipes.AspectRequirement;
import net.sen.minearcana.common.registries.MineArcanaBlockEntites;
import net.sen.minearcana.common.registries.MineArcanaRecipes;
import net.sen.minearcana.common.registries.MineArcanaRegistries;
import net.sen.minearcana.common.utils.aspect.Aspect;
import net.sen.minearcana.common.utils.aspect.AspectStack;
import net.sen.minearcana.common.utils.pipelogic.IAspectHandler;
import net.sen.minearcana.config.MineArcanaConfig;
import net.sen.minearcana.data.aspects.MagicAspectDataLoader;

import java.util.*;
import java.util.stream.Collectors;

public class ArcanaCauldronBlockEntity extends BlockEntity implements IAspectHandler {
    public static final int MAX_FLUID = 3 * FluidType.BUCKET_VOLUME;
    private FluidStack fluidStack = FluidStack.EMPTY;
    private final List<AspectStack> aspects = new ArrayList<>();
    private int temperature; // Current temp
    private int baseTemp;
    private int maxTemp;
    private int sourceTemp; // heat from block below
    public ItemStack inputItem;

    private Item outputPotion;

    public static final String FLUID_TAG = "Fluid";
    public static final String ASPECT_TAG = "Aspects";
    public static final String TEMPERATURE_TAG = "Temperature";
    public static final String TEMPERATURE_BASE_TAG = "BaseTemperature";
    public static final String TEMPERATURE_MAX_TAG = "MaxTemperature";
    public static final String OUTPUT_POTION_TAG = "OutputPotion";
    public static final String OUTPUT_ASPECT_TAG = "OutputAspect";

    public ArcanaCauldronBlockEntity(BlockPos pos, BlockState blockState) {
        super(MineArcanaBlockEntites.ARCANA_CAULDRON.get(), pos, blockState);
    }

    /**
    TICKER
     */
    public static void serverTicker(Level level, BlockPos pos, BlockState state, ArcanaCauldronBlockEntity blockEntity) {
        if (level == null || level.isClientSide) return;

        blockEntity.updateHeatSource(level.getBlockState(pos.below()));
        blockEntity.updateTemperature();

        // Optional: auto-check recipes
        Optional<ArcanaCauldronRecipe> recipe = blockEntity.findMatchingRecipe(blockEntity.inputItem);
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
                if (parts.length != 3) continue; // skip invalid entries

                ResourceLocation key = ResourceLocation.tryParse(parts[0]);
                if (!fluidId.equals(key)) continue;

                int parsedBase;
                int parsedMax;

                try {
                    parsedBase = Integer.parseInt(parts[1]);
                    parsedMax = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    MineArcana.LOGGER.error("Invalid numbers for fluid");
                    continue; // skip invalid numbers
                }

                fluidStack = new FluidStack(stack.getFluid(), Math.min(stack.getAmount(), MAX_FLUID));
                baseTemp = parsedBase;
                maxTemp = parsedMax;
                temperature = baseTemp;

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
            temperature = 0;
            maxTemp = 0;
            baseTemp = 0;
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

        // Calculate desired temperature
        int targetTemp = baseTemp + sourceTemp;

        // Never exceed the fluid's hard cap
        if (targetTemp > maxTemp) {
            targetTemp = maxTemp;
        }

        // Move toward target temperature
        if (temperature < targetTemp) {
            temperature++;
        } else if (temperature > targetTemp) {
            temperature--;
        }

        setChanged();
    }

    public int getTemperature() {
        return temperature;
    }

    public int getBaseTemperature() {
        return baseTemp;
    }

    public int getMaxTemperature() {
        return maxTemp;
    }

    /**
    ASPECTS
     */
    public boolean processItem(ItemStack stack) {
        if (fluidStack.isEmpty() || fluidStack.getAmount() < FluidType.BUCKET_VOLUME) return false;
        if (temperature < 100) return false;

        Map<String, Integer> aspectsFromItem = MagicAspectDataLoader.ITEM_ASPECT_VALUES.getOrDefault(stack.getItem(), Collections.emptyMap());
        if (aspectsFromItem.isEmpty()) return false;

        for (Map.Entry<String, Integer> entry : aspectsFromItem.entrySet()) {
            Aspect aspect = MineArcanaRegistries.ASPECT.get(ResourceLocation.parse(entry.getKey()));
            if (aspect == null) continue;

            AspectStack stackToAdd = new AspectStack(aspect, entry.getValue() * stack.getCount());
            addAspect(stackToAdd);
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
    public void processAspectsFromItemsInside() {
        if (inputItem == null || inputItem.isEmpty()) return;

        // Only process if fluid is present and temperature is sufficient
        if (fluidStack.isEmpty() || fluidStack.getAmount() < FluidType.BUCKET_VOLUME) return;
        if (temperature < 100) return;

        Map<String, Integer> aspectsFromItem = MagicAspectDataLoader.ITEM_ASPECT_VALUES.getOrDefault(inputItem.getItem(), Collections.emptyMap());
        if (aspectsFromItem.isEmpty()) return;

        for (Map.Entry<String, Integer> entry : aspectsFromItem.entrySet()) {
            ResourceLocation key;
            try {
                key = ResourceLocation.parse(entry.getKey());
            } catch (Exception e) {
                MineArcana.LOGGER.error("Invalid aspect key in data: {}", entry.getKey());
                continue;
            }

            Aspect aspect = MineArcanaRegistries.ASPECT.get(key);
            if (aspect == null) continue;

            AspectStack aspectStack = new AspectStack(aspect, entry.getValue() * inputItem.getCount());
            addAspect(aspectStack);
        }

        // Clear inputItem so it doesn’t get processed again
        inputItem = ItemStack.EMPTY;

        // Mark block changed to sync with client
        setChanged();
    }

    public ArcanaCauldronRecipeInput buildRecipeInputFor(ItemStack heldItem) {
        if (heldItem == null || heldItem.isEmpty()) {
            return null; // or Optional.empty(), depending on your usage
        }

        // Convert runtime AspectStack -> AspectRequirement
        List<AspectRequirement> reqs = aspects.stream()
                .map(as -> {
                    ResourceLocation key = MineArcanaRegistries.ASPECT.getKey(as.getAspect());
                    if (key == null) key = ResourceLocation.fromNamespaceAndPath(MineArcana.MODID, "empty");
                    return new AspectRequirement(key, as.getAmount());
                })
                .collect(Collectors.toList());

        // Use a copy of the held item as the "input" stack
        return new ArcanaCauldronRecipeInput(heldItem.copy(), fluidStack.copy(), reqs, temperature);
    }

    public Optional<ArcanaCauldronRecipe> findMatchingRecipe(ItemStack heldItem) {
        if (level == null) return Optional.empty();
        if (level.isClientSide) return Optional.empty();

        ArcanaCauldronRecipeInput input = buildRecipeInputFor(heldItem);
        if (input == null) return Optional.empty();

        List<RecipeHolder<ArcanaCauldronRecipe>> all = level.getRecipeManager().getAllRecipesFor(MineArcanaRecipes.ARCANA_CAULDRON_RECIPE_TYPE.get());
        List<ArcanaCauldronRecipe> allClean = new ArrayList<>();
        all.forEach(arcanaCauldronRecipeRecipeHolder -> {
            allClean.add(arcanaCauldronRecipeRecipeHolder.value());
        });
        return allClean.stream().filter(r -> r.matches(input, level)).findFirst();
    }

    public ItemStack extractPotion(ArcanaCauldronRecipe recipe) {
        if (recipe == null || fluidStack.isEmpty()) return ItemStack.EMPTY;

        ArcanaCauldronRecipeInput rIn = recipe.input();
        // ensure fluid matches and enough amount
        if (!fluidStack.getFluid().equals(rIn.fluid().getFluid())) return ItemStack.EMPTY;
        int neededFluid = rIn.fluid().getAmount(); // recipe's configured fluid amount per craft
        if (fluidStack.getAmount() < neededFluid) return ItemStack.EMPTY;

        // check aspects again (BE's aspects must have enough)
        for (AspectRequirement req : rIn.aspects()) {
            // find matching stored aspect
            boolean ok = false;
            for (AspectStack stored : aspects) {
                ResourceLocation storedKey = MineArcanaRegistries.ASPECT.getKey(stored.getAspect());
                if (storedKey != null && storedKey.equals(req.aspectId()) && stored.getAmount() >= req.amount()) {
                    ok = true;
                    break;
                }
            }
            if (!ok) return ItemStack.EMPTY;
        }

        // consume aspects
        for (AspectRequirement req : rIn.aspects()) {
            removeAspect(new net.sen.minearcana.common.utils.aspect.AspectStack(
                    MineArcanaRegistries.ASPECT.get(req.aspectId()), // careful: this resolves in runtime registry
                    req.amount()
            ));
        }

        // consume fluid
        removeWater(new net.neoforged.neoforge.fluids.FluidStack(fluidStack.getFluid(), neededFluid));

        setChanged();
        return recipe.getOutput().copy();
    }

    /**
     * SAVING / LOADING
     */
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        // --- FLUID ---
        if (tag.contains(FLUID_TAG)) {
            fluidStack = FluidStack.parseOptional(registries, tag.getCompound(FLUID_TAG));
        } else {
            fluidStack = FluidStack.EMPTY;
        }

        // --- TEMPERATURES ---
        baseTemp = tag.getInt(TEMPERATURE_BASE_TAG);
        maxTemp = tag.getInt(TEMPERATURE_MAX_TAG);
        temperature = tag.getInt(TEMPERATURE_TAG);

        // --- OUTPUT POTION ---
        if (tag.contains(OUTPUT_POTION_TAG)) {
            ResourceLocation id = ResourceLocation.tryParse(tag.getString(OUTPUT_POTION_TAG));
            outputPotion = id != null ? BuiltInRegistries.ITEM.get(id) : null;
        } else {
            outputPotion = null;
        }

        // --- ASPECTS ---
        aspects.clear();

        if (tag.contains(ASPECT_TAG)) {
            CompoundTag aspectTag = tag.getCompound(ASPECT_TAG);

            for (String key : aspectTag.getAllKeys()) {
                try {
                    ResourceLocation rl = ResourceLocation.tryParse(key);
                    if (rl == null) {
                        MineArcana.LOGGER.error("Invalid aspect ID: {}", key);
                        continue;
                    }

                    int amount = aspectTag.getInt(key);
                    Aspect aspect = MineArcanaRegistries.ASPECT.get(rl);

                    if (aspect != null) {
                        aspects.add(new AspectStack(aspect, amount));
                    } else {
                        MineArcana.LOGGER.error("Unknown aspect registry ID: {}", rl);
                    }

                } catch (Exception e) {
                    MineArcana.LOGGER.error("Failed loading aspect '{}'", key, e);
                }
            }
        }

        if (tag.contains(OUTPUT_ASPECT_TAG)) {
            CompoundTag aspectTag = tag.getCompound(OUTPUT_ASPECT_TAG);

            for (String key : aspectTag.getAllKeys()) {
                try {
                    ResourceLocation rl = ResourceLocation.tryParse(key);
                    if (rl == null) {
                        MineArcana.LOGGER.error("Invalid output aspect ID: {}", key);
                        continue;
                    }

                    int amount = aspectTag.getInt(key);
                    Aspect aspect = MineArcanaRegistries.ASPECT.get(rl);

                } catch (Exception e) {
                    MineArcana.LOGGER.error("Failed loading output aspect '{}'", key, e);
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        // --- FLUID ---
        if (fluidStack != null && !fluidStack.isEmpty()) {
            tag.put(FLUID_TAG, fluidStack.save(registries));
        }

        // --- TEMPERATURES ---
        tag.putInt(TEMPERATURE_BASE_TAG, baseTemp);
        tag.putInt(TEMPERATURE_MAX_TAG, maxTemp);
        tag.putInt(TEMPERATURE_TAG, temperature);

        // --- OUTPUT POTION ---
        if (outputPotion != null) {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(outputPotion);
            if (id != null) {
                tag.putString(OUTPUT_POTION_TAG, id.toString());
            }
        }

        // --- ASPECTS ---
        if (!aspects.isEmpty()) {
            CompoundTag aspectTag = new CompoundTag();

            for (AspectStack stack : aspects) {
                Aspect aspect = stack.getAspect();
                ResourceLocation key = MineArcanaRegistries.ASPECT.getKey(aspect);

                if (key != null) {
                    aspectTag.putInt(key.toString(), stack.getAmount());
                }
            }

            tag.put(ASPECT_TAG, aspectTag);
        }
    }

    /**
     * IAspectHandler Implementation
     */
    @Override
    public AspectStack getAspect() {
        return aspects.getFirst();
    }

    @Override
    public void insertAspect(AspectStack stack) {
        addAspect(stack);
    }

    @Override
    public AspectStack extractAspect(int amount) {
        return null;
    }

    @Override
    public boolean canInsert() {
        return true;
    }

    @Override
    public boolean canExtract() {
        return false;
    }
}
