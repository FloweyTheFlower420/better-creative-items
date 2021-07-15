package com.floweytf.bettercreativeitems.network;

import com.floweytf.bettercreativeitems.ModMain;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE =
        NetworkRegistry.INSTANCE.newSimpleChannel(ModMain.MODID);

}
