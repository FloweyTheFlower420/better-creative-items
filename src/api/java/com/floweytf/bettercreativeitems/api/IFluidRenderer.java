package com.floweytf.bettercreativeitems.api;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;

public interface IFluidRenderer {
    /**
     * This WILL throw a NPE if you don't check isEmpty()
     * @return The localized name of the Fluid
     */
    String getName();

    /**
     * @param x
     * @param y
     * @param container
     */
    void draw(int x, int y, GuiContainer container);

    boolean isEmpty();

    FluidStack getAsStack(int amount);

    boolean canDrainFluidType(FluidStack other);
}
