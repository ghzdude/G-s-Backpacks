package com.ghzdude.backpack.gui.slot;

import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public class BackpackSlot extends ModularSlot {

    public BackpackSlot(IItemHandler itemHandler, int index) {
        super(itemHandler, index);
    }

    @Override
    public int getItemStackLimit(@NotNull ItemStack stack) {
        return getSlotStackLimit();
    }

    @Override
    public void putStack(@NotNull ItemStack stack) {
//        if (getHasStack()) {
//            int max = getSlotStackLimit();
//            int combined = getStack().getCount() + stack.getCount();
//            stack.setCount(Math.min(max, combined));
//        }
        super.putStack(stack);
    }

    public IItemHandlerModifiable getHandler() {
        return (IItemHandlerModifiable) getItemHandler();
    }


    @Override
    public boolean isIgnoreMaxStackSize() {
        return true;
    }

    @Override
    public boolean isPhantom() {
        return false;
    }
}
