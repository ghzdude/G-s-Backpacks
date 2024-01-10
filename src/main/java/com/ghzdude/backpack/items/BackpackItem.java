package com.ghzdude.backpack.items;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.HandGuiData;
import com.cleanroommc.modularui.factory.ItemGuiFactory;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.ItemCapabilityProvider;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.SlotGroup;
import com.ghzdude.backpack.gui.slot.BackpackSlot;
import com.ghzdude.backpack.gui.slot.PlayerSlot;
import com.ghzdude.backpack.handler.BackpackHandler;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackpackItem extends Item implements IGuiHolder<HandGuiData> {
    int tier;
    public static final String SYNC_NAME = "backpack_inventory";

    private static final int BASE_STACK_SIZE = 4096;
    private static final int BASE_SLOT_SIZE = 9;
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
        IItemHandlerModifiable itemHandler = getHandler(data.getUsedItemStack());
        var backpack = new SlotGroup(SYNC_NAME, 9, 200, true);
        syncManager.registerSlotGroup(backpack);

        var builder = SlotGroupWidget.builder();

        for (int i = 0; i < (tier + 1); i++) {
            builder.row("XXXXXXXXX");
        }

        builder.key('X', i -> new ItemSlot()
                .slot(new BackpackSlot(itemHandler, i)
                        .slotGroup(backpack)
                        .filter(itemStack -> !BackpackItems.ITEMS.contains(itemStack.getItem()))
        ));

        return new ModularPanel("backpack_gui").align(Alignment.Center)
                .padding(4, 7)
                .child(new Column().sizeRel(1.0f)
                        .child(new Column().coverChildren()
                                .child(IKey.str("Inventory").asWidget()
                                        .left(0)
                                        .marginBottom(2))
                                .child(builder.build()
                                    .coverChildren()
                                    .marginBottom(4)))
                        .child(SlotGroupWidget.playerInventory()
                                .leftRel(0.5f)
                                .bottom(0)));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ItemCapabilityProvider() {

            private final BackpackHandler handler = new BackpackHandler(stack, BASE_SLOT_SIZE * (1 + tier), BASE_STACK_SIZE * (1 + tier));
            @Override
            public <T> @Nullable T getCapability(@NotNull Capability<T> capability) {
                if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                    return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler);
                }
                return null;
            }
        };
    }

    protected IItemHandlerModifiable getHandler(ItemStack stack) {
        return (IItemHandlerModifiable) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }
}
