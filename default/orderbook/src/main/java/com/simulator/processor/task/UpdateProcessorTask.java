package com.simulator.processor.task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.simulator.data.MarketUpdate;

public class UpdateProcessorTask implements Runnable {
	private final Future<MarketUpdate<Double, Integer>> future;

	public UpdateProcessorTask(Future<MarketUpdate<Double, Integer>> future) {
		this.future = future;
	}

	@Override
	public void run(){
		try {
			if(future!=null){
				System.out.println(future.get());
			}
		} catch (ExecutionException|InterruptedException e) {
			System.out.println("Couldn't receive update from market processor");
			e.printStackTrace();
		}
	}
}