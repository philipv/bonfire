package com.bonfire.injector;

import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

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
	
	public <T> ObjectName createMBeanName(Class<T> mBeanClass) throws MalformedObjectNameException{
		return new ObjectName(mBeanClass.getPackage() +":name=" + mBeanClass.getSimpleName());
	}
}
