package com.rumaruka.rftotime.client.gui;

import com.cwelth.intimepresence.items.AllItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class RFTTCreativeTabs extends CreativeTabs {

    public static RFTTCreativeTabs rfttCreativeTabs = new RFTTCreativeTabs();

    public RFTTCreativeTabs( ) {
        super("rftotime");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(AllItems.dimensionalShards);
    }
}
