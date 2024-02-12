package com.ghzdude.backpack.gui.slot;

import com.cleanroommc.bogosorter.api.ISlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.IItemHandler;

@Optional.Interface(modid = "bogosort", iface = "com.cleanroommc.bogosorter.api.ISlot")
public class BogoSlot extends ModularSlot implements ISlot {

    public BogoSlot(IItemHandler itemHandler, int index) {
        super(itemHandler, index);
    }

    public BogoSlot(IItemHandler itemHandler, int index, boolean phantom) {
        super(itemHandler, index, phantom);
    }

    @Override
    public Slot bogo$getRealSlot() {
        return this;
    }

    @Override
    public int bogo$getX() {
        return this.xPos;
    }

    @Override
    public int bogo$getY() {
        return this.yPos;
    }

    @Override
    public int bogo$getSlotNumber() {
        return this.slotNumber;
    }

    @Override
    public int bogo$getSlotIndex() {
        return this.getSlotIndex();
    }

    @Override
    public IInventory bogo$getInventory() {
        return this.inventory;
    }

    @Override
    public void bogo$putStack(ItemStack itemStack) {
        putStack(itemStack);
    }

    @Override
    public ItemStack bogo$getStack() {
        return getStack();
    }

    @Override
    public int bogo$getMaxStackSize(ItemStack itemStack) {
        return getItemStackLimit(itemStack);
    }

    @Override
    public int bogo$getItemStackLimit(ItemStack itemStack) {
        return getItemStackLimit(itemStack);
    }

    @Override
    public boolean bogo$isEnabled() {
        return isEnabled();
    }

    @Override
    public boolean bogo$isItemValid(ItemStack stack) {
        return isItemValid(stack);
    }

    @Override
    public boolean bogo$canTakeStack(EntityPlayer player) {
        return canTakeStack(player);
    }

    @Override
    public void bogo$onSlotChanged() {
        onSlotChanged();
    }

    @Override
    public void bogo$onSlotChanged(ItemStack oldItem, ItemStack newItem) {
        onSlotChange(oldItem, newItem);
    }

    @Override
    public ItemStack bogo$onTake(EntityPlayer player, ItemStack itemStack) {
        return onTake(player, itemStack);
    }

}
