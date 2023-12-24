package com.ghzdude.backpack.handler;

import com.cleanroommc.modularui.utils.ItemStackItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.NotNull;

public class BackpackHandler extends ItemStackItemHandler {

    private final int MAX_AMOUNT;
    private static final String TRUE_AMOUNT = "TrueAmount";
    public BackpackHandler(ItemStack container, int slots, int maxAmount) {
        super(container, slots);
        this.MAX_AMOUNT = maxAmount;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        ItemStack stack = super.getStackInSlot(slot);
        NBTTagCompound item = getCompound(slot);
        if (!item.hasKey(TRUE_AMOUNT)) {
            item.setInteger(TRUE_AMOUNT, stack.getCount());
        } else {
            stack.setCount(getCompound(slot).getInteger(TRUE_AMOUNT));
        }
        return stack;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        NBTTagCompound item = getCompound(slot);
        int size = Math.min(stack.getCount() + getStackInSlot(slot).getCount(), MAX_AMOUNT);
        item.setInteger(TRUE_AMOUNT, size);
        super.setStackInSlot(slot, stack);
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
