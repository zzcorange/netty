package tools.rpc;


import javax.xml.ws.Endpoint;

/**
 * 发布类
 * @author hqs
 *
 */
public class RPCPublisher {
    public static void main(String[] args) {
        //自己定义地址
        Endpoint.publish("http://localhost:9966/rpc", new RPCServiceImpl());
    }
}