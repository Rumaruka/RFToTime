package com.rumaruka.rftotime.common.inventory;

import com.cwelth.intimepresence.gui.steamhammer.SteamHammerSlot;
import com.rumaruka.rftotime.api.ISync;
import com.rumaruka.rftotime.common.tiles.RFShardProcessorTE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;

public class RFShardProcessorServerContainer extends RFShardProcessorContainer implements ISync {
    public RFShardProcessorServerContainer(IInventory playerInventory, RFShardProcessorTE te) {
        super(playerInventory, te);
    }

    @Override
    protected void addOwnSlots() {
        addSlotToContainer(new SteamHammerSlot(tileEntity.enderPearlSlot, 0, 79, 74, false)); //Input
        addSlotToContainer(new SteamHammerSlot(tileEntity.dimshardsSlot, 0, 97, 74, false)); //Input
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if(!tileEntity.getWorld().isRemote){
            if(tileEntity.getEnergy()!=tileEntity.getClientEnergy()){
                tileEntity.setClientEnergy(tileEntity.getEnergy());

            }
        }
    }

    @Override
    public void sync(int energy) {
        tileEntity.setClientEnergy(energy);
    }
}
