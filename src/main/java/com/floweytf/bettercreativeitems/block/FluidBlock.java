package com.floweytf.bettercreativeitems.block;

import static com.floweytf.bettercreativeitems.Constants.*;
import com.floweytf.bettercreativeitems.ModMain;
import com.floweytf.bettercreativeitems.tileentity.FluidTileEntity;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@SuppressWarnings("NullableProblems")
public class FluidBlock extends BlockContainer {
    public FluidBlock() {
        super(Material.BARRIER);
        setRegistryName(id("fluid"));
        setUnlocalizedName(MOD_ID + "." + "fluid");
        setCreativeTab(CreativeTabs.MISC);
        ModMain.proxy.registerModel(Item.getItemFromBlock(this), 0);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new FluidTileEntity();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY)
    {
        System.out.println("Thing");
        TileEntity te = worldIn.getTileEntity(pos);

        if (!(te instanceof FluidTileEntity))
            return true;
        if (worldIn.isRemote)
            return true;

        playerIn.openGui(ModMain.instance, GUI_ID_FLUID, worldIn, pos.getX(), pos.getY(), pos.getZ());

        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new FluidTileEntity();
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return true;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
