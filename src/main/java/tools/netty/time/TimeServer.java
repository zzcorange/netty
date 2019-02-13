package tools.netty.time;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Discards any incoming data.
 */
public class TimeServer {

    private int port;
    private static Channel channel;
    public static Map<String,Channel> channelMap = new HashMap<>();
    public TimeServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {

                            ch.pipeline().addLast(new TimeEncoder())
                                    .addLast(new TimeDecoder())
                                    .addLast(new IdleStateHandler(10,0,0))
                                    .addLast(new TimeServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 1024)
//                    .option(ChannelOption.TCP_NODELAY, true)// (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
       //     f.channel().writeAndFlush("返回数据");
//            new Thread(()->{
//                int i=0;
//                while(true){
//                    if(TimeServerHandler.msg.length()==0) {
//                        TimeServerHandler.msg=""+i;
//                        System.out.println("serverMsg:"+i);
//                        i++;
//                    }
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//            }).start();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    /**
     * 向客户端发送消息
     */
    public void sendData() throws InterruptedException {
        Scanner sc = new Scanner(System.in);

        for (int i = 0; i < 1000; i++) {
            while(channelMap.isEmpty()){
//            System.out.println("channel是空");
                Thread.sleep(1000);
            }
            Channel temp = channelMap.get(channelMap.keySet().toArray()[0]);
            if (temp != null && temp.isActive()) {
                //获取一个键盘扫描器
                String nextLine = sc.nextLine();
                temp.writeAndFlush("i="+i+"参数："+nextLine);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        TimeServer timeServer = new TimeServer(port);
        new Thread(()->{
            try {
                timeServer.sendData();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        timeServer.run();

    }
}