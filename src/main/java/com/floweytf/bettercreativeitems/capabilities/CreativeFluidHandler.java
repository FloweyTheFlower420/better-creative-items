package com.floweytf.bettercreativeitems.capabilities;

import com.floweytf.bettercreativeitems.utils.FluidRenderer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import scala.Int;

import javax.annotation.Nullable;

public class CreativeFluidHandler implements IFluidHandler {
    public FluidRenderer fluid = new FluidRenderer();

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[]{
            new IFluidTankProperties() {
                @Nullable
                @Override
                public FluidStack getContents() {
                    if (fluid == null) {
                        return null;
                    }
                    return fluid.getAsStack(Integer.MAX_VALUE);
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
                    if (fluid == null) {
                        return false;
                    }
                    return fluid.canDrainFluidType(fluidStack);
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
        if (fluid == null) {
            return null;
        }
        if (fluid.canDrainFluidType(resource)) {
            return resource;
        }
        return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (fluid == null) {
            return null;
        }
        return fluid.getAsStack(maxDrain);
    }
}