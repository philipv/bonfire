package com.bonfire.util.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BatchBlockingQueue<T> {
    private int batchSize;
    private long expireTime;
    private Object[] batch;
    private volatile int indexPosition = 0;
    final Lock lock = new ReentrantLock();
    final Condition putCondition = lock.newCondition();
    final Condition takeCondition = lock.newCondition();
    
    public BatchBlockingQueue(int batchSize, long expireTime){
        this.batchSize = batchSize;
        this.expireTime = expireTime;
        this.batch = new Object[batchSize];
    }
    
    public void put(T t) throws InterruptedException{
        lock.lock();
        try{
            while(batch[batchSize - 1]!=null)
                putCondition.await();
            batch[indexPosition++] = t;
            if(indexPosition==batchSize)
                takeCondition.signal();
        }finally{
            lock.unlock();
        }
    }
    
    public Object[] takeBatch() throws InterruptedException{
        lock.lock();
        try{
            while(batch[0]==null){
                takeCondition.await(expireTime, TimeUnit.MILLISECONDS);
                if(batch[0]!=null){
                    break;
                }
            }
            Object[] currentBatch = new Object[indexPosition];
            System.arraycopy(batch, 0, currentBatch, 0, indexPosition);
            batch = new Object[batchSize];
            indexPosition = 0;
            putCondition.signal();
            return currentBatch;
        }finally{
            lock.unlock();
        }
    }
}
