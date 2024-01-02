package com.ghzdude.backpack.mixin;

import com.cleanroommc.modularui.screen.ModularContainer;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Arrays;
import java.util.List;

@Mixin(ModularContainer.class)
public class GuiManagerMixin implements ILateMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        return Arrays.asList("mixins.ghzbackpacks.json");
    }

    @Inject(method = "transferItem",
            remap = false,
            locals = LocalCapture.CAPTURE_FAILHARD,
            at = @At(target = "Ljava/lang/Math;min(II)I",
                    shift = At.Shift.AFTER,
                    by = 2,
                    value = "INVOKE_ASSIGN"))
    protected void transferItemMixin(ModularSlot fromSlot, ItemStack fromStack,
                                     CallbackInfoReturnable<ItemStack> cir,
                                     @Local(name = "maxSize") LocalRef<Integer> maxSize) {
        maxSize.set(5000);
        cir.cancel();
    }
}
