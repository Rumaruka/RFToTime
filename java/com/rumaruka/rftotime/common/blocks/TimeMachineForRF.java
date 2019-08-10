package com.rumaruka.rftotime.common.blocks;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.items.AllItems;
import com.cwelth.intimepresence.renderers.TimeMachineTESR;
import com.cwelth.intimepresence.tileentities.CommonTEBlock;

import com.rumaruka.rftotime.client.renderer.TimeMachineTESRForRF;
import com.rumaruka.rftotime.common.tiles.RFShardProcessorTE;
import com.rumaruka.rftotime.common.tiles.TimeMachineTEForRF;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TimeMachineForRF extends CommonTEBlock<TimeMachineTEForRF> {
    public static final IProperty<Boolean> IS_CASE_RAISED = PropertyBool.create("is_case_raised");
    public static final IProperty<Boolean> IS_OFFLINE = PropertyBool.create("is_offline");

    public TimeMachineForRF() {
        super(Material.IRON, "timemachine_rf");
        setCreativeTab(ModMain.itpCreativeTab);
        setHardness(.5F);
        setHarvestLevel("pickaxe", 2);

        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(IS_CASE_RAISED, false).withProperty(IS_OFFLINE, true));
    }

    @Override
    public Class<TimeMachineTEForRF> getTileEntityClass() {
        return TimeMachineTEForRF.class;
    }

    @Nullable
    @Override
    public TimeMachineTEForRF createTileEntity(World worldIn, IBlockState meta) {
        return new TimeMachineTEForRF();
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        ClientRegistry.bindTileEntitySpecialRenderer(TimeMachineTEForRF.class, new TimeMachineTESRForRF());
    }

    @SideOnly(Side.CLIENT)
    public void initItemModel() {

        Item itemBlock = Item.REGISTRY.getObject(new ResourceLocation(ModMain.MODID, "timemachine"));
        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(getRegistryName(), "inventory");
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlock, 0, itemModelResourceLocation);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, IS_CASE_RAISED, IS_OFFLINE);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TimeMachineTEForRF te = (TimeMachineTEForRF)worldIn.getTileEntity(pos);
        if(te != null) {
            //ModMain.logger.info("getActualState: " + te.isOffline + ", isRemote: " + te.getWorld().isRemote);
            return state.withProperty(IS_CASE_RAISED, false).withProperty(IS_OFFLINE, te.isOffline);
        }
        else
            return state.withProperty(IS_CASE_RAISED, false).withProperty(IS_OFFLINE, true);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {
            TimeMachineTEForRF te = (TimeMachineTEForRF) worldIn.getTileEntity(pos);

            attachTE(worldIn, pos);
            if (worldIn.isBlockPowered(pos)) {
                te.setActive(true);
            } else {
                te.setActive(false);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TimeMachineTEForRF te = (TimeMachineTEForRF) worldIn.getTileEntity(pos);
            ItemStack heldItem = playerIn.getHeldItem(hand);
            if (!playerIn.isSneaking()) {
                if (te.caseLevel == 34) {
                    if (te.caseSlot.isEmpty()) {
                        if (heldItem.getItem() == AllItems.timeBattery) {
                            playerIn.setHeldItem(hand, te.useItem(heldItem));
                            return true;
                        }
                    } else {
                        if (heldItem.isEmpty()) {
                            if (!te.caseSlot.isEmpty()) {
                                playerIn.setHeldItem(hand, te.useItem(heldItem));
                                return true;
                            }
                        }
                    }
                }
                return true;
            }
        }
        return true;
    }

    private void attachTE(IBlockAccess worldIn, BlockPos pos) {
        TimeMachineTEForRF te = (TimeMachineTEForRF) worldIn.getTileEntity(pos);
        if (te != null) {
            TileEntity north = worldIn.getTileEntity(pos.north());
            TileEntity east = worldIn.getTileEntity(pos.east());
            TileEntity south = worldIn.getTileEntity(pos.south());
            TileEntity west = worldIn.getTileEntity(pos.west());
            if (north instanceof RFShardProcessorTE) {
                te.attachedTE = pos.north();
            } else if (east instanceof RFShardProcessorTE) {
                te.attachedTE = pos.east();
            } else if (south instanceof RFShardProcessorTE) {
                te.attachedTE = pos.south();
            } else if (west instanceof RFShardProcessorTE) {
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
        if (!world.isRemote) {
            attachTE(world, pos);

        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if(!worldIn.isRemote)
        {
            TimeMachineTEForRF te = (TimeMachineTEForRF)worldIn.getTileEntity(pos);

            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), te.caseSlot);
        }

    }
}
