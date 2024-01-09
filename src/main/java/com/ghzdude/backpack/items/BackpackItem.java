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

        SlotGroupWidget.Builder slotBuilder = SlotGroupWidget.builder();

        for (int i = 0; i < (tier + 1); i++) {
            slotBuilder.row("XXXXXXXXX");
        }

        slotBuilder.key('X', i -> new ItemSlot()
                .slot(new BackpackSlot(itemHandler, i)
                        .slotGroup(backpack)
                        .filter(itemStack -> !BackpackItems.ITEMS.contains(itemStack.getItem()))
        ));

        return new ModularPanel("backpack_gui").align(Alignment.Center)
                .child(new Column().coverChildrenHeight().widthRel(1f).margin(7, 0)
                    .child(IKey.str("Inventory").asWidget().left(0).margin(0, 2))
                    .child(slotBuilder.build().coverChildren().marginBottom(2)))
                .child(createPlayerInventory(syncManager));
    }

    private static IWidget createPlayerInventory(GuiSyncManager manager) {
        IItemHandler player = manager.getPlayerInventory();
        SlotGroup playerSG = new SlotGroup("player_slots", 9, 0, true);
        SlotGroupWidget slotGroupWidget = new SlotGroupWidget();

        slotGroupWidget
                .debugName("player_inventory")
                .flex().coverChildren()
                .startDefaultMode()
                .leftRel(0.5f)
                .endDefaultMode()
                .bottom(7);
        for (int i = 0; i < 9; i++) {
            slotGroupWidget.child(new ItemSlot()
                    .slot(new PlayerSlot(player, i)
                            .slotGroup(playerSG))
                    .pos(i * 18, 3 * 18 + 5)
                    .debugName("slot_" + i));
        }
        for (int i = 0; i < 27; i++) {
            slotGroupWidget.child(new ItemSlot()
                    .slot(new ModularSlot(player, i + 9)
                            .slotGroup(playerSG))
                    .pos(i % 9 * 18, i / 9 * 18)
                    .debugName("slot_" + (i + 9)));
        }

        return slotGroupWidget;
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
