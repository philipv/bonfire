package com.bonfire.processor;

import java.util.concurrent.LinkedBlockingQueue;

public class SerialProcessor extends Thread {
    private volatile boolean stopProcessor;
    private LinkedBlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
    
    @Override
    public void run(){
        while(!stopProcessor){
            try {
                Runnable runnable = taskQueue.take();
                runnable.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void stopProcessor(){
        stopProcessor = true;
    }

    public LinkedBlockingQueue<Runnable> getTaskQueue() {
        return taskQueue;
    }
}
