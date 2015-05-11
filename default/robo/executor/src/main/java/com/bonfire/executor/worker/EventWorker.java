package com.bonfire.executor.worker;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bonfire.injector.DependencyInjector;

public class EventWorker<T extends Runnable> implements ManagedExecutor {
	private ScheduledExecutorService scheduledExecutorService;
	private MBeanServer mBeanServer;
	private ObjectName mBeanName;
	private AtomicInteger receivedCount = new AtomicInteger();
	private static final Logger LOGGER = LoggerFactory.getLogger(EventWorker.class);
	private static int MAX_RETRIES = 3;
	
	public EventWorker(DependencyInjector injector){
		this.scheduledExecutorService = injector.createExecutors();
		this.mBeanServer = injector.createMBeanServer();
		try {
			this.mBeanName = injector.createMBeanName(this.getClass());
			mBeanServer.registerMBean(new StandardMBean(this, ManagedExecutor.class), mBeanName);
		} catch (Exception e) {
			LOGGER.warn("Cannot monitor this executor due to failure in registration", e);
		}
	}
	
	public void submit(T work){
		scheduledExecutorService.execute(work);
		receivedCount.incrementAndGet();
	}
	
	public void submit(Date executionTime, T work){
		scheduledExecutorService.schedule(work, executionTime.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		receivedCount.incrementAndGet();
	}
	
	public void scheduleUntil(T work, long initialDelay, long execIntervalMs, long cancelTime){
		ScheduledFuture<?> future = scheduledExecutorService.scheduleAtFixedRate(work, initialDelay, execIntervalMs, TimeUnit.MILLISECONDS);
		scheduledExecutorService.schedule(new CancelRepetitiveTask(future), cancelTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		
	}

	@Override
	public int getReceived() {
		return receivedCount.get();
	}
	
	public void destroy(){
		scheduledExecutorService.shutdown();
		try {
			mBeanServer.unregisterMBean(mBeanName);
		} catch (MBeanRegistrationException e) {
			LOGGER.error(String.format("Could not unreister %s from %s", mBeanName, mBeanServer));
		} catch (InstanceNotFoundException e) {
			LOGGER.warn(String.format("Could not unegister %s as it was not registered before", mBeanName));;
		}
	}
	
	private class CancelRepetitiveTask implements Runnable{
		
		private ScheduledFuture<?> future;
		
		public CancelRepetitiveTask(ScheduledFuture<?> future){
			this.future = future;
		}

		@Override
		public void run() {
			int retryCount = 1;
			while(retryCount<=MAX_RETRIES && !future.cancel(false)){
				retryCount++;
			}
			if(!future.isCancelled()){
				LOGGER.error("Could not cancel a repetitive task from the executor. Can run untill the judgement day!!");
			}
		}
	}

}
