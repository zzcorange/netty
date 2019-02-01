package tools.netty.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.nio.charset.Charset;

public class TimeEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ByteBuf encoded = ctx.alloc().buffer(1);
        encoded.writeCharSequence(msg.toString(), Charset.forName("utf8"));
        System.out.println("encodeMessage:"+msg);
        ctx.write(encoded, promise); // (1)
    }
}