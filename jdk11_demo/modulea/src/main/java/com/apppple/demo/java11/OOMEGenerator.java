package com.apppple.demo.java11;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <pre>
 *  模拟OOM的一个类
 * </pre>
 * @author fanhui.mengfh on 2021/5/27
 */
/**
 java \
   -Xmx1024m \
     -Xlog:gc*=debug:file=gc.log:utctime,uptime,tid,level:filecount=10,filesize=128m \
    -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=heapdump.hprof \
    -XX:StartFlightRecording=disk=true,dumponexit=true,filename=recording.jfr,maxsize=1024m,maxage=1d,settings=profile \
      OOMEGenerator.java
 */
public class OOMEGenerator {
    static BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        new Thread(new Consumer()).start();
        new Thread(new Producer()).start();
    }

    static class Producer implements Runnable {

        public void run() {
            while (true) {
                queue.offer(new byte[3 * 1024 * 1024]);

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    static class Consumer implements Runnable {
        public void run() {
            while (true) {
                try {
                    queue.take();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
