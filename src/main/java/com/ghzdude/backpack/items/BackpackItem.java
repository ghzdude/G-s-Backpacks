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

    int slots;
    List<SyncHandler> syncHandlers = new ArrayList<>(slots);
    public static final String SYNC_NAME = "inventory";
    public BackpackItem(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(BackpacksMod.MODID, name);
        setRegistryName(resourceLocation);
        setCreativeTab(CreativeTabs.TOOLS);
        setTranslationKey(resourceLocation.getNamespace() + "." + resourceLocation.getPath());
        setMaxStackSize(1);
        BackpackItems.ITEMS.add(this);
        slots = 9;
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

        ModularPanel panel = new ModularPanel("backpack_gui").align(Alignment.Center).width(192);
        SlotGroupWidget.Builder slotBuilder = SlotGroupWidget.builder()
                .row("XXXXXXXXX")
                .key('X', i -> {
                    ItemSlot slot = new ItemSlot().slot(SyncHandlers.itemSlot(itemHandler, i).slotGroup(SYNC_NAME));
                    syncHandlers.add(slot.getSyncHandler());
                    return slot;
                });

        IWidget slotGroupWidget = slotBuilder.build().top(18);
        IWidget title = IKey.str("Inventory").asWidget();
        panel.child(new Column()
                .padding(18)
                .child(title)
                .child(slotGroupWidget)
                .child(SlotGroupWidget.playerInventory().align(Alignment.Center))
        );
        return panel;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ItemStackItemHandler(stack, slots);
    }
}
