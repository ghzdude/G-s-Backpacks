package com.ghzdude.backpack.items;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.HandGuiData;
import com.cleanroommc.modularui.factory.ItemGuiFactory;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.ItemStackItemHandler;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.ghzdude.backpack.slot.BackpackSlot;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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

public class BackpackItem extends Item implements IGuiHolder<HandGuiData> {
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
            ItemGuiFactory.open((EntityPlayerMP) playerIn, playerIn.getActiveHand());
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public ModularPanel buildUI(HandGuiData data, GuiSyncManager syncManager) {
        IItemHandlerModifiable itemHandler = (IItemHandlerModifiable) data.getUsedItemStack().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        syncManager.registerSlotGroup(SYNC_NAME, 9);

        ModularPanel panel = new ModularPanel("backpack_gui").align(Alignment.Center);
        SlotGroupWidget.Builder slotBuilder = SlotGroupWidget.builder();

        for (int i = 0; i < tier * 3; i++) {
            slotBuilder.row("XXXXXXXXX");
        }

        slotBuilder.key('X', i -> {
            ItemSlot slot = new ItemSlot().slot(
                    new BackpackSlot(itemHandler, i, tier * 1000000)
                            .slotGroup(SYNC_NAME)
                            .filter(itemStack -> !BackpackItems.ITEMS.contains(itemStack.getItem()))
            );
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
