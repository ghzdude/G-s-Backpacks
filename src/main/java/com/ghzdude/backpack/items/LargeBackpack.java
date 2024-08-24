package com.ghzdude.backpack.items;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.scroll.VerticalScrollData;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.SlotGroup;
import com.ghzdude.backpack.api.BaseBackpackItem;
import com.ghzdude.backpack.gui.slot.BackpackSlot;
import com.ghzdude.backpack.handler.BackpackHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

import java.util.Arrays;

public class LargeBackpack extends BaseBackpackItem {
    protected static final int BASE_STACK_SIZE = 64;
    protected static final int BASE_SLOT_SIZE = 243;
    public LargeBackpack(ResourceLocation name) {
        super(name);
    }

    @Override
    protected IItemHandler createHandler(ItemStack container) {
        return new BackpackHandler(container, BASE_SLOT_SIZE, BASE_STACK_SIZE);
    }

    @Override
    protected IWidget createBackpackUI(ItemStack backpack, PanelSyncManager syncManager) {
        var group = new SlotGroup(SYNC_NAME, 9, true);
        var widgets = new ItemSlot[BASE_SLOT_SIZE];
        Arrays.fill(widgets, new ItemSlot());
        return new Grid()
                .margin(0)
                .height(18 * 6)
                .scrollable(new VerticalScrollData())
                .mapTo(9, Arrays.asList(widgets), (i, slot) -> slot
                        .slot(new BackpackSlot(getHandler(backpack), i)
                                .slotGroup(group)));
    }
}
