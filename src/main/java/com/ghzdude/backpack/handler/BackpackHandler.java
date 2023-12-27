package com.ghzdude.backpack.handler;

import com.cleanroommc.modularui.utils.ItemStackItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;

public class BackpackHandler extends ItemStackItemHandler {
    private final NonNullList<ItemStack> stacks;
    private final int MAX_AMOUNT;
    private static final String COUNT = "Count";

    public BackpackHandler(ItemStack container, int slots, int maxAmount) {
        super(container, slots);
        this.MAX_AMOUNT = maxAmount;
        this.stacks = NonNullList.withSize(slots, ItemStack.EMPTY);
        for (int i = 0; i < slots; i++) {
            this.stacks.set(i, getStackInSlot(i));
        }
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        NBTTagCompound item = getCompound(slot);
        int size = item.getInteger(COUNT);
        ItemStack stack = super.getStackInSlot(slot);
        stack.setCount(size);

        if (!ItemStack.areItemStacksEqual(stack, stacks.get(slot)))
            stacks.set(slot, stack);

        return stacks.get(slot);
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        super.setStackInSlot(slot, stack);
        stacks.set(slot, stack);
        NBTTagCompound item = getCompound(slot);
        int size = Math.min(stacks.get(slot).getCount(), MAX_AMOUNT);
        if (size > 0) item.setInteger(COUNT, size);
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
