package com.floweytf.bettercreativeitems.registry;

import com.floweytf.bettercreativeitems.plugin.FluidRenderer;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class PluginRegistry {
    private final Map<String, FluidRenderer> delegate = new HashMap<>();

    public <T extends FluidRenderer> void register(ResourceLocation resource, T value) {
        delegate.put(resource.toString(), value);
    }

}
