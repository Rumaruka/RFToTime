package com.rumaruka.rftotime.init;



import com.cwelth.intimepresence.ModMain;
import com.rumaruka.rftotime.api.Const;
import com.rumaruka.rftotime.client.gui.RFTTCreativeTabs;
import com.rumaruka.rftotime.common.blocks.RFShardProcessor;
import com.rumaruka.rftotime.common.blocks.RFSteamHammer;
import com.rumaruka.rftotime.common.blocks.TimeMachineForRF;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderException;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraft.block.Block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import com.google.common.collect.ObjectArrays;
import java.lang.reflect.Constructor;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import com.google.common.base.Strings;
public class RFTTBlocks {



    public static Block rfShardProcessor;
    public static Block time_machine_rf;
    public static Block rfsteamhammer;


    public static void init(){
        rfShardProcessor = new RFShardProcessor().setCreativeTab(RFTTCreativeTabs.rfttCreativeTabs);
        time_machine_rf = new TimeMachineForRF().setCreativeTab(RFTTCreativeTabs.rfttCreativeTabs);
        rfsteamhammer = new RFSteamHammer().setCreativeTab(RFTTCreativeTabs.rfttCreativeTabs);

    }
    public static void InGameRegister(){
        registerBlock(rfShardProcessor);
        registerBlock(time_machine_rf);
        registerBlock(rfsteamhammer);
    }


    public static void renderBlocks(){
        registerRender(rfShardProcessor);
        initShardProcessorItem(rfShardProcessor);
        registerRender(time_machine_rf);
        initTMForRFItem(time_machine_rf);
        registerRender(rfsteamhammer);
        initModelHammerItem(rfsteamhammer);



    }


    @Deprecated
    public static Block registerBlock(Block block)
    {
        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        return block;
    }


    @Deprecated
    public static Block registerBlock(Block block, String name)
    {
        if (block.getRegistryName() == null && Strings.isNullOrEmpty(name))
            throw new IllegalArgumentException("Attempted to register a Block with no name: " + block);
        if (block.getRegistryName() != null && !block.getRegistryName().toString().equals(name))
            throw new IllegalArgumentException("Attempted to register a Block with conflicting names. Old: " + block.getRegistryName() + " New: " + name);
        return registerBlock(block.getRegistryName() != null ? block : block.setRegistryName(name));
    }
    @Deprecated
    public static Block registerBlock(Block block, Class<? extends ItemBlock> itemclass, String name, Object... itemCtorArgs)
    {
        if (Strings.isNullOrEmpty(name))
        {
            throw new IllegalArgumentException("Attempted to register a block with no name: " + block);
        }
        if (Loader.instance().isInState(LoaderState.CONSTRUCTING))
        {
            FMLLog.warning("The mod %s is attempting to register a block whilst it it being constructed. This is bad modding practice - please use a proper mod lifecycle event.", Loader.instance().activeModContainer());
        }
        try
        {
            assert block != null : "registerBlock: block cannot be null";
            if (block.getRegistryName() != null && !block.getRegistryName().toString().equals(name))
                throw new IllegalArgumentException("Attempted to register a Block with conflicting names. Old: " + block.getRegistryName() + " New: " + name);
            ItemBlock i = null;
            if (itemclass != null)
            {
                Class<?>[] ctorArgClasses = new Class<?>[itemCtorArgs.length + 1];
                ctorArgClasses[0] = Block.class;
                for (int idx = 1; idx < ctorArgClasses.length; idx++)
                {
                    ctorArgClasses[idx] = itemCtorArgs[idx - 1].getClass();
                }
                Constructor<? extends ItemBlock> itemCtor = itemclass.getConstructor(ctorArgClasses);
                i = itemCtor.newInstance(ObjectArrays.concat(block, itemCtorArgs));
            }
            // block registration has to happen first
            ForgeRegistries.BLOCKS.register(block.getRegistryName() == null ? block.setRegistryName(name) : block);
            if (i != null)
                ForgeRegistries.ITEMS.register(i.setRegistryName(name));
            return block;
        } catch (Exception e)
        {
            FMLLog.log(Level.ERROR, e, "Caught an exception during block registration");
            throw new LoaderException(e);
        }
    }

    public static void registerRender(Block block)
    {
        Item item = Item.getItemFromBlock(block);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(Const.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
    }
    public static void initModelHammerItem(Block block){
        Item item = Item.REGISTRY.getObject(new ResourceLocation(Const.MODID,"rf_steamhammer"));

        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(block.getRegistryName(), "inventory");
        final int DEFAULT_ITEM_SUBTYPE = 0;
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
    }

    public static void initTMForRFItem(Block block){
        Item item = Item.REGISTRY.getObject(new ResourceLocation(Const.MODID,"timemachine_rf"));

        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(block.getRegistryName(), "inventory");
        final int DEFAULT_ITEM_SUBTYPE = 0;
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
    }

    public static void initShardProcessorItem(Block block){
        Item item = Item.REGISTRY.getObject(new ResourceLocation(Const.MODID,"rfshardprocessor"));

        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(block.getRegistryName(), "inventory");
        final int DEFAULT_ITEM_SUBTYPE = 0;
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
    }
}
