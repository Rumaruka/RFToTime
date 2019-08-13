package com.rumaruka.rftotime.init;

import com.cwelth.intimepresence.blocks.AllBlocks;
import com.rumaruka.rftotime.api.RecipeAPI;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RFTTRecipes {

    public static void setup(){

        RecipeAPI.addShapedRecipe(new ItemStack(RFTTBlocks.time_machine_rf),new Object[]{
                " r ",
                "rTr",
                " r " ,'r',new ItemStack(Items.REDSTONE),'T', new ItemStack(AllBlocks.timeMachine)
        });

        RecipeAPI.addShapedRecipe(new ItemStack(AllBlocks.timeMachine),new Object[]{
                "   ",
                " T ",
                "   " ,'T', new ItemStack(RFTTBlocks.time_machine_rf)

        });

        RecipeAPI.addShapedRecipe(new ItemStack(RFTTBlocks.rfShardProcessor),new Object[]{
                "RRR",
                "RSR",
                "ppp"
                ,'R',new ItemStack(Blocks.REDSTONE_BLOCK),'S', new ItemStack(AllBlocks.shardProcessor),'p',new ItemStack(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE)

        });


        RecipeAPI.addShapedRecipe(new ItemStack(RFTTBlocks.rfsteamhammer),new Object[]{
                "SSS",
                "RHR",
                "ppp"

                ,'R',new ItemStack(Blocks.REDSTONE_BLOCK),'H',new ItemStack(AllBlocks.steamHammer),'S', new ItemStack(AllBlocks.crudeIronBlock),'p',new ItemStack(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE)
        });

    }
}
