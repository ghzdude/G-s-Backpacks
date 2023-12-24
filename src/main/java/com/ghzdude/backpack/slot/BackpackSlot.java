package com.ghzdude.backpack.slot;

import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class BackpackSlot extends ModularSlot {

    public BackpackSlot(IItemHandler itemHandler, int index) {
        super(itemHandler, index);
    }

    @Override
    public void putStack(@Nonnull ItemStack stack) {
        super.putStack(stack);
    }

    @Override
    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
        return super.onTake(thePlayer, stack);
    }
}
