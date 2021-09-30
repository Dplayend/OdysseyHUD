package com.dplayend.odysseyhud.bauble;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.render.IRenderBauble;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaubleClock extends Item implements IBauble, IRenderBauble {
    private final BaubleType type;
    public BaubleClock(BaubleType type) {
        this.type = type;
        setUnlocalizedName("clock");
        setMaxStackSize(64);
        setCreativeTab(CreativeTabs.TOOLS);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        ItemStack stack = player.getHeldItem(hand);

        for (int slot : type.getValidSlots()) {
            ItemStack remainder = handler.insertItem(slot, stack.copy(), true);
            if (remainder.getCount() < stack.getCount()) {
                player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
                handler.insertItem(slot, stack.copy(), false);
                stack.setCount(remainder.getCount());
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }
        }
        return new ActionResult<>(EnumActionResult.FAIL, stack);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return type;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onPlayerBaubleRender(ItemStack itemStack, EntityPlayer entityPlayer, RenderType renderType, float v) {

    }
}
