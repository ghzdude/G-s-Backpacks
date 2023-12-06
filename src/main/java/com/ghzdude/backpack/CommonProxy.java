package com.ghzdude.backpack;

import com.ghzdude.backpack.items.BackpackItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
}
