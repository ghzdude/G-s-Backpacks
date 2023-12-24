package com.ghzdude.backpack.slot;

import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class BackpackSlot extends ModularSlot {

    private final int STACK_LIMIT;
    private int stackAmount = 0;
    public BackpackSlot(IItemHandler itemHandler, int index, int stackLimit) {
        super(itemHandler, index);
        this.STACK_LIMIT = stackLimit;
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return this.STACK_LIMIT;
    }

    @Override
    public void putStack(@Nonnull ItemStack stack) {
        if (!isItemValid(stack)) return;
        stackAmount += stack.getCount();
        getHandler().setStackInSlot(getSlotIndex(), stack);
        getStack().setCount(stackAmount);
    }

    @Override
    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
        if (!stack.isEmpty()) return ItemStack.EMPTY;
        ItemStack returnable = getStack().copy();
        returnable.setCount(Math.min(returnable.getMaxStackSize(), stackAmount));
        if (stackAmount > getStack().getMaxStackSize()) {
            stackAmount -= returnable.getCount();
        } else {
            stackAmount = 0;
            getHandler().setStackInSlot(getSlotIndex(), ItemStack.EMPTY);
        }
        return returnable;
    }

    private IItemHandlerModifiable getHandler() {
        return (IItemHandlerModifiable) getItemHandler();
    }
}
