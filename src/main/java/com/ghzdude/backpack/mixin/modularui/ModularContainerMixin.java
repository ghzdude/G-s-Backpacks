package com.ghzdude.backpack.mixin.modularui;

import com.cleanroommc.modularui.screen.ModularContainer;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ModularContainer.class, remap = false)
public abstract class ModularContainerMixin extends Container {

    @WrapOperation(method = "slotClick", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getCount()I", ordinal = 1),
            @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getCount()I", ordinal = 2)
    })
    private int clampClickPickup(ItemStack instance, Operation<Integer> original) {
        return Math.min(instance.getCount(), instance.getMaxStackSize());
    }
    @WrapOperation(method = "slotClick", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxStackSize()I", ordinal = 0),
            @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxStackSize()I", ordinal = 1)
    })
    private int clampClickOverflow(ItemStack instance, Operation<Integer> original, @Local Slot clickedSlot) {
        if (clickedSlot instanceof ModularSlot mSlot && mSlot.isIgnoreMaxStackSize()) {
            return mSlot.getSlotStackLimit();
        }
        return original.call(instance);
    }

    @Inject(method = "slotClick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/inventory/Slot;putStack(Lnet/minecraft/item/ItemStack;)V",
                    ordinal = 3, shift = At.Shift.BEFORE), cancellable = true)
    private void fixClickSwap(int slotId, int mouseButton, ClickType clickTypeIn, EntityPlayer player,
                     CallbackInfoReturnable<ItemStack> cir, @Local(ordinal = 1) ItemStack slotStack,
                              @Local(ordinal = 0) ItemStack ret) {
        if (slotStack.getCount() > slotStack.getMaxStackSize()) {
            cir.setReturnValue(ret);
        }
    }

    @WrapOperation(method = "transferItem",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"))
    private int wrapMin(int slotLimit, int stackLimit, Operation<Integer> min, @Local(ordinal = 1) ModularSlot toSlot) {
        return toSlot.isIgnoreMaxStackSize() ? slotLimit : min.call(slotLimit, stackLimit);
    }
}
