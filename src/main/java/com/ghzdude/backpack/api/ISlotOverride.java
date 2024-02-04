package com.ghzdude.backpack.api;

import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ISlotOverride {
    Result<ItemStack> transferStackInSlot(EntityPlayer playerIn, int index, List<ModularSlot> slots);

    Result<ItemStack> slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player, int dragEvent, int dragMode);

    Result<Boolean> canDragIntoSlot(Slot slotIn);

    Result<Void> onContainerClosed(EntityPlayer playerIn);

    Result<Boolean> canMergeSlot(ItemStack stack, Slot slotIn);

    class Result<T> {

        @NotNull
        private final boolean callSuper;

        @Nullable
        private final T returnable;

        public Result(@NotNull boolean callSuper, @Nullable T returnable) {
            this.callSuper = callSuper;
            this.returnable = returnable;
        }

        public Result(boolean callSuper) {
            this(callSuper, null);
        }

        public boolean callSuper() {
            return callSuper;
        }

        public T getReturnable() {
            return returnable;
        }
    }
}
