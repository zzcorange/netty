package tools.netty.entity;

import net.sf.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Message implements Serializable {
    private static AtomicInteger temp = new AtomicInteger(0);
    private int clientId;
    private String message;
    public Message(String message){
        this.message = message;
        clientId = temp.getAndAdd(1);
    }
    public Message(String message,int clientId){
        this.message = message;
        this.clientId = clientId;
    }
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String toString(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("clientId",clientId);
        jsonObject.put("message",message);
        return jsonObject.toString();
    }

    public static void main(String[] args) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("a","b");
        map.put("c","d");
        System.out.println(JSONObject.fromObject(map).toString());
    }
}
