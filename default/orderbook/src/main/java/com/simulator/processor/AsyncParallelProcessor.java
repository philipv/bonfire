package com.simulator.processor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.data.Trade;
import com.simulator.exception.ProcessingFailedException;
import com.simulator.orderbook.OrderBook;

public class AsyncParallelProcessor {
	private MarketProcessor[] marketProcessors;
	private ExecutorService[] singleThreadedExecutors;
	private int cores;
	
	public AsyncParallelProcessor(int cores) {
		this.cores = cores;
		setMarketProcessors(new MarketProcessor[cores]);
		setSingleThreadedExecutors(new ExecutorService[cores]);
		
		for(int i=0;i<cores;i++){
			marketProcessors[i] = new MarketProcessor(new HashMap<String, OrderBook>());
			singleThreadedExecutors[i] = Executors.newSingleThreadExecutor();
		}
	}
	
	public Future<MarketUpdate<Double, Integer>> process(final Quote newQuote){
		if(newQuote!=null){
			final int executorId = newQuote.getSymbol()!=null?getExecutorId(newQuote.getSymbol()):getExecutorId("");
			return singleThreadedExecutors[executorId].submit(new Callable<MarketUpdate<Double, Integer>>() {
				@Override
				public MarketUpdate<Double, Integer> call() throws Exception {
					try{
						return marketProcessors[executorId].createMarketOrder(newQuote);
					}catch(ProcessingFailedException pfe){
						System.out.println("Couldn't process input");
						pfe.printStackTrace();
					}
					return new MarketUpdate<>(new LinkedList<Trade>(), new HashMap<Double, Integer>(), new HashMap<Double, Integer>());
				}
			});	
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
