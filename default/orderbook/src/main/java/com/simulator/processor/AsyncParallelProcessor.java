package com.simulator.processor;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.factory.InjectionManager;
import com.simulator.processor.task.CreateQuoteTask;

/**
 * @author vinith
 * This is an IAsyncProcessor implementation which uses ExecutorService to dispatch tasks to a 
 * separate thread for processing. This class is thread safe and could be used by multiple threads.
 * This takes a Quote and returns a MarketUpdate.
 */
public class AsyncParallelProcessor implements IProcessor<Quote, Future<MarketUpdate<Double, Long>>>{
	private List<? extends IProcessor<Quote, MarketUpdate<Double, Long>>> marketProcessors;
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

	/**
	 * This method is to dispatch the newQuote to a separate thread(from the calling thread).
	 * This method ensure that subsequent quotes for same symbol go to the same thread and to do 
	 * that it uses a modulus of hashCode on the symbol of the quote. This can also accept a null 
	 * symbol and still continue to dispatch without any issues.
	 * @see com.simulator.processor.IProcessor#process(java.lang.Object)
	 */
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
	
	/** 
	 * This is to shutdown all the executor services in case of a process shutdown.
	 * @see com.simulator.processor.IProcessor#shutdown()
	 */
	@Override
	public void shutdown(){
		for(ExecutorService singleThreadedExecutor:singleThreadedExecutors){
			singleThreadedExecutor.shutdown();
		}
	}
}
