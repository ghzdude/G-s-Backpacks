package com.ghzdude.backpack.mixin.modularui;

import com.cleanroommc.modularui.core.mixin.ContainerAccessor;
import com.cleanroommc.modularui.screen.ModularContainer;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.SlotGroup;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(value = ModularContainer.class, remap = false)
public abstract class ModularContainerMixin extends Container {

    @WrapOperation(method = "slotClick", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getCount()I", ordinal = 1),
            @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getCount()I", ordinal = 2)
    })
    private int clampPickup(ItemStack instance, Operation<Integer> original) {
        if (instance.getCount() > instance.getMaxStackSize()) {
            return instance.getMaxStackSize();
        }
        return original.call(instance);
    }
    @WrapOperation(method = "transferItem",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"))
    protected int wrapMin(int slotLimit, int stackLimit, Operation<Integer> min, @Local(ordinal = 1) ModularSlot toSlot) {
        return toSlot.isIgnoreMaxStackSize() ? slotLimit : min.call(slotLimit, stackLimit);
    }

//    @WrapOperation(method = "transferStackInSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;"))
//    protected ItemStack clampCopy(ItemStack instance, Operation<ItemStack> original) {
//        if (instance.getCount() > instance.getMaxStackSize()) {
//            return instance.splitStack(instance.getMaxStackSize());
//        }
//        return original.call(instance);
//    }
}
