package com.ghzdude.backpack.handler;

import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.ItemHandlerHelper;

import java.io.IOException;

@SuppressWarnings({"OverrideOnly", "UnstableApiUsage"})
public class OversizedItemSlotSH extends SyncHandler {
    private final ModularSlot slot;
    public OversizedItemSlotSH(ModularSlot slot) {
        super();
        this.slot = slot;
    }

    @Override
    public void init(String key, GuiSyncManager syncManager) {
        super.init(key, syncManager);
        syncManager.getContainer().registerSlot(this.slot);
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
        this.slot.setEnabled(enabled);
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
    public void readOnServer(int id, PacketBuffer buf) throws IOException {
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
        ItemStack slotStack = this.slot.getStack();
        ItemStack stackToPut;
        if (!cursorStack.isEmpty() && !slotStack.isEmpty() && ItemHandlerHelper.canItemStacksStack(cursorStack, slotStack)) {
            stackToPut = cursorStack.copy();
            stackToPut.setCount(slotStack.getCount() + cursorStack.getCount());
            this.slot.putStack(stackToPut);
        } else if (slotStack.isEmpty()) {
            if (cursorStack.isEmpty()) return;
            stackToPut = cursorStack.copy();
            this.slot.putStack(stackToPut);
        }
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
