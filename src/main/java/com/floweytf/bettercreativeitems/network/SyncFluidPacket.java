package com.floweytf.bettercreativeitems.network;

import com.floweytf.bettercreativeitems.tileentity.FluidTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.StandardCharsets;

public class SyncFluidPacket implements IMessage {
    public static class MessageHandler implements IMessageHandler<SyncFluidPacket, IMessage> {
        @Override
        public IMessage onMessage(SyncFluidPacket message, MessageContext ctx) {
            WorldServer world = ctx.getServerHandler().player.getServerWorld();

            if (!world.isBlockLoaded(message.pos)) {
                return null;
            }
            TileEntity te = world.getTileEntity(message.pos);
            if (!(te instanceof FluidTileEntity)) {
                return null;
            }
            FluidTileEntity fte = (FluidTileEntity) te;
            fte.setFluid(message.fluidID);
            return null;
        }
    }

    // A default constructor is always required
    public SyncFluidPacket() {
    }

    private String fluidID;
    private BlockPos pos;

    public SyncFluidPacket(String fluidID, BlockPos pos) {
        this.fluidID = fluidID;
        this.pos = pos;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        String id = fluidID;
        byte[] bytes = id.getBytes(StandardCharsets.UTF_8);

        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        byte[] bytes = new byte[buf.readInt()];
        buf.readBytes(bytes);
        fluidID = new String(bytes, StandardCharsets.UTF_8);
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }
}
