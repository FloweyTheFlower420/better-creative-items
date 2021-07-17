package com.floweytf.bettercreativeitems.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.PacketUtil;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

// C -> S
public class CreativeInventoryPacket implements IMessage {
    public static class MessageHandler implements IMessageHandler<CreativeInventoryPacket, IMessage> {
        @Override
        public IMessage onMessage(CreativeInventoryPacket message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            ItemStack itemstack = message.stack;
            boolean isDrop = message.slotId < 0;
            boolean isValidSlot = message.slotId >= 1 && message.slotId <= 45;
            boolean isValid = itemstack.isEmpty() || itemstack.getMetadata() >= 0 && itemstack.getCount() <= 64 && !itemstack.isEmpty();

            if (isValidSlot && isValid)
            {
                if (itemstack.isEmpty())
                    player.inventoryContainer.putStackInSlot(message.slotId, ItemStack.EMPTY);
                else
                    player.inventoryContainer.putStackInSlot(message.slotId, itemstack);

                player.inventoryContainer.setCanCraft(player, true);
            }
            else if (isDrop && isValid /*&& this.itemDropThreshold < 200*/)
            {
                //this.itemDropThreshold += 20;
                EntityItem entityitem = player.dropItem(itemstack, true);

                if (entityitem != null)
                    entityitem.setAgeToCreativeDespawnTime();
            }

            return null;
        }
    }

    private int slotId;
    private ItemStack stack;

    // A default constructor is always required
    public CreativeInventoryPacket() {
    }

    public CreativeInventoryPacket(int slotIdIn, ItemStack stackIn) {
        this.slotId = slotIdIn;
        this.stack = stackIn.copy();
    }

    public static void sendPacket(int slotId, ItemStack stack) {
        PacketHandler.INSTANCE.sendToServer(new CreativeInventoryPacket(slotId, stack));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeShort(slotId);
        PacketUtil.writeItemStackFromClientToServer(buffer, stack);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        this.slotId = buffer.readShort();
        try {
            this.stack = buffer.readItemStack();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
