package com.bonfire.util.concurrent;

import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

import com.bonfire.processor.BatchProcessor;
import com.bonfire.processor.SerialProcessor;

public class TestBatchBlockingQueue {

    @Test
    public void testInputFasterThanExpireTime() throws InterruptedException{
        BatchProcessor<Runnable> batchProcessor = new BatchProcessor<Runnable>(1000, 1000);
        BatchBlockingQueue<Runnable> taskQueue = batchProcessor.getTaskQueue();
        batchProcessor.start();
        final long startTime = System.currentTimeMillis();
        for(int i=0;i<999999;i++){
            final int j = i;
            taskQueue.put(getTask(false, j, startTime));
        }
        taskQueue.put(getTask(true, 0, startTime));
        Thread.sleep(10000000);
    }
    
    @Test
    public void testBlockingQueue() throws InterruptedException{
        SerialProcessor serialProcessor = new SerialProcessor();
        LinkedBlockingQueue<Runnable> taskQueue = serialProcessor.getTaskQueue();
        serialProcessor.start();
        final long startTime = System.currentTimeMillis();
        for(int i=0;i<999999;i++){
            final int j = i;
            taskQueue.put(getTask(false, j, startTime));
        }
        taskQueue.put(getTask(true, 0, startTime));
        Thread.sleep(10000000);
    }
    
    private Runnable getTask(boolean last, final int count, final long startTime){
        if(!last){
            return new Runnable(){
                public void run() {
                    //System.out.println("Count is " + count);
                }
            };
        }else{
            return new Runnable(){
                public void run() {
                    System.out.println("Total Time Taken = " + (System.currentTimeMillis() - startTime));
                }
            };
        }
         
    }

}
