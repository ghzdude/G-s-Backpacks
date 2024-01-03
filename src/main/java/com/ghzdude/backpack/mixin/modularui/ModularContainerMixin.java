package com.ghzdude.backpack.mixin.modularui;

import com.cleanroommc.modularui.core.mixin.ContainerAccessor;
import com.cleanroommc.modularui.screen.ModularContainer;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.SlotGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("MissingUnique")
@Mixin(value = ModularContainer.class, remap = false)
public abstract class ModularContainerMixin extends Container{

    private static final int DROP_TO_WORLD = -999;
    private static final int LEFT_MOUSE = 0;
    private static final int RIGHT_MOUSE = 1;

    @Inject(method = "slotClick",
            at = @At(value = "HEAD"),
            cancellable = true)
    protected void slotClickMixin(int slotId, int mouseButton, ClickType clickTypeIn, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack returnable = ItemStack.EMPTY;
        InventoryPlayer inventoryplayer = player.inventory;

        if (clickTypeIn == ClickType.QUICK_CRAFT || acc().getDragEvent() != 0) {
            cir.setReturnValue(super.slotClick(slotId, mouseButton, clickTypeIn, player));
        }

        if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) &&
                (mouseButton == LEFT_MOUSE || mouseButton == RIGHT_MOUSE)) {
            if (slotId == DROP_TO_WORLD) {
                if (!inventoryplayer.getItemStack().isEmpty()) {
                    if (mouseButton == LEFT_MOUSE) {
                        player.dropItem(inventoryplayer.getItemStack(), true);
                        inventoryplayer.setItemStack(ItemStack.EMPTY);
                    }

                    if (mouseButton == RIGHT_MOUSE) {
                        player.dropItem(inventoryplayer.getItemStack().splitStack(1), true);
                    }
                }
                cir.setReturnValue(inventoryplayer.getItemStack());
            }
            if (clickTypeIn == ClickType.QUICK_MOVE) {
                Slot fromSlot = getSlot(slotId);

                if (!fromSlot.canTakeStack(player)) {
                    cir.setReturnValue(ItemStack.EMPTY);
                }

                returnable = this.transferStackInSlot(player, slotId);
            } else {
                Slot clickedSlot = getSlot(slotId);

                ItemStack slotStack = clickedSlot.getStack();
                ItemStack heldStack = inventoryplayer.getItemStack();

                if (slotStack.isEmpty()) {
                    if (!heldStack.isEmpty() && clickedSlot.isItemValid(heldStack)) {
                        int stackCount = mouseButton == LEFT_MOUSE ? heldStack.getCount() : 1;

                        if (stackCount > clickedSlot.getItemStackLimit(heldStack)) {
                            stackCount = clickedSlot.getItemStackLimit(heldStack);
                        }

                        clickedSlot.putStack(heldStack.splitStack(stackCount));
                    }
                } else if (clickedSlot.canTakeStack(player)) {
                    if (heldStack.isEmpty()) {
                        int toRemove = mouseButton == LEFT_MOUSE ? slotStack.getCount() : (slotStack.getCount() + 1) / 2;
                        toRemove = Math.min(toRemove, slotStack.getMaxStackSize());

                        inventoryplayer.setItemStack(slotStack.splitStack(toRemove));
                        clickedSlot.putStack(slotStack);

                        clickedSlot.onTake(player, inventoryplayer.getItemStack());
                    } else if (clickedSlot.isItemValid(heldStack)) {
                        if (slotStack.getItem() == heldStack.getItem() &&
                                slotStack.getMetadata() == heldStack.getMetadata() &&
                                ItemStack.areItemStackTagsEqual(slotStack, heldStack)) {
                            int stackCount = mouseButton == LEFT_MOUSE ? heldStack.getCount() : 1;

                            if (stackCount > clickedSlot.getItemStackLimit(heldStack) - slotStack.getCount()) {
                                stackCount = clickedSlot.getItemStackLimit(heldStack) - slotStack.getCount();
                            }

                            if (stackCount > heldStack.getMaxStackSize() - slotStack.getCount()) {
                                stackCount = heldStack.getMaxStackSize() - slotStack.getCount();
                            }

                            heldStack.shrink(stackCount);
                            slotStack.grow(stackCount);
                            clickedSlot.putStack(slotStack);

                        } else if (heldStack.getCount() <= clickedSlot.getItemStackLimit(heldStack)) {
                            clickedSlot.putStack(heldStack);
                            inventoryplayer.setItemStack(slotStack);
                        }
                    } else if (slotStack.getItem() == heldStack.getItem() &&
                            heldStack.getMaxStackSize() > 1 &&
                            (!slotStack.getHasSubtypes() || slotStack.getMetadata() == heldStack.getMetadata()) &&
                            ItemStack.areItemStackTagsEqual(slotStack, heldStack) && !slotStack.isEmpty()) {
                        int stackCount = slotStack.getCount();

                        if (stackCount + heldStack.getCount() <= heldStack.getMaxStackSize()) {
                            heldStack.grow(stackCount);
                            slotStack = clickedSlot.decrStackSize(stackCount);

                            if (slotStack.isEmpty()) {
                                clickedSlot.putStack(ItemStack.EMPTY);
                            }

                            clickedSlot.onTake(player, inventoryplayer.getItemStack());
                        }
                    }
                }
                clickedSlot.onSlotChanged();
            }
            this.detectAndSendChanges();
            cir.setReturnValue(returnable);
        }
    }

    @Shadow
    public abstract ContainerAccessor acc();

    @Shadow @Final private List<ModularSlot> slots;

    @Shadow @Final private List<ModularSlot> shiftClickSlots;

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer playerIn, int index) {
        ModularSlot slot = this.slots.get(index);
        if (!slot.isPhantom()) {
            ItemStack stack = slot.getStack();
            if (!stack.isEmpty()) {
                ItemStack remainder = stack.copy();
                int count = Math.min(remainder.getCount(), remainder.getMaxStackSize());
                remainder.setCount(count);
                remainder = g_s_Backpacks$transferItem(slot, remainder.copy());
                if (!remainder.isEmpty()) count -= remainder.getCount();
                stack.shrink(count);
                slot.putStack(stack);
            }
        }
        return ItemStack.EMPTY;
    }


    protected ItemStack g_s_Backpacks$transferItem(ModularSlot fromSlot, ItemStack fromStack) {
        @Nullable SlotGroup fromSlotGroup = fromSlot.getSlotGroup();
        List<ModularSlot> emptySlots = new ArrayList<>(shiftClickSlots.size());
        for (ModularSlot toSlot : this.shiftClickSlots) {
            SlotGroup slotGroup = Objects.requireNonNull(toSlot.getSlotGroup());
            if (slotGroup != fromSlotGroup && toSlot.isEnabled() && toSlot.isItemValid(fromStack)) {
                ItemStack toStack = toSlot.getStack().copy();
                if (toSlot.isPhantom()) {
                    if (toStack.isEmpty() || (ItemHandlerHelper.canItemStacksStack(fromStack, toStack) && toStack.getCount() < toSlot.getItemStackLimit(toStack))) {
                        toSlot.putStack(fromStack.copy());
                        return fromStack;
                    }
                } else if (toStack.isEmpty()) {
                    emptySlots.add(toSlot);
                } else if (ItemHandlerHelper.canItemStacksStack(fromStack, toStack)) {
                    int j = toStack.getCount() + fromStack.getCount();
                    int maxSize = toSlot.isIgnoreMaxStackSize() ?
                            toSlot.getSlotStackLimit() :
                            Math.min(toSlot.getSlotStackLimit(), fromStack.getMaxStackSize());

                    if (j <= maxSize) {
                        fromStack.setCount(0);
                        toStack.setCount(j);
                        toSlot.putStack(toStack);
                    } else if (toStack.getCount() < maxSize) {
                        fromStack.shrink(maxSize - toStack.getCount());
                        toStack.setCount(maxSize);
                        toSlot.putStack(toStack);
                    }

                    if (fromStack.isEmpty()) {
                        return fromStack;
                    }
                }
            }
        }
        for (ModularSlot emptySlot : emptySlots) {
            if (fromStack.getCount() > emptySlot.getSlotStackLimit()) {
                emptySlot.putStack(fromStack.splitStack(emptySlot.getSlotStackLimit()));
            } else {
                emptySlot.putStack(fromStack.splitStack(fromStack.getCount()));
            }
            if (fromStack.getCount() < 1) {
                break;
            }
        }
        return fromStack;
    }
}
