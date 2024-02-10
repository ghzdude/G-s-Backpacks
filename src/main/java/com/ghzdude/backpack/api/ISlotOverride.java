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
    int LEFT_MOUSE = 0;
    int RIGHT_MOUSE = 1;

    default Result<ItemStack> transferStackInSlot(EntityPlayer playerIn, ModularSlot fromSlot, List<ModularSlot> slots) {
        return new Result<>(true);
    }

    default Result<ItemStack> slotClick(ModularSlot fromSlot, int mouseButton, ClickType clickTypeIn, EntityPlayer player, int dragEvent, int dragMode) {
        return new Result<>(true);
    }

    default Result<Boolean> canDragIntoSlot(Slot slotIn) {
        return new Result<>(true);
    }

    default Result<Boolean> canMergeSlot(ItemStack stack, Slot slotIn) {
        return new Result<>(true);
    }

    default Result<ItemStack> insertStack(ItemStack stack) {
        return new Result<>(true);
    }

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
