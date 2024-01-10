package com.ghzdude.backpack.mixin.modularui;

import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.ghzdude.backpack.gui.slot.PlayerSlot;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiSyncManager.class)
public class GuiSyncManagerMixin {

    @Shadow @Final private PlayerMainInvWrapper playerInventory;

    @Shadow @Final private static String PLAYER_INVENTORY;

    @WrapOperation(method = "<init>",
            at = @At(value = "INVOKE",
                    target = "Lcom/cleanroommc/modularui/value/sync/GuiSyncManager;itemSlot(" +
                            "Ljava/lang/String;" +
                            "ILcom/cleanroommc/modularui/widgets/slot/ModularSlot;)" +
                            "Lcom/cleanroommc/modularui/value/sync/GuiSyncManager;"))
    private GuiSyncManager fixPlayerSlot(GuiSyncManager instance, String key, int id, ModularSlot slot, Operation<GuiSyncManager> original) {
        ModularSlot playerSlot = new PlayerSlot(this.playerInventory, id).slotGroup(PLAYER_INVENTORY);
        return original.call(instance, key, id, playerSlot);
    }
}
