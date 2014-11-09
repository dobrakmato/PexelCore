package eu.matejkormuth.pexel.network;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final NettyMessage message,
            final List<Object> out) throws Exception {
        out.add(Unpooled.copiedBuffer(message.payload));
    }
}
