package com.floweytf.bettercreativeitems.tileentity;

import com.floweytf.bettercreativeitems.ModConfig;
import com.floweytf.bettercreativeitems.caps.CreativeEnergyStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class EnergyTileEntity extends TileEntity implements ITickable {
    private final IEnergyStorage cap = new CreativeEnergyStorage();

    private void insertPower(TileEntity tileEntity) {
        IEnergyStorage cap = null;
        for(EnumFacing facing : EnumFacing.VALUES) {
            cap = tileEntity.getCapability(CapabilityEnergy.ENERGY, facing);
            if(cap != null)
                break;
        }

        if(cap == null)
            return;
        for(int i = 0; i < ModConfig.energy.cyclesPerTick; i++)
            cap.receiveEnergy(Integer.MAX_VALUE, false);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityEnergy.ENERGY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityEnergy.ENERGY)
            return (T) cap; // ur capping

        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        // iterate all blockpos in 3 block radius
        BlockPos pos = getPos();
        Iterable<BlockPos> blockInBoxSet = BlockPos.getAllInBox(
            pos.add(ModConfig.energy.radius, ModConfig.energy.radius, ModConfig.energy.radius),
            pos.add(-ModConfig.energy.radius, -ModConfig.energy.radius, -ModConfig.energy.radius)
        );
        blockInBoxSet.forEach(blockPos -> {
            if(isValidPos(blockPos)) {
                TileEntity entity = world.getTileEntity(blockPos);
                if(entity != null)
                    insertPower(entity);
            }
        });
    }

    private boolean isValidPos(BlockPos pos) {
        if(pos.getY() < 0 && pos.getY() > 256)
            return false;
        return world.isBlockLoaded(pos);
    }
}
