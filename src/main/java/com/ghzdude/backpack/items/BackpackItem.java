package com.ghzdude.backpack.items;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.manager.ClientGUI;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.value.sync.ItemSlotSH;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.ghzdude.backpack.BackpacksMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class BackpackItem extends Item {

    IItemHandler handler;
    List<SyncHandler> syncHandlers;
    public BackpackItem(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(BackpacksMod.MODID, name);
        setRegistryName(resourceLocation);
        setCreativeTab(CreativeTabs.TOOLS);
        setTranslationKey(resourceLocation.getNamespace() + "." + resourceLocation.getPath());
        setMaxStackSize(1);
        BackpackItems.ITEMS.add(this);
        handler = new ItemStackHandler(9);
        syncHandlers = new ArrayList<>(handler.getSlots());
    }
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (worldIn.isRemote) {
            ClientGUI.open(createGUI());
            return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return ActionResult.newResult(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    public ModularScreen createGUI() {
//        ModularPanel panel = ModularPanel.defaultPanel("inventory");
        ModularPanel panel = new ModularPanel("inventory")
                .size(100, 80)
                .bindPlayerInventory();
        SlotGroupWidget.Builder slotBuilder = SlotGroupWidget.builder()
                .row("XXXXXXXXX")
                .key('X', i -> {
                    ItemSlot slot = new ItemSlot().slot(new ModularSlot(handler, i));
                    syncHandlers.add(slot.getSyncHandler());
                    return slot;
                });

        IWidget slotGroupWidget = slotBuilder.build().top(7 + 16).left(7);
        IWidget title = IKey.str("Inventory").asWidget().top(7).left(7);
        panel.child(slotGroupWidget).child(title);
        return new ModularScreen(BackpacksMod.MODID, panel);
    }
}
