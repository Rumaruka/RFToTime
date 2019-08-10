package com.rumaruka.rftotime.client.gui;

import com.cwelth.intimepresence.items.AllItems;
import com.rumaruka.rftotime.common.tiles.RFShardProcessorTE;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class RFShardProcessorIH extends ItemStackHandler {
    RFShardProcessorTE te = null;

    public RFShardProcessorIH(RFShardProcessorTE te)
    {
        this.te = te;
    }


    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if(stack.getItem() == Items.ENDER_PEARL)
            te.enderPearlSlot.setStackInSlot(0, stack);
        else if(stack.getItem() == AllItems.dimensionalShards)
            te.dimshardsSlot.setStackInSlot(0, stack);
        else
            te.itemStackHandler.insertItem(slot, stack,false);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(stack.getItem() == Items.ENDER_PEARL)
            return te.enderPearlSlot.insertItem(slot, stack, simulate);
        else if(stack.getItem() == AllItems.dimensionalShards)
            return te.dimshardsSlot.insertItem(slot, stack, simulate);
        else
            return te.itemStackHandler.insertItem(slot, stack, simulate);
    }
}
