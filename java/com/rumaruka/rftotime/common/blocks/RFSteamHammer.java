package com.rumaruka.rftotime.common.blocks;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.tileentities.CommonTEBlock;
import com.rumaruka.rftotime.api.Const;
import com.rumaruka.rftotime.api.GUIs;
import com.rumaruka.rftotime.common.tiles.RFSteamHammerTE;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class RFSteamHammer extends CommonTEBlock<RFSteamHammerTE> {

    public static final IProperty<Boolean> IS_PISTON = PropertyBool.create("is_piston");
    public static final IProperty<RFSteamHammer.BlockPart> HALF = PropertyEnum.create("half", RFSteamHammer.BlockPart.class);

    AxisAlignedBB aabbBottomBlock = new AxisAlignedBB(0, 0, 0, 1, 2, 1);
    AxisAlignedBB aabbTopBlock = new AxisAlignedBB(0, -1, 0, 1, 1, 1);
    public RFSteamHammer() {
        super(Material.IRON, "rf_steamhammer");
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        if(state.getValue(HALF) == BlockPart.UPPER) return false;
        else return super.hasTileEntity(state);
    }


    @Override
    public Class<RFSteamHammerTE> getTileEntityClass() {
        return RFSteamHammerTE.class;
    }

    @Nullable
    @Override
    public RFSteamHammerTE createTileEntity(World world, IBlockState iBlockState) {
        return new RFSteamHammerTE();
    }
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @SideOnly(Side.CLIENT)
    public static void initItemModel() {

        Item itemBlock = Item.REGISTRY.getObject(new ResourceLocation(ModMain.MODID, "steamhammer"));
        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("steamhammer", "inventory");
        final int DEFAULT_ITEM_SUBTYPE = 0;
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlock, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        worldIn.setBlockToAir(pos);
        dropBlockAsItem(worldIn, pos, state, 0);
        if(state.getValue(HALF) == BlockPart.UPPER)
            worldIn.setBlockToAir(pos.down());
        else
            worldIn.setBlockToAir(pos.up());
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }


    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this,FACING,IS_PISTON,HALF);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(IS_PISTON,false);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        if(state.getActualState(worldIn, pos).getValue(HALF) == BlockPart.LOWER)
            addCollisionBoxToList(pos, entityBox, collidingBoxes, aabbBottomBlock);
        else
            addCollisionBoxToList(pos, entityBox, collidingBoxes, aabbTopBlock);
    }
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if(state.getActualState(source, pos).getValue(HALF) == BlockPart.LOWER)
            return aabbBottomBlock;
        else
            return aabbTopBlock;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            pos = (state.getValue(HALF) == BlockPart.LOWER)? pos: pos.down();
            RFSteamHammerTE te = (RFSteamHammerTE) worldIn.getTileEntity(pos);
            ItemStack heldItem = playerIn.getHeldItem(hand);
            if(!playerIn.isSneaking()) {
                if (!heldItem.isEmpty()) {
                    if(heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                        if (FluidUtil.interactWithFluidHandler(playerIn, hand, te.waterTank)) {
                        }
                        return true;
                    }
                }
                if(te.prepareGUIToBeOpened(true))
                    playerIn.openGui(Const.MODID, GUIs.rfsteamhammer.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
                return true;
            }
        }
        return true;
    }
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if(!worldIn.isRemote) {
            if (state.getValue(HALF) == BlockPart.LOWER) {
                worldIn.setBlockState(pos.up(), this.blockState.getBaseState().withProperty(FACING, state.getValue(FACING)).withProperty(IS_PISTON, false).withProperty(HALF, BlockPart.UPPER));
                worldIn.notifyNeighborsOfStateChange(pos.up(), this, false);
            }
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)).withProperty(HALF, BlockPart.values()[(meta & 8) >> 3]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(FACING).getIndex() & 7;
        if(state.getValue(HALF) == BlockPart.UPPER)meta |= 8;
        return meta;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if(!worldIn.isRemote)
        {
            if(state.getValue(HALF) == BlockPart.LOWER) {
                RFSteamHammerTE te = (RFSteamHammerTE) worldIn.getTileEntity(pos);

                if (te != null) {
                    InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), te.itemStackHandler.getStackInSlot(0));
                    InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), te.outputHandler.getStackInSlot(0));
                    InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), te.bucketHandler.getStackInSlot(0));
                }
            }
        }

    }
    public enum BlockPart implements IStringSerializable {
        LOWER,
        UPPER;

        public String toString()
        {
            return this.getName();
        }

        @Override
        public String getName() {
            return (this == UPPER)? "upper" : "lower";
        }
    }
}
