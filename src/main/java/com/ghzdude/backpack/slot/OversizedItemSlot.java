package com.ghzdude.backpack.slot;

import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.ghzdude.backpack.handler.OversizedItemSlotSH;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OversizedItemSlot extends ItemSlot {
    @Override
    public ItemSlot slot(ModularSlot slot) {
        setSyncHandler(new OversizedItemSlotSH(slot));
        return this;
    }

    @Override
    public void setGhostIngredient(@NotNull ItemStack ingredient) {
        // do nothing
    }

    @Override
    public @Nullable ItemStack castGhostIngredientIfValid(@NotNull Object ingredient) {
        return null;
    }

}
