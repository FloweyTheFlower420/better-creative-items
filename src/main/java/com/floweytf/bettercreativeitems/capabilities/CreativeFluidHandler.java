package com.floweytf.bettercreativeitems.capabilities;

import com.floweytf.bettercreativeitems.api.IFluidRenderer;
import com.floweytf.bettercreativeitems.plugin.FluidRenderer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class CreativeFluidHandler implements IFluidHandler {
    public IFluidRenderer fluidRenderer = FluidRenderer.EMPTY;

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[]{
            new IFluidTankProperties() {
                @Nullable
                @Override
                public FluidStack getContents() {
                    if (fluidRenderer == null) {
                        return null;
                    }
                    return fluidRenderer.getAsStack(Integer.MAX_VALUE);
                }

                @Override
                public int getCapacity() {
                    return Integer.MAX_VALUE;
                }

                @Override
                public boolean canFill() {
                    return false;
                }

                @Override
                public boolean canDrain() {
                    return true;
                }

                @Override
                public boolean canFillFluidType(FluidStack fluidStack) {
                    return false;
                }

                @Override
                public boolean canDrainFluidType(FluidStack fluidStack) {
                    if (fluidRenderer == null) {
                        return false;
                    }
                    return fluidRenderer.canDrainFluidType(fluidStack);
                }
            }
        };
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (fluidRenderer == null) {
            return null;
        }
        if (fluidRenderer.canDrainFluidType(resource)) {
            return resource;
        }
        return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (fluidRenderer == null) {
            return null;
        }
        return fluidRenderer.getAsStack(maxDrain);
    }
}