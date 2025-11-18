package net.sen.minearcana.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.sen.minearcana.MineArcana;
import net.sen.minearcana.common.registries.MineArcanaBlockEntites;
import net.sen.minearcana.common.registries.MineArcanaRegistries;
import net.sen.minearcana.common.utils.aspect.Aspect;
import net.sen.minearcana.common.utils.aspect.AspectStack;
import net.sen.minearcana.common.utils.pipelogic.IAspectHandler;
import net.sen.minearcana.data.aspects.MagicAspectDataLoader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AspectExtractorBlockEntity extends BlockEntity implements IAspectHandler {
    public final ItemStackHandler inventoryHandler = new ItemStackHandler(1) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            AspectExtractorBlockEntity.this.setChanged();
        }
    };

    private final List<AspectStack> aspects = new ArrayList<>();
    private int progress = 0;
    private int maxProgress = 72;

    public static final String ITEM_TAG = "Item";
    public static final String ASPECT_TAG = "Aspect";
    public static final String PROGRESS_TAG = "Progress";

    public AspectExtractorBlockEntity(BlockPos pos, BlockState blockState) {
        super(MineArcanaBlockEntites.ASPECT_EXTRACTOR.get(), pos, blockState);
    }

    public static void serverTicker(Level level, BlockPos pos, BlockState state, AspectExtractorBlockEntity blockEntity) {
        if (blockEntity.inventoryHandler.getStackInSlot(0).isEmpty()) return;

        if (blockEntity.inventoryHandler.getStackInSlot(0) != ItemStack.EMPTY) {
            //process the item
            blockEntity.progress++;
        } else {
            blockEntity.progress = 0;
        }

        if (blockEntity.progress >= blockEntity.maxProgress) {
            blockEntity.extractAspect();
            blockEntity.progress = 0;
        }
    }

    /**
     ITEM LOGIC
     */
    public void clearContents() {
        inventoryHandler.setStackInSlot(0, ItemStack.EMPTY);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventoryHandler.getSlots());
        for (int i = 0; i < inventoryHandler.getSlots(); i++) {
            inv.setItem(i, inventoryHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    public void extractAspect() {
        ItemStack itemStack = inventoryHandler.getStackInSlot(0);
        Map<String, Integer> aspectsFromItem = MagicAspectDataLoader.ITEM_ASPECT_VALUES.getOrDefault(itemStack.getItem(), Collections.emptyMap());
        if (aspectsFromItem.isEmpty()) return;
        for (Map.Entry<String, Integer> entry : aspectsFromItem.entrySet()) {
            Aspect aspect = MineArcanaRegistries.ASPECT.get(ResourceLocation.parse(entry.getKey()));
            if (aspect == null) continue;

            AspectStack stackToAdd = new AspectStack(aspect, entry.getValue() * itemStack.getCount());
            addAspect(stackToAdd);
        }

        clearContents();
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

    public List<AspectStack> getAspects() {
        return Collections.unmodifiableList(aspects);
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

    /**
     * SAVING / LOADING
     */
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.inventoryHandler.deserializeNBT(registries, tag.getCompound(ITEM_TAG));

        this.progress = Integer.parseInt(tag.getString(PROGRESS_TAG));

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
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.put(ITEM_TAG, this.inventoryHandler.serializeNBT(registries));
        tag.putInt(PROGRESS_TAG, progress);

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
    }

    @Override
    public AspectStack extractAspect(int amount) {
        AspectStack aspectStack = aspects.getFirst();
        AspectStack newStack = new AspectStack(aspectStack.getAspect(), amount);
        if (removeAspect(newStack)) return newStack;

        return AspectStack.empty();
    }

    @Override
    public boolean canInsert() {
        return false;
    }

    @Override
    public boolean canExtract() {
        return true;
    }
}
