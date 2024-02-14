package com.ghzdude.backpack.items;

import com.ghzdude.backpack.BackpacksMod;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class BackpackItems {

    public static final List<Item> ITEMS = new ArrayList<>();

    public static final Item BULK_BACKPACK = new BackpackItem(new ResourceLocation(BackpacksMod.MODID, "bulk_basic"), 0);
    //todo large bulk backpack
    //todo tool backpack
    //todo fluid backpack?
//    public static final Item LARGE_BACKPACK = new BackpackItem(new ResourceLocation(BackpacksMod.MODID, "large"), 1);
//    public static final Item MASSIVE_BACKPACK = new BackpackItem(new ResourceLocation(BackpacksMod.MODID, "massive"), 2);
}
