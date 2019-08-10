package com.rumaruka.rftotime.proxy;

import com.cwelth.intimepresence.ModMain;
import com.rumaruka.rftotime.init.RFTTBlocks;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber({Side.CLIENT})
public class ClientProxy extends CommonProxy {

    public ClientProxy() {
    }

    public void preInit(FMLPreInitializationEvent e) {
        OBJLoader.INSTANCE.addDomain("rftotime");
        OBJLoader.INSTANCE.addDomain(ModMain.MODID);
        super.preInit(e);
    }


    public void postInit(FMLPostInitializationEvent e) {

    }

    @Override
    public void renderObject() {
        RFTTBlocks.renderBlocks();

    }

    public void init(FMLInitializationEvent e) {
        super.init(e);
    }
}
