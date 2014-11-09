package eu.matejkormuth.pexel.master;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class NettyMessageDecoder extends MessageToMessageDecoder<ByteBuf> {
    
    @Override
    protected void decode(final ChannelHandlerContext paramChannelHandlerContext,
            final ByteBuf message, final List<Object> out) throws Exception {
        out.add(new NettyMessage(message.array()));
    }
    
}
