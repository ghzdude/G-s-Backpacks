package com.ghzdude.backpack.items;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.manager.ClientGUI;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.ghzdude.backpack.BackpacksMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class BackpackItem extends Item implements IForgeRegistryEntry<Item> {

    public BackpackItem(String name) {
        setRegistryName(BackpacksMod.MODID, name);
        setTranslationKey("backpack." + name);
        setMaxStackSize(1);
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
        ModularPanel panel = ModularPanel.defaultPanel("inventory");
        panel.child(IKey.str("My first screen").asWidget()
                .top(7).left(7));
        return new ModularScreen(BackpacksMod.MODID, panel);
    }

}
