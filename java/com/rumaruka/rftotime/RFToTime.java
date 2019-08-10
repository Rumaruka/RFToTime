package com.rumaruka.rftotime;



import com.rumaruka.rftotime.api.Const;
import com.rumaruka.rftotime.init.RFTTBlocks;
import com.rumaruka.rftotime.init.RFTTTiles;
import com.rumaruka.rftotime.network.SyncRFShardProcessor;
import com.rumaruka.rftotime.proxy.CommonProxy;
import com.rumaruka.rftotime.utils.GuiHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Const.MODID,name = "RF To Time",version = "0.1a",dependencies = "required-after:intimepresence@[1.2,)")
public class RFToTime {
    @Mod.Instance(Const.MODID)
    public static RFToTime instance;
    public static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);
    @SidedProxy(
            clientSide = "com.rumaruka.rftotime.proxy.ClientProxy",
            serverSide = "com.rumaruka.rftotime.proxy.CommonProxy"
    )
    public static CommonProxy proxy;
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);

        RFTTBlocks.init();
        RFTTBlocks.InGameRegister();
        RFTTTiles.registerTileEntites();

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.renderObject();
         proxy.init(event);
         network.registerMessage(SyncRFShardProcessor.Handler.class,SyncRFShardProcessor.class,7, Side.CLIENT);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance,new GuiHandler());



    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e)
    {
        proxy.postInit(e);
    }
}
