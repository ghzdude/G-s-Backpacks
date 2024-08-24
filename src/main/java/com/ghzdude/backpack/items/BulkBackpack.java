package com.ghzdude.backpack.items;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.slot.SlotGroup;
import com.ghzdude.backpack.api.BaseBackpackItem;
import com.ghzdude.backpack.gui.slot.BackpackSlot;
import com.ghzdude.backpack.handler.BackpackHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public class BulkBackpack extends BaseBackpackItem {

    protected static final int BASE_STACK_SIZE = 4096;
    protected static final int BASE_SLOT_SIZE = 27;

    public BulkBackpack(ResourceLocation name) {
        super(name);
    }

    @Override
    protected IWidget createBackpackUI(ItemStack backpack, PanelSyncManager syncManager) {
        IItemHandler handler = getHandler(backpack);
        SlotGroup backpackGroup = new SlotGroup(SYNC_NAME, 9, 200, true);
        syncManager.registerSlotGroup(backpackGroup);

        return new Column().sizeRel(1.0f)
                        .child(new Column().coverChildren()
                                .child(IKey.lang(backpack.getDisplayName())
                                        .asWidget()
                                        .left(0)
                                        .marginBottom(6))
                                .child(SlotGroupWidget.builder()
                                        .matrix("XXXXXXXXX",
                                                "XXXXXXXXX",
                                                "XXXXXXXXX")
                                        .key('X', i -> new ItemSlot()
                                                .slot(new BackpackSlot(handler, i)
                                                        .slotGroup(backpackGroup)
                                                        .filter(itemStack -> handler.isItemValid(i, itemStack))))
                                        .build()))
                        .child(SlotGroupWidget.playerInventory()
                                .leftRel(0.5f)
                                .bottom(0));
    }

    @Override
    protected IItemHandler createHandler(ItemStack container) {
        return new BackpackHandler(container, BASE_SLOT_SIZE, BASE_STACK_SIZE);
    }
}
