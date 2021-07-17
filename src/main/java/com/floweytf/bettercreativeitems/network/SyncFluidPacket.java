package com.floweytf.bettercreativeitems.network;

import com.floweytf.bettercreativeitems.tileentity.FluidTileEntity;
import com.floweytf.bettercreativeitems.utils.FluidRenderer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.StandardCharsets;

// C -> S
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
            fte.setFluidRenderer(message.fluid);
            return null;
        }
    }

    // A default constructor is always required
    public SyncFluidPacket() {
    }

    private FluidRenderer fluid;
    private BlockPos pos;

    public SyncFluidPacket(FluidRenderer fluid, BlockPos pos) {
        this.fluid = fluid;
        this.pos = pos;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        fluid.writeToByteBuf(buffer);
        buffer.writeBlockPos(pos);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        fluid = new FluidRenderer();
        fluid.readFromByteBuf(buffer);
        pos = buffer.readBlockPos();
    }
}
