package com.rumaruka.rftotime.common.tiles;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.items.AllItems;
import com.cwelth.intimepresence.network.SyncTESRAnim;
import com.cwelth.intimepresence.tileentities.CommonTE;

import com.rumaruka.rftotime.RFToTime;
import com.rumaruka.rftotime.api.CustomEnergyFE;
import com.rumaruka.rftotime.client.gui.RFShardProcessorIH;
import com.rumaruka.rftotime.network.SyncRFShardProcessor;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class RFShardProcessorTE extends CommonTE implements ITickable, ICapabilityProvider {

    public int burnTime = 0;
    public int burnTimeInitial = 0;
    public int pearlTime = 0;
    public int shardTime = 0;
    public int timeStored = 0;
    public BlockPos attachedTE = null;
    public boolean isGUIopened = false;
    private boolean isWorking;
    private int clientEnergy = -1;

    private CustomEnergyFE energy = new CustomEnergyFE(100_000, 25);

    public ItemStackHandler enderPearlSlot = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if(!world.isRemote){

                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);


                RFShardProcessorTE.this.markDirty();
            }
        }
    };
    public ItemStackHandler dimshardsSlot = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if(!world.isRemote) {
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);

                RFShardProcessorTE.this.markDirty();
            }
        }
    };
    RFShardProcessorIH capHandler = new RFShardProcessorIH(this);

    public RFShardProcessorTE( ) {
        super(1);

}

    @Override
    public void update() {
        if (burnTime == 0) {

            if (!dimshardsSlot.getStackInSlot(0).isEmpty() && dimshardsSlot.getStackInSlot(0).getItem() == AllItems.dimensionalShards || pearlTime > 0) {
                if (!enderPearlSlot.getStackInSlot(0).isEmpty() && enderPearlSlot.getStackInSlot(0).getItem() == Items.ENDER_PEARL || shardTime > 0) {
                    if ((burnTime=energy.getEnergyStored())>0) {


                        RFToTime.network.sendToAllAround(new SyncRFShardProcessor(this), new NetworkRegistry.TargetPoint(world.provider.getDimension(),
                                pos.getX(), pos.getY(), pos.getZ(), 64D));
                    }
                }
            }

        } else {
            burnTime-=10;
            if (burnTime<0)burnTime=0;

            if(pearlTime == 0) {
                if (!enderPearlSlot.getStackInSlot(0).isEmpty() && enderPearlSlot.getStackInSlot(0).getItem() == Items.ENDER_PEARL)
                {
                    pearlTime = 1000;
                    enderPearlSlot.getStackInSlot(0).shrink(1);
                    RFToTime.network.sendToAllAround(new SyncRFShardProcessor(this), new NetworkRegistry.TargetPoint(world.provider.getDimension(),
                            pos.getX(), pos.getY(), pos.getZ(), 64D));
                }
            }
            markDirty();
            sendUpdates();
        }
        if(pearlTime > 0 && burnTime > 0)
        {
            pearlTime--;
            if(shardTime == 0) {
                if (!dimshardsSlot.getStackInSlot(0).isEmpty() && dimshardsSlot.getStackInSlot(0).getItem() == AllItems.dimensionalShards)
                {
                    shardTime = 600;
                    dimshardsSlot.getStackInSlot(0).shrink(1);
                    RFToTime.network.sendToAllAround(new SyncRFShardProcessor(this), new NetworkRegistry.TargetPoint(world.provider.getDimension(),
                            pos.getX(), pos.getY(), pos.getZ(), 64D));
                }
            }
            markDirty();
            sendUpdates();
        }
        if(shardTime > 0 && pearlTime > 0 && burnTime > 0)
        {


            shardTime--;
            timeStored++;
            markDirty();
            sendUpdates();
        }
    }

    public void sendUpdates()
    {
        ModMain.network.sendToAllAround(new SyncTESRAnim(this, burnTimeInitial, burnTime, pearlTime, shardTime, timeStored),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(),
                        (double)getPos().getX(),
                        (double)getPos().getY(),
                        (double)getPos().getZ(),
                        16D
                ));
    }
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("burnTime")) burnTime = compound.getInteger("burnTime");
        if(compound.hasKey("burnTimeInitial")) burnTimeInitial = compound.getInteger("burnTimeInitial");
        if(compound.hasKey("pearlTime")) pearlTime = compound.getInteger("pearlTime");
        if(compound.hasKey("shardTime")) shardTime = compound.getInteger("shardTime");
        if(compound.hasKey("timeStored")) timeStored = compound.getInteger("timeStored");
        if(compound.hasKey("pearlStackHandler")) enderPearlSlot.deserializeNBT((NBTTagCompound)compound.getTag("pearlStackHandler"));
        if(compound.hasKey("shardsStackHandler")) dimshardsSlot.deserializeNBT((NBTTagCompound)compound.getTag("shardsStackHandler"));
        if(compound.hasKey("attachedTE")) attachedTE = NBTUtil.getPosFromTag((NBTTagCompound)compound.getTag("attachedTE"));

        else attachedTE = null;
        energy.setEnergy(compound.getInteger("energy"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setInteger("burnTime", burnTime);
        compound.setInteger("burnTimeInitial", burnTimeInitial);
        compound.setInteger("pearlTime", pearlTime);
        compound.setInteger("shardTime", shardTime);
        compound.setInteger("timeStored", timeStored);
        compound.setTag("pearlStackHandler", enderPearlSlot.serializeNBT());
        compound.setTag("shardsStackHandler", dimshardsSlot.serializeNBT());
        compound.setInteger("energy", energy.getEnergyStored());
        if(attachedTE != null) compound.setTag("attachedTE", NBTUtil.createPosTag(attachedTE));
        return compound;
    }
    @Override
    public boolean prepareGUIToBeOpened(boolean shouldOpen) {
        if(shouldOpen) {
            if(isGUIopened) return false;
            isGUIopened = true;
            return true;
        } else {
            isGUIopened = false;
            return true;
        }
    }


    public int getClientEnergy() {
        return clientEnergy;
    }

    public void setClientEnergy(int clientEnergy) {
        this.clientEnergy = clientEnergy;
    }




    public int getEnergy() {
        return energy.getEnergyStored();

    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        if (capability == CapabilityEnergy.ENERGY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public void updateTEFromPacket(int[] params) {
        burnTimeInitial = params[0];
        burnTime = params[1];
        pearlTime = params[2];
        shardTime = params[3];
        timeStored = params[4];
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) capHandler;
        }
        if(capability== CapabilityEnergy.ENERGY){
            return CapabilityEnergy.ENERGY.cast(energy);
        }
        return super.getCapability(capability, facing);
    }
}

