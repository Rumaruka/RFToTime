package com.rumaruka.rftotime.proxy;


import com.rumaruka.rftotime.RFTTConfig;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class CommonProxy {


    public static Configuration configuration;

    public CommonProxy(){

    }

    public void preInit(FMLPreInitializationEvent e) {
        File dir = e.getModConfigurationDirectory();
        configuration = new Configuration(new File(dir.getPath(),"rftotime.cfg"));
        RFTTConfig.readConfig();

    }

    public void init(FMLInitializationEvent e) {
    }
    public void postInit(FMLPostInitializationEvent e) {

        if(configuration.hasChanged()){
            configuration.save();
        }

    }

  public void renderObject(){}
}
