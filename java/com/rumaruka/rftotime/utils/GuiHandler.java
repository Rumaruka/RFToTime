package com.rumaruka.rftotime.utils;

import com.rumaruka.rftotime.api.GUIs;
import com.rumaruka.rftotime.client.gui.RFShardProcessorGUIContainer;
import com.rumaruka.rftotime.common.inventory.RFShardProcessorServerContainer;
import com.rumaruka.rftotime.common.tiles.RFShardProcessorTE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (ID == GUIs.rfshardprocessor.ordinal()) {
            RFShardProcessorTE te = (RFShardProcessorTE) world.getTileEntity(pos);
            return new RFShardProcessorServerContainer(player.inventory, te);

        }
        else
        return null;
    }
        @Nullable
        @Override
        public Object getClientGuiElement ( int ID, EntityPlayer player, World world,int x, int y, int z){
            BlockPos pos = new BlockPos(x, y, z);
            if(ID == GUIs.rfshardprocessor.ordinal()) {
                RFShardProcessorTE te = (RFShardProcessorTE) world.getTileEntity(pos);
                return new RFShardProcessorGUIContainer(te, new RFShardProcessorServerContainer(player.inventory, te), "textures/gui/shardprocessor.png", player);
            } else
            return null;
        }
    }

