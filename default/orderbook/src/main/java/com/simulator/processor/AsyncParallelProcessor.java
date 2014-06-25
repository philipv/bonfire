package com.simulator.processor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.factory.InjectionManager;
import com.simulator.processor.task.CreateQuoteTask;

public class AsyncParallelProcessor {
	private MarketProcessor[] marketProcessors;
	private ExecutorService[] singleThreadedExecutors;
	private int cores;
	
	public AsyncParallelProcessor(int cores, InjectionManager injectionManager) {
		this.cores = cores;
		setMarketProcessors(injectionManager.createMultiMarketProcessors(cores));
		setSingleThreadedExecutors(injectionManager.createMultiExecutors(cores));
		
		for(int i=0;i<cores;i++){
			marketProcessors[i] = injectionManager.createMarketProcessor();
			singleThreadedExecutors[i] = injectionManager.createSingleThreadedExecutor();
		}
	}

	public Future<MarketUpdate<Double, Long>> process(final Quote newQuote){
		if(newQuote!=null){
			final int executorId = newQuote.getSymbol()!=null?getExecutorId(newQuote.getSymbol()):getExecutorId("");
			CreateQuoteTask task = new CreateQuoteTask(marketProcessors[executorId], newQuote);
			Future<MarketUpdate<Double, Long>> createQuoteFuture = singleThreadedExecutors[executorId].submit(task);
			return createQuoteFuture;	
		}
		return null;
	}

	public int getExecutorId(Object key) {
		return key.hashCode()%cores;
	}
	
	public void setMarketProcessors(MarketProcessor[] marketProcessors) {
		this.marketProcessors = marketProcessors;
	}

	public void setSingleThreadedExecutors(ExecutorService[] singleThreadedExecutors) {
		this.singleThreadedExecutors = singleThreadedExecutors;
	}
	
	public void shutdown(){
		for(ExecutorService singleThreadedExecutor:singleThreadedExecutors){
			singleThreadedExecutor.shutdown();
		}
	}
}
