package com.ghzdude.backpack.handler;

import com.cleanroommc.modularui.utils.ItemStackItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class BackpackHandler extends ItemStackItemHandler {

    private final int MAX_AMOUNT;
    private static final String TRUE_AMOUNT = "TrueAmount";
    public BackpackHandler(ItemStack container, int slots, int maxAmount) {
        super(container, slots);
        this.MAX_AMOUNT = maxAmount;
    }

    @Override
    public @Nonnull ItemStack getStackInSlot(int slot) {
        ItemStack stack = super.getStackInSlot(slot);
        NBTTagCompound item = getCompound(slot);
        if (!item.hasKey(TRUE_AMOUNT)) {
            item.setInteger(TRUE_AMOUNT, stack.getCount());
        }
        int trueAmount = getCompound(slot).getInteger(TRUE_AMOUNT);
        stack.setCount(trueAmount);
        return stack;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
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
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return getSlotLimit(slot);
    }

    protected NBTTagCompound getCompound(int slot) {
        return (NBTTagCompound) getItemsNbt().get(slot);
    }
}
