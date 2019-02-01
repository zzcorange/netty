package tools;

import java.util.ArrayList;

public class lambda {
    public  static void main(String...args){
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.forEach(System.out::println);
        arrayList.forEach(i->{
            System.out.println("f"+i);
        });
    }
}
