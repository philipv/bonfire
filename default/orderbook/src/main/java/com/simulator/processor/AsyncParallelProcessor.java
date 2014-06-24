package com.simulator.processor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.factory.FactoryUtility;

public class AsyncParallelProcessor {
	private final class QuoteTask implements
			Callable<MarketUpdate<Double, Integer>> {
		private final Quote newQuote;
		private MarketProcessor marketProcessor;

		private QuoteTask(MarketProcessor marketProcessor, Quote newQuote) {
			this.marketProcessor = marketProcessor;
			this.newQuote = newQuote;
		}

		@Override
		public MarketUpdate<Double, Integer> call() throws Exception {
			MarketUpdate<Double, Integer> result = marketProcessor.createMarketOrder(newQuote);
			return result;
		}
	}

	private MarketProcessor[] marketProcessors;
	private ExecutorService[] singleThreadedExecutors;
	private int cores;
	
	public AsyncParallelProcessor(int cores, FactoryUtility factoryUtility) {
		this.cores = cores;
		setMarketProcessors(factoryUtility.createMultiMarketProcessors(cores));
		setSingleThreadedExecutors(factoryUtility.createMultiExecutors(cores));
		
		for(int i=0;i<cores;i++){
			marketProcessors[i] = factoryUtility.createMarketProcessor();
			singleThreadedExecutors[i] = factoryUtility.createSingleThreadedExecutor();
		}
	}

	public Future<MarketUpdate<Double, Integer>> process(final Quote newQuote){
		if(newQuote!=null){
			final int executorId = newQuote.getSymbol()!=null?getExecutorId(newQuote.getSymbol()):getExecutorId("");
			return singleThreadedExecutors[executorId].submit(new QuoteTask(marketProcessors[executorId], newQuote));	
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
