package com.floweytf.bettercreativeitems.utils;

import com.floweytf.bettercreativeitems.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class Utils {
    public static String translationKey(String key, String id, String name) {
        return key + '.' + Constants.MOD_ID + '.' + id + '.' + name;
    }

    public static String getFluidName(Fluid fluid) {
        return fluid.getLocalizedName(new FluidStack(fluid, 1));
    }
}
