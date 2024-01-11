package com.ghzdude.backpack;

import com.ghzdude.backpack.items.BackpackItems;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = BackpacksMod.MODID)
public class CommonProxy {
    @SubscribeEvent
    public static void itemRegister(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        for (Item item : BackpackItems.ITEMS) {
            registry.register(item);
        }
    }

    @SuppressWarnings("DataFlowIssue")
    @SubscribeEvent
    public static void modelRegister(ModelRegistryEvent event) {
        for (Item item : BackpackItems.ITEMS) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
    }

    private static final String CACHE = "backpack_cache";
    @SuppressWarnings("DataFlowIssue")
    @SubscribeEvent
    public static void onPickup(EntityItemPickupEvent event) {
        var playerTag = event.getEntityPlayer().getEntityData();
        var toInsert = event.getItem().getItem();
        byte i = playerTag.hasKey(CACHE) ? playerTag.getByte(CACHE) : 0;

        ItemStack cachedStack = event.getEntityPlayer().inventory.getStackInSlot(i);
        ItemStack returnable = toInsert.copy();
        IItemHandler handler;
        if (BackpackItems.ITEMS.contains(cachedStack.getItem())) {
            handler = cachedStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            returnable = insertItem(handler, toInsert);
        } else {
            IInventory inv = event.getEntityPlayer().inventory;
            for (byte j = 0; j < inv.getSizeInventory(); j++) {
                var stack = inv.getStackInSlot(j);
                if (!BackpackItems.ITEMS.contains(stack.getItem())) continue;

                var hasHandler = stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                if (!hasHandler) continue;

                handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                playerTag.setByte(CACHE, j);
                returnable = insertItem(handler, toInsert);
            }
        }
        if (returnable.isEmpty()) {
            var rand = event.getEntityPlayer().getEntityWorld().rand;
            event.getEntityPlayer().playSound(SoundEvents.ENTITY_ITEM_PICKUP,0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F );
            event.getItem().setDead();
            event.setCanceled(true);
        }
    }

    private static ItemStack insertItem(IItemHandler handler, ItemStack stack) {
        var remainder = stack.copy();
        IntList matchingSlots = new IntArrayList(handler.getSlots());
        IntList emptySlots = new IntArrayList(handler.getSlots());

        for (int i = 0; i < handler.getSlots(); i++) {
            var ourStack = handler.getStackInSlot(i);
            if (ourStack.isEmpty()) {
                emptySlots.add(i);

            } else if (ItemHandlerHelper.canItemStacksStack(ourStack, stack) && handler.getSlotLimit(i) - ourStack.getCount() > 0) {
                matchingSlots.add(i);

            }
        }

        for (int i : matchingSlots) {
            remainder = handler.insertItem(i, remainder, false);
            if (remainder.isEmpty()) return remainder;
        }

        for (int i : emptySlots) {
            remainder = handler.insertItem(i, remainder, false);
            if (remainder.isEmpty()) return remainder;
        }
        return remainder;
    }
}
