package com.ghzdude.backpack.items;

import com.ghzdude.backpack.BackpacksMod;
import com.ghzdude.backpack.api.BaseBackpackItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/*
 * Items:
 * - Bulk Backpack - stores items up to a stack size of 4096, option to store items on pickup, no NBT or tools
 * - Large Backpack - stores many slots of items up to max stack size
 * - Crafting Backpack - stores items and has a 3x3 crafting grid
 * - Fluid Backpack - stores a few fluid tanks, and some slots for items
 * - Tool Backpack - stores damage-able items like tools and weapons
 */
public class BackpackItems {

    public static final List<Item> ITEMS = new ArrayList<>();

    public static final BaseBackpackItem BULK_BACKPACK = new BulkBackpack(location("bulk_basic"));
    public static final BaseBackpackItem LARGE_BACKPACK = new LargeBackpack(location("large_backpack"));

    public static ResourceLocation location(String path) {
        return new ResourceLocation(BackpacksMod.MODID, path);
    }
}
