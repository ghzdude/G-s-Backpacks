package com.ghzdude.backpack;

import com.ghzdude.backpack.items.BackpackItems;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = BackpacksMod.MODID,
     name = "Ghzdude's Backpacks",
     version = "0.1",
     acceptedMinecraftVersions = "[1.12.2,1.13)",
     dependencies = "required:forge; required-after:modularui@[2.3.1,)")
public class BackpacksMod {
    public static final String MODID = "backpacks";
}
