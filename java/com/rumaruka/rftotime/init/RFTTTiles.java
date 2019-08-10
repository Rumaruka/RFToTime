package com.rumaruka.rftotime.init;

import com.cwelth.intimepresence.renderers.TimeMachineTESR;
import com.rumaruka.rftotime.client.renderer.TimeMachineTESRForRF;
import com.rumaruka.rftotime.common.tiles.RFShardProcessorTE;
import com.rumaruka.rftotime.common.tiles.TimeMachineTEForRF;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RFTTTiles {

    public static void registerTileEntites(){
        GameRegistry.registerTileEntity(RFShardProcessorTE.class,"rfshardprocessor");
        GameRegistry.registerTileEntity(TimeMachineTEForRF.class,"timemachine_rf");
        ClientRegistry.bindTileEntitySpecialRenderer(TimeMachineTEForRF.class, new TimeMachineTESRForRF());
    }
}
