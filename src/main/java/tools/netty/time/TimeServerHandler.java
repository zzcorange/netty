package tools.netty.time;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.sf.json.JSONObject;
import tools.netty.entity.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    Map<String,ChannelHandlerContext> clients = new HashMap<>();
    public static ChannelHandlerContext temp =null;
    public static ArrayList<String> msgs = new ArrayList<>();
    public static volatile  String msg="";
    @Override
    public void channelActive(final ChannelHandlerContext ctx) { // (1)
//        while(true){
//            if(msg.length()>0){
//                ctx.writeAndFlush(msg);
//                msg = "";
//            }
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        System.out.println("客户端连接成功");
//        int i=0;
//        while (true){
//            System.out.println(new UnixTime());
//            ctx.writeAndFlush(new UnixTime());
//            i++;
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(((Message)msg).getMessage().indexOf("ping")==-1)
        System.out.println("服务端收到的返回："+((Message)msg).toString());
        JSONObject jsonObject = JSONObject.fromObject((Message)msg);
        TimeServer.channelMap.put(((Message) msg).getClientId()+"",ctx.channel());
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //channel失效，从Map中移除
        System.out.println("链接失效了");
        TimeServer.channelMap.values().remove(ctx.channel());

    }
}