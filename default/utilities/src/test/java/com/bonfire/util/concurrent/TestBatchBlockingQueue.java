package com.bonfire.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Test;

import com.bonfire.processor.BatchProcessor;

public class TestBatchBlockingQueue {

    @Test
    public void test() throws InterruptedException{
        BatchProcessor batchProcessor = new BatchProcessor(50, 1000);
        BatchBlockingQueue<Runnable> taskQueue = batchProcessor.getTaskQueue();
        batchProcessor.start();
        
        for(int i=0;i<100;i++){
            final int j = i;
            taskQueue.put(new Runnable(){
                public void run() {
                    System.out.println("Count is " + j);
                }
            });
        }
    }

}
