package com.floweytf.bettercreativeitems.block;

import com.floweytf.bettercreativeitems.ModMain;
import com.floweytf.bettercreativeitems.tileentity.FluidTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.floweytf.bettercreativeitems.Constants.GUI_ID_FLUID;

@SuppressWarnings("NullableProblems")
public class FluidBlock extends BlockBase {
    public FluidBlock() {
        super(Material.BARRIER, "fluid");
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
        TileEntity te = worldIn.getTileEntity(pos);

        if (!(te instanceof FluidTileEntity)) {
            return true;
        }
        if (worldIn.isRemote) {
            return true;
        }

        playerIn.openGui(ModMain.instance, GUI_ID_FLUID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
}