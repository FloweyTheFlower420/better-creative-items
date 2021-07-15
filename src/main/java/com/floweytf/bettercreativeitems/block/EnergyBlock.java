package com.floweytf.bettercreativeitems.block;

import com.floweytf.bettercreativeitems.tileentity.EnergyTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@SuppressWarnings("NullableProblems")
public class EnergyBlock extends BlockBase {
    public EnergyBlock() {
        super(Material.BARRIER, "energy");
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    public TileEntity createTileEntity(World world, IBlockState state) {
        return new EnergyTileEntity();
    }
}
