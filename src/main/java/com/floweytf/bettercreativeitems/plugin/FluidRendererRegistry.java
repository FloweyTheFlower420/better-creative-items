package com.floweytf.bettercreativeitems.plugin;

import com.floweytf.bettercreativeitems.api.IFluidRenderer;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Comparator;

import static com.floweytf.bettercreativeitems.Constants.FLUID_ID_EMPTY;

public class FluidRendererRegistry {
    private static final ArrayList<IFluidRenderer> FLUIDS = new ArrayList<>();
    private static final BiMap<ResourceLocation, IFluidRenderer> ID_TO_FLUIDS = HashBiMap.create();

    public static IFluidRenderer get(int index) {
        return FLUIDS.get(index);
    }

    public static void register(ResourceLocation id, IFluidRenderer renderer) {
        ID_TO_FLUIDS.put(id, renderer);
        FLUIDS.add(renderer);
    }

    public static IFluidRenderer get(ResourceLocation id) {
        if (ID_TO_FLUIDS.containsKey(id)) {
            return ID_TO_FLUIDS.get(id);
        }
        return FluidRenderer.EMPTY;
    }

    public static IFluidRenderer get(String id) {
        return get(new ResourceLocation(id));
    }

    public static ResourceLocation get(IFluidRenderer renderer) {
        if (ID_TO_FLUIDS.inverse().containsKey(renderer)) {
            return ID_TO_FLUIDS.inverse().get(renderer);
        }
        return FLUID_ID_EMPTY;
    }

    public static int size() {
        return FLUIDS.size();
    }

    public static ImmutableList<IFluidRenderer> getList() {
        return ImmutableList.copyOf(FLUIDS);
    }

    public static void sort() {
        FLUIDS.sort(Comparator.comparing(IFluidRenderer::getName));
    }
}
