package com.rumaruka.rftotime.common.inventory;

import com.cwelth.intimepresence.gui.steamhammer.SteamHammerSlot;
import com.rumaruka.rftotime.common.tiles.RFSteamHammerTE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.IItemHandler;

public class RFSteamHammerServerContainer extends RFSteamHammerContainer{
    public RFSteamHammerServerContainer(IInventory playerInventory, RFSteamHammerTE te) {
        super(playerInventory, te);
    }

    @Override
    protected void addOwnSlots() {
        IItemHandler inputHandler = this.tileEntity.itemStackHandler;
        IItemHandler outputHandler = this.tileEntity.outputHandler;
        IItemHandler bucketHandler = this.tileEntity.bucketHandler;


        addSlotToContainer(new SteamHammerSlot(inputHandler, 0, 52, 25, false)); //Input
        addSlotToContainer(new SteamHammerSlot(outputHandler, 0, 106, 25, true)); //Output
        addSlotToContainer(new SteamHammerSlot(bucketHandler, 0, 25, 83,false)); //Water input
    }


}
