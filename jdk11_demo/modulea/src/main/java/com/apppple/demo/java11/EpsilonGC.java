package com.apppple.demo.java11;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fanhui.mengfh on 2021/5/27
 */
public class EpsilonGC {
    public static void main(String[] args) {
        boolean flag = true;
        List<Garbage> garbageList = new ArrayList<>();
        int count = 0;
        while (flag) {
            garbageList.add(new Garbage());
            if (count ++ == 500) {
                garbageList.clear();
            }
        }
    }
}

class Garbage {
    private double d1 = 1;
    private double d2 = 2;

    /**
     * 在GC清除对象时会调用一次
     */
    @Override
    protected void finalize() throws Throwable {
        System.out.println(this + " collecting");
    }
}
