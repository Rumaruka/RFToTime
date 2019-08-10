package com.rumaruka.rftotime.network;
import com.rumaruka.rftotime.common.tiles.RFShardProcessorTE;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
public class SyncRFShardProcessor implements IMessage {
    public BlockPos tePos;
    public ItemStack[] stacks = new ItemStack[2];

    public SyncRFShardProcessor(){}

    public SyncRFShardProcessor(RFShardProcessorTE te)
    {
        this.tePos = te.getPos();

        stacks[0] = te.dimshardsSlot.getStackInSlot(0);
        stacks[1] = te.enderPearlSlot.getStackInSlot(0);

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tePos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        for(int i = 0; i < 2; i++)
            this.stacks[i] = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(tePos.getX());
        buf.writeInt(tePos.getY());
        buf.writeInt(tePos.getZ());

        for(int i = 0; i < 2; i++)
            ByteBufUtils.writeItemStack(buf, stacks[i]);
    }

    public static class Handler implements IMessageHandler<SyncRFShardProcessor, IMessage> {
        @Override
        public IMessage onMessage(SyncRFShardProcessor message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft mc = Minecraft.getMinecraft();
                RFShardProcessorTE te = (RFShardProcessorTE)mc.world.getTileEntity(message.tePos);
                if(te != null) {

                    te.dimshardsSlot.setStackInSlot(0, message.stacks[0]);
                    te.enderPearlSlot.setStackInSlot(0, message.stacks[1]);
                }
            });
            return null;
        }
    }
}