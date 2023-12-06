package com.ghzdude.backpack;

import com.ghzdude.backpack.items.BackpackItem;
import com.ghzdude.backpack.items.BackpackItems;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = BackpacksMod.MODID)
public class CommonProxy {

    @SubscribeEvent
    public static void itemRegister(RegistryEvent.Register<Item> event) {
        BackpackItems.basicBackpack = new BackpackItem("basic");
        event.getRegistry().register(BackpackItems.basicBackpack);
    }
}
