package com.bonfire.util.concurrent;

import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

import com.bonfire.processor.BatchProcessor;
import com.bonfire.processor.SerialProcessor;

public class TestBatchBlockingQueue {

    @Test
    public void testNormalBatches() throws InterruptedException{
        BatchProcessor batchProcessor = new BatchProcessor(100, 1000);
        BatchBlockingQueue<Runnable> taskQueue = batchProcessor.getTaskQueue();
        batchProcessor.start();
        final long startTime = System.currentTimeMillis();
        for(int i=0;i<9999;i++){
            final int j = i;
            taskQueue.put(new Runnable(){
                public void run() {
                    System.out.println("Count is " + j);
                }
            });
        }
        taskQueue.put(new Runnable(){
            public void run() {
                System.out.println("Total Time Taken = " + (System.currentTimeMillis() - startTime));
            }
        });
        Thread.sleep(100000);
    }
    
    @Test
    public void testBlockingQueue() throws InterruptedException{
        SerialProcessor serialProcessor = new SerialProcessor();
        LinkedBlockingQueue<Runnable> taskQueue = serialProcessor.getTaskQueue();
        serialProcessor.start();
        final long startTime = System.currentTimeMillis();
        for(int i=0;i<9999;i++){
            final int j = i;
            taskQueue.put(new Runnable(){
                public void run() {
                    System.out.println("Count is " + j);
                }
            });
        }
        taskQueue.put(new Runnable(){
            public void run() {
                System.out.println("Total Time Taken = " + (System.currentTimeMillis() - startTime));
            }
        });
        Thread.sleep(100000);
    }

}
