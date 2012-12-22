package com.bonfire.processor;

import com.bonfire.util.concurrent.BatchBlockingQueue;

public class BatchProcessor<T extends Runnable> extends Thread{
    
    private volatile boolean stopProcessor;
    private BatchBlockingQueue<Runnable> taskQueue;
    
    public BatchProcessor(int batchSize, long expireTime){
        taskQueue = new BatchBlockingQueue<Runnable>(batchSize, expireTime);
    }

    @Override
    public void run(){
        while(!stopProcessor){
            try {
                Object[] runnables = taskQueue.takeBatch();
                //System.out.println(System.currentTimeMillis() + " - Got batch of " + runnables.length);
                for(Object runnable:runnables){
                    ((T)runnable).run();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void stopProcessor(){
        stopProcessor = true;
    }

    public BatchBlockingQueue<Runnable> getTaskQueue() {
        return taskQueue;
    }
}
