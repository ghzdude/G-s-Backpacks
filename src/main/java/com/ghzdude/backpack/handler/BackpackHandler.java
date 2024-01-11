package com.ghzdude.backpack.handler;

import com.cleanroommc.modularui.utils.ItemStackItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

public class BackpackHandler extends ItemStackItemHandler {
    private final int MAX_AMOUNT;
    private static final String COUNT = "Count";

    public BackpackHandler(ItemStack container, int slots, int maxAmount) {
        super(container, slots);
        this.MAX_AMOUNT = maxAmount;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        ItemStack existing = getStackInSlot(slot);

        int limit = getStackLimit(slot, stack);

        if (!existing.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0) return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                setStackInSlot(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
                setStackInSlot(slot, existing);
            }
            onContentsChanged(slot);
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        NBTTagCompound item = getCompound(slot);
        int size = item.getInteger(COUNT);
        ItemStack stack = super.getStackInSlot(slot);
        stack.setCount(size);
        return stack;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        super.setStackInSlot(slot, stack);
        NBTTagCompound item = getCompound(slot);
        item.setInteger(COUNT, stack.getCount());
    }

    @Override
    public int getSlotLimit(int slot) {
        return MAX_AMOUNT;
    }

    @Override
    protected int getStackLimit(int slot, @NotNull ItemStack stack) {
        return getSlotLimit(slot);
    }

    protected NBTTagCompound getCompound(int slot) {
        return (NBTTagCompound) getItemsNbt().get(slot);
    }
}
