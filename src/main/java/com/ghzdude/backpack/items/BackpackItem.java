package com.ghzdude.backpack.items;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.manager.GuiCreationContext;
import com.cleanroommc.modularui.manager.GuiInfos;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.ItemStackItemHandler;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.ghzdude.backpack.BackpacksMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BackpackItem extends Item implements IGuiHolder {
    int tier;
    SyncHandler[] syncHandlers;
    public static final String SYNC_NAME = "backpack_inventory";
    public BackpackItem(ResourceLocation name, int tier) {
        this.tier = tier;
        syncHandlers = new SyncHandler[tier * 3 * 9];
        setRegistryName(name);
        setCreativeTab(CreativeTabs.TOOLS);
        setTranslationKey(name.getNamespace() + "." + name.getPath());
        setMaxStackSize(1);
        BackpackItems.ITEMS.add(this);
    }
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {
            GuiInfos.getForHand(handIn).open(playerIn);
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public ModularPanel buildUI(GuiCreationContext guiCreationContext, GuiSyncManager guiSyncManager, boolean isClient) {
        IItemHandlerModifiable itemHandler = (IItemHandlerModifiable) guiCreationContext.getUsedItemStack().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        guiSyncManager.registerSlotGroup(SYNC_NAME, 9);

        ModularPanel panel = new ModularPanel("backpack_gui").align(Alignment.Center);
        SlotGroupWidget.Builder slotBuilder = SlotGroupWidget.builder();

        for (int i = 0; i < tier * 3; i++) {
            slotBuilder.row("XXXXXXXXX");
        }

        slotBuilder.key('X', i -> {
            ItemSlot slot = new ItemSlot().slot(SyncHandlers.itemSlot(itemHandler, i).slotGroup(SYNC_NAME));
            syncHandlers[i] = slot.getSyncHandler();
            return slot;
        });

        IWidget title = IKey.str("Inventory").asWidget().top(4).left(4);
        IWidget slotGroupWidget = slotBuilder.build().top(4 + 12).leftRel(0.5f);

        panel.child(title)
                .child(slotGroupWidget)
                .bindPlayerInventory();
        return panel;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ItemStackItemHandler(stack, syncHandlers.length);
    }
}
