package com.rumaruka.rftotime.common.inventory;

import com.cwelth.intimepresence.gui.CommonContainer;
import com.cwelth.intimepresence.items.AllItems;
import com.rumaruka.rftotime.common.tiles.RFShardProcessorTE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public abstract class RFShardProcessorContainer extends CommonContainer<RFShardProcessorTE> {
    public RFShardProcessorContainer(IInventory playerInventory, RFShardProcessorTE te) {
        super(playerInventory, te);
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
//        super.transferStackInSlot(playerIn, index);
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < ownSlotsCount) {
                if (!this.mergeItemStack(itemstack1, ownSlotsCount, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if(itemstack1.getItem() == Items.ENDER_PEARL)
                {
                    if(!this.mergeItemStack(itemstack1, 1, 2, false))
                        return ItemStack.EMPTY;
                } else if(itemstack1.getItem() == AllItems.dimensionalShards)
                {
                    if(!this.mergeItemStack(itemstack1, 2, 3, false))
                        return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileEntity.canInteractWith(playerIn);
    }
}
