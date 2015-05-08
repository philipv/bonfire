package com.bonfire.executor.worker;

import org.junit.Test;

import com.bonfire.injector.DependencyInjector;

public class TestEventWorker {
	
	@Test
	public void testExecutor() throws InterruptedException{
		System.setProperty("com.sun.management.jmxremote.port", "1234");
		EventWorker<Runnable> testExecutor = new EventWorker<Runnable>(new DependencyInjector());
		Thread.sleep(100000000);
	}
}
