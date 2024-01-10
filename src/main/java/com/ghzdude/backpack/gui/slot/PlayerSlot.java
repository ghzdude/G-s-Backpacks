package com.ghzdude.backpack.gui.slot;

import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.ghzdude.backpack.items.BackpackItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class PlayerSlot extends ModularSlot {

    public PlayerSlot(IItemHandler itemHandler, int index) {
        super(itemHandler, index);
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        var hasHandler = getStack().hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        return !hasHandler && super.canTakeStack(playerIn);
    }
}
