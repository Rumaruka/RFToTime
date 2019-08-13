package com.rumaruka.rftotime;


import com.cwelth.intimepresence.ModMain;
import com.rumaruka.rftotime.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

import java.util.logging.Level;

public class RFTTConfig{


    public static int MAX_POWER_SHARDPROCESSOR ;
    public static int MAX_RECIEVE_SHARDPROCESSOR ;

    public static int MAX_POWER_STEAMHAMMER ;
    public static int MAX_RECIEVE_STEAMHAMMER ;


    public RFTTConfig() {
    }

    public static void readConfig(){
        Configuration cfg = CommonProxy.configuration;
        try {
            cfg.load();
            initGeneralCFG(cfg);
        }
        catch (Exception e){
            ModMain.logger.log(Level.WARNING, "Problem loading config file!", e);
        }
        finally {
            if(cfg.hasChanged()){
                cfg.save();
            }
        }
    }

    public static void initGeneralCFG(Configuration c) {
        c.addCustomCategoryComment("general","Energy Cutomized Power");
        MAX_POWER_SHARDPROCESSOR = c.getInt("Max Power for Shard Processor","general", 100_000,1_000,Integer.MAX_VALUE,"");
        MAX_POWER_STEAMHAMMER = c.getInt("Max Power for SteamHammer","general", 100_000,1_000,Integer.MAX_VALUE,"");
        MAX_RECIEVE_SHARDPROCESSOR = c.getInt("Max Recieve for Shard Processor","general", 25,10,Integer.MAX_VALUE,"");
        MAX_RECIEVE_STEAMHAMMER = c.getInt("Max Recieve for SteamHammer","general", 100,10,Integer.MAX_VALUE,"");
    }

}
