package com.simulator.factory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.simulator.data.Quote;
import com.simulator.data.Sequenceable;
import com.simulator.data.Side;
import com.simulator.orderbook.OrderBook;
import com.simulator.processor.AsyncParallelProcessor;
import com.simulator.processor.MarketProcessor;
import com.simulator.util.AscendingComparator;
import com.simulator.util.DescendingComparator;
import com.simulator.util.MarketDepth;
import com.simulator.util.MarketDepthImpl;

/*
 * Utility class for creating different types of objects
 */
public class InjectionManager {
	
	public BufferedReader createBufferedReader(InputStreamReader inputStreamReader) {
		return new BufferedReader(inputStreamReader);
	}
	
	public MarketDepth createMarketDepth(Side side){
		switch(side){
			case B: 
				return new MarketDepthImpl(this, Side.B);
			case S:
			default:
				return new MarketDepthImpl(this, Side.S);
		}
	}

	public PriorityQueue<Sequenceable<Quote>> createPriorityQueue(Side side) {
		switch(side){
			case B: 
				return new PriorityQueue<>(16, new DescendingComparator<>());
			case S:
			default:
				return new PriorityQueue<>(16, new AscendingComparator<>());
		}
	}
	
	public OrderBook createOrderBook(){
		return new OrderBook(this);
	}
	
	public MarketProcessor createMarketProcessor() {
		return new MarketProcessor(this);
	}

	public Map<String, OrderBook> createOrderBookPerSymbol() {
		return new HashMap<String, OrderBook>();
	}
	
	public ExecutorService createSingleThreadedExecutor() {
		return Executors.newSingleThreadExecutor();
	}
	
	public MarketProcessor[] createMultiMarketProcessors(int cores) {
		return new MarketProcessor[cores];
	}
	
	public ExecutorService[] createMultiExecutors(int cores) {
		return new ExecutorService[cores];
	}
	
	public AsyncParallelProcessor createAsyncProcessor(int cores) {
		return new AsyncParallelProcessor(cores, this);
	}
	
}
