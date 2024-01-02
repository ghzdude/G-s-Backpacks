package com.ghzdude.backpack.handler;

import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.value.sync.ItemSlotSH;
import com.ghzdude.backpack.gui.slot.BackpackSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.ItemHandlerHelper;

@SuppressWarnings({"UnstableApiUsage"})
public class OversizedItemSlotSH extends ItemSlotSH {
    public OversizedItemSlotSH(BackpackSlot slot) {
        super(slot);
    }

    @Override
    public void detectAndSendChanges(boolean init) {
//        ItemStack itemStack = getSlot().getStack();
//        if (itemStack.isEmpty()) return;
//        boolean onlyAmountChanged = false;
//        if (init) {
//            getSlot().onSlotChangedReal(itemStack, onlyAmountChanged, false, init);
//            if (onlyAmountChanged) {
//                this.lastStoredItem.setCount(itemStack.getCount());
//            } else {
//                this.lastStoredItem = itemStack.isEmpty() ? ItemStack.EMPTY : itemStack.copy();
//            }
//            final boolean finalOnlyAmountChanged = onlyAmountChanged;
//            syncToClient(1, buffer -> {
//                buffer.writeBoolean(finalOnlyAmountChanged);
//                buffer.writeItemStack(itemStack);
//                buffer.writeBoolean(init);
//            });
//        }
    }

    public void setEnabled(boolean enabled, boolean sync) {
        getSlot().setEnabled(enabled);
        if (sync) {
            sync(4, buffer -> buffer.writeBoolean(enabled));
        }
    }

    @Override
    public void readOnClient(int id, PacketBuffer buf) {
        if (id == 4) {
            setEnabled(buf.readBoolean(), false);
        }
    }

    @Override
    public void readOnServer(int id, PacketBuffer buf) {
        if (id == 2) {
            phantomClick(MouseData.readPacket(buf));
        } else if (id == 3) {
            phantomScroll(MouseData.readPacket(buf));
        } else if (id == 4) {
            setEnabled(buf.readBoolean(), false);
        }
    }

    protected void phantomClick(MouseData mouseData) {
        ItemStack cursorStack = getSyncManager().getCursorItem();
        ItemStack slotStack = getSlot().getStack();
        ItemStack stackToPut;
        if (mouseData.shift && !slotStack.isEmpty()) {
            stackToPut = slotStack.copy();
            int removed = Math.min(slotStack.getCount(), slotStack.getMaxStackSize());

            stackToPut.setCount(removed);
            slotStack.shrink(removed);

            getSlot().getHandler().setStackInSlot(getSlot().getSlotIndex(), slotStack);
            getSyncManager().getPlayer().inventory.storeItemStack(stackToPut);
        } else if (!cursorStack.isEmpty() && !slotStack.isEmpty()) {
            if (ItemHandlerHelper.canItemStacksStack(cursorStack, slotStack)) {
                stackToPut = cursorStack.copy();
                cursorStack.shrink(stackToPut.getCount());

                getSlot().putStack(stackToPut);
                getSyncManager().setCursorItem(cursorStack);
            }
        } else if (slotStack.isEmpty() && !cursorStack.isEmpty()) {
            getSlot().putStack(cursorStack.copy());
            getSyncManager().setCursorItem(ItemStack.EMPTY);
        } else if (cursorStack.isEmpty() && !slotStack.isEmpty()) {
            stackToPut = slotStack.copy();
            int removed = Math.min(slotStack.getCount(), slotStack.getMaxStackSize());
            stackToPut.setCount(removed);
            slotStack.shrink(removed);

            getSyncManager().setCursorItem(stackToPut);
            getSlot().putStack(slotStack);
        }
    }

    @Override
    public BackpackSlot getSlot() {
        return (BackpackSlot) super.getSlot();
    }

    protected void phantomScroll(MouseData mouseData) {
//        ItemStack currentItem = this.slot.getStack();
//        int amount = mouseData.mouseButton;
//        if (mouseData.shift) amount *= 4;
//        if (mouseData.ctrl) amount *= 16;
//        if (mouseData.alt) amount *= 64;
//        if (amount > 0 && currentItem.isEmpty() && !this.lastStoredPhantomItem.isEmpty()) {
//            ItemStack stackToPut = this.lastStoredPhantomItem.copy();
//            stackToPut.setCount(amount);
//            this.slot.putStack(stackToPut);
//        } else {
//            incrementStackCount(amount);
//        }
    }

    public void incrementStackCount(int amount) {
//        ItemStack stack = getSlot().getStack();
//        if (stack.isEmpty()) {
//            return;
//        }
//        int oldAmount = stack.getCount();
//        if (amount < 0) {
//            amount = Math.max(0, oldAmount + amount);
//        } else {
//            if (Integer.MAX_VALUE - amount < oldAmount) {
//                amount = Integer.MAX_VALUE;
//            } else {
//                int maxSize = getSlot().getSlotStackLimit();
//                if (!this.slot.isIgnoreMaxStackSize() && stack.getMaxStackSize() < maxSize) {
//                    maxSize = stack.getMaxStackSize();
//                }
//                amount = Math.min(oldAmount + amount, maxSize);
//            }
//        }
//        if (oldAmount != amount) {
//            stack = stack.copy();
//            stack.setCount(amount);
//            getSlot().putStack(stack);
//        }
    }
}
