package com.rumaruka.rftotime.common.blocks;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.tileentities.CommonTEBlock;
import com.cwelth.intimepresence.tileentities.TimeMachineTE;
import com.rumaruka.rftotime.api.Const;
import com.rumaruka.rftotime.api.GUIs;
import com.rumaruka.rftotime.client.renderer.RFShardProcessorTESR;
import com.rumaruka.rftotime.common.tiles.RFShardProcessorTE;
import com.rumaruka.rftotime.common.tiles.TimeMachineTEForRF;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class RFShardProcessor extends CommonTEBlock<RFShardProcessorTE> {


    public RFShardProcessor() {
        super(Material.IRON,"rfshardprocessor");

    }

    @Override
    public Class<RFShardProcessorTE> getTileEntityClass() {
        return RFShardProcessorTE.class;
    }

    @Nullable
    @Override
    public RFShardProcessorTE createTileEntity(World world, IBlockState iBlockState) {
        return new RFShardProcessorTE();
    }
    @SideOnly(Side.CLIENT)
    public static void initItemModel() {

        Item itemBlock = Item.REGISTRY.getObject(new ResourceLocation(ModMain.MODID, "shardprocessor"));
        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("shardprocessor", "inventory");
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlock, 0, itemModelResourceLocation);
    }
    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
        ClientRegistry.bindTileEntitySpecialRenderer(RFShardProcessorTE.class,new RFShardProcessorTESR());
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            if (!playerIn.isSneaking()) {
                RFShardProcessorTE te = (RFShardProcessorTE)worldIn.getTileEntity(pos);
                if(te != null) {
                    if(te.prepareGUIToBeOpened(true))
                        playerIn.openGui(Const.MODID, GUIs.rfshardprocessor.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
                    return true;
                }
            }
        }
        return true;
    }
    private void attachTE(IBlockAccess worldIn, BlockPos pos)
    {
        RFShardProcessorTE te = (RFShardProcessorTE)worldIn.getTileEntity(pos);
        if(te != null)
        {
            TileEntity north = worldIn.getTileEntity(pos.north());
            TileEntity east = worldIn.getTileEntity(pos.east());
            TileEntity south = worldIn.getTileEntity(pos.south());
            TileEntity west = worldIn.getTileEntity(pos.west());
            if(north instanceof TimeMachineTEForRF)
            {
                te.attachedTE = pos.north();
            } else if(east instanceof TimeMachineTEForRF)
            {
                te.attachedTE = pos.east();
            } else if(south instanceof TimeMachineTEForRF)
            {
                te.attachedTE = pos.south();
            } else if(west instanceof TimeMachineTEForRF)
            {
                te.attachedTE = pos.west();
            } else
                te.attachedTE = null;
            te.markDirty();
            te.getWorld().notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 2);

        }

    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if(!world.isRemote)
        {
            attachTE(world, pos);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if(!worldIn.isRemote)
        {
            attachTE(worldIn, pos);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if(!worldIn.isRemote)
        {
            RFShardProcessorTE te = (RFShardProcessorTE)worldIn.getTileEntity(pos);


            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), te.enderPearlSlot.getStackInSlot(0));
            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), te.dimshardsSlot.getStackInSlot(0));
        }
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}

