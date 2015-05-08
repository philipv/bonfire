package com.bonfire.injector;

import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.management.MBeanServer;

public class DependencyInjector {
	public ScheduledExecutorService createExecutors(int n){
		return Executors.newScheduledThreadPool(n);
	}
	
	public ScheduledExecutorService createExecutors(){ 
		return Executors.newSingleThreadScheduledExecutor();
	}
	
	public MBeanServer createMBeanServer(){
		return ManagementFactory.getPlatformMBeanServer();
	}
}
