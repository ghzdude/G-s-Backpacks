package com.ghzdude.backpack.items;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.HandGuiData;
import com.cleanroommc.modularui.factory.ItemGuiFactory;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.ghzdude.backpack.handler.BackpackHandler;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackpackItem extends Item implements IGuiHolder<HandGuiData> {
    int tier;
    public static final String SYNC_NAME = "backpack_inventory";

    private static final int[] BACKPACK_SIZES = new int[] {1000000, 4000000, 16000000};
    public BackpackItem(ResourceLocation name, int tier) {
        this.tier = tier;
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
        SlotGroupWidget.Builder slotBuilder = SlotGroupWidget.builder().row("X"); // testing

//        for (int i = 0; i < (tier + 1) * 3; i++) {
//            slotBuilder.row("XXXXXXXXX");
//        }

        slotBuilder.key('X', i -> new ItemSlot().slot(
                new BackpackSlot(itemHandler, i)
                        .slotGroup(SYNC_NAME)
                        .filter(itemStack -> !BackpackItems.ITEMS.contains(itemStack.getItem()))
        ));

        IWidget title = IKey.str("Inventory").asWidget().top(4).left(4);
        IWidget slotGroupWidget = slotBuilder.build().top(4 + 12).leftRel(0.5f);

        panel.child(title)
                .child(slotGroupWidget)
                .bindPlayerInventory();
        return panel;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new BackpackHandler(stack, (tier + 1) * 27, BACKPACK_SIZES[tier]);
    }
}
