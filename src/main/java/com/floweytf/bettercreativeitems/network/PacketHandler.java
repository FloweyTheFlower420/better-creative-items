package com.floweytf.bettercreativeitems.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import static com.floweytf.bettercreativeitems.Constants.MOD_ID;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE =
        NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);

    public static void register() {
        int packetId = 0;
        // register messages from client to server
        INSTANCE.registerMessage(SyncFluidPacket.MessageHandler.class, SyncFluidPacket.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(CreativeInventoryPacket.MessageHandler.class, CreativeInventoryPacket.class, packetId++, Side.SERVER);
    }
}
