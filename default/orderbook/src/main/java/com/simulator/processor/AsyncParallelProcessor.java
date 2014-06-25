package com.simulator.processor;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.factory.InjectionManager;
import com.simulator.processor.task.CreateQuoteTask;

public class AsyncParallelProcessor implements IAsyncProcessor<Quote, MarketUpdate<Double, Long>>{
	private List<MarketProcessor> marketProcessors;
	private List<ExecutorService> singleThreadedExecutors;
	private int cores;
	
	public AsyncParallelProcessor(int cores, InjectionManager injectionManager) {
		this.cores = cores;
		
		List<MarketProcessor> marketProcessors = injectionManager.createMultiMarketProcessors(cores);
		List<ExecutorService> singleThreadedExecutors = injectionManager.createMultiExecutors(cores);
		for(int i=0;i<cores;i++){
			marketProcessors.add(injectionManager.createMarketProcessor());
			singleThreadedExecutors.add(injectionManager.createSingleThreadedExecutor());
		}
		setMarketProcessors(Collections.unmodifiableList(marketProcessors));
		setSingleThreadedExecutors(Collections.unmodifiableList(singleThreadedExecutors));
	}

	@Override
	public Future<MarketUpdate<Double, Long>> process(final Quote newQuote){
		if(newQuote!=null){
			final int executorId = newQuote.getSymbol()!=null?getExecutorId(newQuote.getSymbol()):getExecutorId("");
			CreateQuoteTask task = new CreateQuoteTask(marketProcessors.get(executorId), newQuote);
			Future<MarketUpdate<Double, Long>> createQuoteFuture = singleThreadedExecutors.get(executorId).submit(task);
			return createQuoteFuture;	
		}
		return null;
	}
 
	public int getExecutorId(Object key) {
		return key.hashCode()%cores;
	}
	
	public void setMarketProcessors(List<MarketProcessor> marketProcessors) {
		this.marketProcessors = marketProcessors;
	}

	public void setSingleThreadedExecutors(List<ExecutorService> singleThreadedExecutors) {
		this.singleThreadedExecutors = singleThreadedExecutors;
	}
	
	@Override
	public void shutdown(){
		for(ExecutorService singleThreadedExecutor:singleThreadedExecutors){
			singleThreadedExecutor.shutdown();
		}
	}
}
