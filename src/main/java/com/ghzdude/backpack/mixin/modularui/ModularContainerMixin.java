package com.ghzdude.backpack.mixin.modularui;

import com.cleanroommc.modularui.screen.ModularContainer;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.ghzdude.backpack.api.ISlotOverride;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ModularContainer.class, remap = false)
public abstract class ModularContainerMixin extends Container {

    @Shadow @Final private List<ModularSlot> slots;

    @Shadow @Final private List<ModularSlot> shiftClickSlots;

    @Unique
    @Inject(method = "slotClick", at = @At(value = "HEAD"), cancellable = true)
    private void slotClick(int slotId, int mouseButton, ClickType clickTypeIn, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        if (backpacks$validSlot(slotId) && this.slots.get(slotId) instanceof ISlotOverride slotOverride) {
            var result = slotOverride.slotClick(mouseButton, clickTypeIn, player, getDragEvent(mouseButton), extractDragMode(mouseButton));
            if (result.shouldReturn()) {
                cir.setReturnValue(result.getReturnable());
            }
        }
    }

    @Unique
    @Inject(method = "transferStackInSlot", at = @At(value = "HEAD"), cancellable = true)
    private void transferStackInSlot(EntityPlayer playerIn, int index, CallbackInfoReturnable<ItemStack> cir) {
        if (backpacks$validSlot(index) && this.slots.get(index) instanceof ISlotOverride slotOverride) {
            var result = slotOverride.transferStackInSlot(playerIn, this.shiftClickSlots);
            if (result.shouldReturn()) {
                cir.setReturnValue(result.getReturnable());
            }
        }
    }

    @Override
    public boolean canDragIntoSlot(Slot slotIn) {
        if (slotIn instanceof ISlotOverride slotOverride) {
            var result = slotOverride.canDragIntoSlot(slotIn);
            if (result.shouldReturn()) {
                return Boolean.TRUE.equals(result.getReturnable());
            }
        }
        return super.canDragIntoSlot(slotIn);
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        if (slotIn instanceof ISlotOverride slotOverride) {
            var result = slotOverride.canMergeSlot(stack, slotIn);
            if (result.shouldReturn()) {
                return Boolean.TRUE.equals(result.getReturnable());
            }
        }
        return super.canMergeSlot(stack, slotIn);
    }

    @Inject(method = "transferItem",
            cancellable = true,
            at = {
            @At(value = "INVOKE_ASSIGN",
                target = "Lnet/minecraft/item/ItemStack;getCount()I",
                ordinal = 1,
                shift = At.Shift.BEFORE),
            @At(value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;getCount()I",
                ordinal = 5,
                shift = At.Shift.BEFORE)
    })
    private void insertStack(ModularSlot fromSlot, ItemStack fromStack, CallbackInfoReturnable<ItemStack> cir,
                             @Local(ordinal = 1) ModularSlot toSlot) {
        if (toSlot instanceof ISlotOverride slotOverride) {
            var result = slotOverride.insertStack(fromStack);
            if (result.shouldReturn()) {
                var stack = result.getReturnable();
                cir.setReturnValue(stack == null ? ItemStack.EMPTY : stack);
            }
        }
    }

    @Unique
    private boolean backpacks$validSlot(int slot) {
        return slot >= 0 && slot < this.slots.size();
    }
}
