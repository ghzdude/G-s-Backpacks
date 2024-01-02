package com.ghzdude.backpack.mixin.modularui;

import com.cleanroommc.modularui.screen.ModularContainer;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ModularContainer.class)
public abstract class ModularContainerMixin {

    @Inject(method = "transferItem",
            remap = false,
            locals = LocalCapture.PRINT,
            at = @At(target = "Ljava/lang/Math;min(II)I",
                    shift = At.Shift.AFTER,
                    value = "INVOKE_ASSIGN"))
    protected void transferItemMixin(ModularSlot fromSlot, ItemStack fromStack,
                                                   @Local(ordinal = 9) LocalRef<Integer> maxSize) {
        maxSize.set(5000);
    }
}
