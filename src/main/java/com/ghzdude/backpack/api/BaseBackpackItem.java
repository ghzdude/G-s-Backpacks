package com.ghzdude.backpack.api;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.HandGuiData;
import com.cleanroommc.modularui.factory.ItemGuiFactory;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.ItemCapabilityProvider;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.ghzdude.backpack.items.BackpackItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseBackpackItem extends Item implements IGuiHolder<HandGuiData> {
    protected static final String SYNC_NAME = "backpack_inventory";
    private final String guiName;

    public BaseBackpackItem(ResourceLocation name) {
        guiName = name.getPath() + "_gui";
        setRegistryName(name);
        setTranslationKey(name.getPath());
        setCreativeTab(CreativeTabs.TOOLS);
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

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ItemCapabilityProvider() {
            private final IItemHandler handler = createHandler(stack);

            @Override
            public <T> @Nullable T getCapability(@NotNull Capability<T> capability) {
                if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                    return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler);
                }
                return null;
            }

            @Override
            public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
                return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
            }
        };
    }

    protected IItemHandler getHandler(ItemStack stack) {
        return stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

    protected abstract IItemHandler createHandler(ItemStack container);

    @Override
    public final ModularPanel buildUI(HandGuiData data, PanelSyncManager syncManager) {
        return ModularPanel.defaultPanel(guiName)
                .padding(4, 7)
                .child(createBackpackUI(data.getUsedItemStack(), syncManager));
    }

    protected abstract IWidget createBackpackUI(ItemStack backpack, PanelSyncManager syncManager);
}
