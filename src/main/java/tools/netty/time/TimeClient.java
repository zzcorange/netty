package tools.netty.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import tools.netty.entity.Message;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TimeClient {
    private NioEventLoopGroup worker = new NioEventLoopGroup();
    public static int  clientId = 1;
    private static Channel channel;

    private static Bootstrap b;
    /**
     * 连接服务端 and 重连
     */
    protected void doConnect() {

        if (channel != null && channel.isActive()){
            return;
        }
        ChannelFuture connect = b.connect("127.0.0.1", 8080);
        //实现监听通道连接的方法
        connect.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {

                if(channelFuture.isSuccess()){
                    channel = channelFuture.channel();
                    System.out.println("连接成功");
                }else{
                    System.out.println("每隔2s重连....");
                    channelFuture.channel().eventLoop().schedule(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            doConnect();
                        }
                    },2, TimeUnit.SECONDS);
                }
            }
        });
    }
    public  void start()
        {
            String host = "localhost";
            int port = 8080;
            EventLoopGroup workerGroup = new NioEventLoopGroup();
//        new Thread(()->{
//            Scanner sc = new Scanner(System.in);
//            while (true){
//                String name = sc.next();
//                System.out.println(name);
//            }
//        }).start();
            try {
                b = new Bootstrap(); // (1)
                b.group(workerGroup); // (2)
                b.channel(NioSocketChannel.class); // (3)
                b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
                b.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
//                                .addLast(new TimeDecoder())
//                                .addLast(new TimeEncoder())
                                .addLast(new ObjectEncoder())
                                .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
                                .addLast(new IdleStateHandler(0,0,5))
                                .addLast(new TimeClientHandler(TimeClient.this));
                    }
                });

                doConnect();
            } catch (Exception e){
                e.printStackTrace();
                System.exit(-1);
            }
            finally {
//                workerGroup.shutdownGracefully();
            }
        }
    /**
     * 向服务端发送消息
     */
    public void sendData() throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        while(channel==null){
            System.out.println("channel是空");
            Thread.sleep(1000);
        }
        for (int i = 0; i < 1000; i++) {
            System.out.println(channel==null);
            if (channel != null && channel.isActive()) {
                //获取一个键盘扫描器
                String nextLine = sc.nextLine();
                channel.writeAndFlush(new Message("i="+i,clientId));
            }
        }
    }
    public static void main(String[] args) {
        try{
            TimeClient timeClient = new TimeClient();
            timeClient.start();
            timeClient.sendData();
        } catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }

    }
}