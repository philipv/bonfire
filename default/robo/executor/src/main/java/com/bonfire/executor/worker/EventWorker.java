package com.bonfire.executor.worker;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bonfire.injector.DependencyInjector;

public class EventWorker<T extends Runnable> implements ManagedExecutor {
	private ScheduledExecutorService scheduledExecutorService;
	private MBeanServer mBeanServer;
	private AtomicInteger receivedCount = new AtomicInteger();
	private static final Logger LOGGER = LoggerFactory.getLogger(EventWorker.class);
	
	public EventWorker(DependencyInjector injector){
		this.scheduledExecutorService = injector.createExecutors();
		this.mBeanServer = injector.createMBeanServer();
		try {
			mBeanServer.registerMBean(this, new ObjectName("SimpleAgent:name=" + getClass().getName()));
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

	@Override
	public int getReceived() {
		return receivedCount.get();
	}

	@Override
	public int getPending() {
		return 0;
	}
}
