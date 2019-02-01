package tools.netty.time;

import java.util.ArrayList;

public class ArrayListTest {
    public static void main(String[] args) {
        ArrayList<String> msgs = new ArrayList<>();
        new Thread(()->{
            int i=0;
            while(true){
                if(msgs.size()<=10) {
                    System.out.println("add="+i);
                    msgs.add("" + i);
                    i++;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }).start();
        new Thread(()->{
            int i=0;
            while(true){
                if(msgs.size()>=10) {
                    System.out.println(msgs.remove(5));
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }).start();
    }
}
