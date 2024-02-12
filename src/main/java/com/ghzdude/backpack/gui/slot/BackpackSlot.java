package com.ghzdude.backpack.gui.slot;

import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.SlotGroup;
import com.ghzdude.backpack.api.backpacks$SlotOverride;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BackpackSlot extends BogoSlot implements backpacks$SlotOverride {

    public BackpackSlot(IItemHandler itemHandler, int index) {
        super(itemHandler, index);
    }

    @Override
    public int getItemStackLimit(@NotNull ItemStack stack) {
        return getSlotStackLimit();
    }

    @Override
    public boolean isIgnoreMaxStackSize() {
        return true;
    }

    @Override
    public Result<ItemStack> slotClick(int mouseButton, ClickType clickTypeIn, EntityPlayer player, int dragEvent, int dragMode) {
        ItemStack returnable = ItemStack.EMPTY;
        InventoryPlayer inventoryplayer = player.inventory;

        if ((clickTypeIn == ClickType.PICKUP) &&
                (mouseButton == LEFT_MOUSE || mouseButton == RIGHT_MOUSE)) {
            ItemStack slotStack = getStack().copy();
            ItemStack heldStack = inventoryplayer.getItemStack().copy();

            if (slotStack.isEmpty()) {
                if (!heldStack.isEmpty() && isItemValid(heldStack)) {
                    int stackCount = mouseButton == LEFT_MOUSE ? heldStack.getCount() : 1;

                    if (stackCount > getItemStackLimit(heldStack)) {
                        stackCount = getItemStackLimit(heldStack);
                    }

                    putStack(heldStack.splitStack(stackCount));
                    inventoryplayer.setItemStack(heldStack);
                }
            } else if (canTakeStack(player)) {
                if (heldStack.isEmpty() && !slotStack.isEmpty()) {

                    int toRemove = Math.min(slotStack.getMaxStackSize(),
                            mouseButton == LEFT_MOUSE ?
                                slotStack.getCount() :
                                (slotStack.getCount() + 1) / 2);

                    inventoryplayer.setItemStack(slotStack.splitStack(toRemove));
                    putStack(slotStack);

                    onTake(player, inventoryplayer.getItemStack());
                } else if (isItemValid(heldStack)) {
                    if (slotStack.getItem() == heldStack.getItem() &&
                            slotStack.getMetadata() == heldStack.getMetadata() &&
                            ItemStack.areItemStackTagsEqual(slotStack, heldStack)) {
                        int stackCount = mouseButton == LEFT_MOUSE ? heldStack.getCount() : 1;

                        if (stackCount > getItemStackLimit(heldStack) - slotStack.getCount()) {
                            stackCount = getItemStackLimit(heldStack) - slotStack.getCount();
                        }

                        heldStack.shrink(stackCount);
                        slotStack.grow(stackCount);
                        putStack(slotStack);
                        inventoryplayer.setItemStack(heldStack);

                    } else if (heldStack.getCount() <= getItemStackLimit(heldStack)) {
                        putStack(heldStack);
                        inventoryplayer.setItemStack(slotStack);
                    }
                } else if (slotStack.getItem() == heldStack.getItem() &&
                        heldStack.getMaxStackSize() > 1 &&
                        (!slotStack.getHasSubtypes() || slotStack.getMetadata() == heldStack.getMetadata()) &&
                        ItemStack.areItemStackTagsEqual(slotStack, heldStack) && !slotStack.isEmpty()) {
                    int stackCount = slotStack.getCount();

                    if (stackCount + heldStack.getCount() <= heldStack.getMaxStackSize()) {
                        heldStack.grow(stackCount);
                        slotStack = decrStackSize(stackCount);

                        if (slotStack.isEmpty()) {
                            putStack(ItemStack.EMPTY);
                        }

                        onTake(player, inventoryplayer.getItemStack());
                    }
                }
                onSlotChanged();
            }
            return new Result<>(false, returnable);
        }
        return new Result<>(true);
    }

    @Override
    public Result<ItemStack> transferStackInSlot(EntityPlayer playerIn, List<ModularSlot> shiftClickSlots) {
        @Nullable SlotGroup fromSlotGroup = getSlotGroup();
        ItemStack fromStack = getStack().copy();
        List<ModularSlot> emptySlots = new ArrayList<>();

        for (ModularSlot toSlot : shiftClickSlots) {
            SlotGroup slotGroup = Objects.requireNonNull(toSlot.getSlotGroup());
            if (slotGroup != fromSlotGroup && toSlot.isEnabled() && toSlot.isItemValid(fromStack)) {
                ItemStack toStack = toSlot.getStack().copy();
                if (ItemHandlerHelper.canItemStacksStack(fromStack, toStack)) {
                    int combined = toStack.getCount() + fromStack.getCount();
                    int maxSize = toSlot.getSlotStackLimit();

                    if (combined <= maxSize) {
                        fromStack.setCount(0);
                        toStack.setCount(combined);
                        toSlot.putStack(toStack);
                    } else if (toStack.getCount() < maxSize) {
                        fromStack.shrink(maxSize - toStack.getCount());
                        toStack.setCount(maxSize);
                        toSlot.putStack(toStack);
                    }

                    if (fromStack.isEmpty()) {
                        return new Result<>(false, ItemStack.EMPTY);
                    }
                }
                else if (toStack.isEmpty()) {
                    emptySlots.add(toSlot);
                }
            }
        }
        for (ModularSlot emptySlot : emptySlots) {
            if (fromStack.getCount() > fromStack.getMaxStackSize()) {
                emptySlot.putStack(fromStack.splitStack(fromStack.getMaxStackSize()));
            }
            else {
                emptySlot.putStack(fromStack.splitStack(fromStack.getCount()));
            }
            putStack(fromStack);
            break;
        }
        return new Result<>(false, fromStack);
    }

    @Override
    public Result<ItemStack> insertStack(ItemStack stack) {
        ItemStack toStack = getStack().copy();
        int combined = toStack.getCount() + stack.getCount();
        int maxSize = getSlotStackLimit();

        if (combined <= maxSize) {
            stack.setCount(0);
            toStack.setCount(combined);
            putStack(toStack);
        } else if (toStack.getCount() < maxSize) {
            stack.shrink(maxSize - toStack.getCount());
            toStack.setCount(maxSize);
            putStack(toStack);
        }

        return new Result<>(false, stack);
    }
}
