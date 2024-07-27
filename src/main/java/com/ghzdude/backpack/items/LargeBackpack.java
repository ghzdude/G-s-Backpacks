package com.ghzdude.backpack.items;

import com.cleanroommc.modularui.factory.HandGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.ghzdude.backpack.api.BaseBackpackItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public class LargeBackpack extends BaseBackpackItem {
    public LargeBackpack(ResourceLocation name) {
        super(name);
    }

    @Override
    protected IItemHandler createHandler(ItemStack container) {
        return null;
    }

    @Override
    public ModularPanel buildUI(HandGuiData data, PanelSyncManager syncManager) {
        return null;
    }
}
