package com.floweytf.bettercreativeitems.api;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginRegistry {
    private final BiMap<ResourceLocation, IFluidRenderer> handlers = HashBiMap.create();
    private final Map<Fluid, List<IFluidRenderer>> fluidToHandlers = new HashMap<>();

    public void register(ResourceLocation id, Fluid handledFluid, IFluidRenderer handler) {
        handlers.put(id, handler);
        fluidToHandlers.computeIfAbsent(handledFluid, k -> new ArrayList<>());
        fluidToHandlers.get(handledFluid).add(handler);
    }
}
