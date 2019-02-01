package tools.netty.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

public class TimeDecoder extends ByteToMessageDecoder { // (1)
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) { // (2)
        if (in.readableBytes() < 1) {
            return; // (3)
        }
        String data = in.readBytes(in.readableBytes()).toString(Charset.forName("utf-8"));
        out.add(data);
        System.out.println("decode出来的结果："+data);
        ByteBuf frame = in.retainedDuplicate();
        in.skipBytes(in.readableBytes());
    }
}