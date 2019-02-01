package tools;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import net.sf.json.JSONObject;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.rtsp.RtspHeaderNames.CONTENT_TYPE;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler  extends
        SimpleChannelInboundHandler<FullHttpRequest> {
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
        // 构造返回数据
        JSONObject jsonRootObj = new JSONObject();
        JSONObject jsonUserInfo = new JSONObject();
        jsonUserInfo.put("id", 1);
        jsonUserInfo.put("name", "张三");
        jsonUserInfo.put("password", "123");
        jsonRootObj.put("userInfo", jsonUserInfo);
        // 获取传递的数据
//        Map<String, Object> params = getParamsFromChannel(ctx, fullHttpRequest);
//        jsonRootObj.put("params", params);

        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.ACCEPTED);
        response.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");
        StringBuilder bufRespose = new StringBuilder();
        bufRespose.append(jsonRootObj.toString());
        ByteBuf buffer = Unpooled.copiedBuffer(bufRespose, CharsetUtil.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    } // (1)


}