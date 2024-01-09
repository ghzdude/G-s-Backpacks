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

    public IItemHandlerModifiable getHandler() {
        return (IItemHandlerModifiable) getItemHandler();
    }


    @Override
    public boolean isIgnoreMaxStackSize() {
        return true;
    }
}
