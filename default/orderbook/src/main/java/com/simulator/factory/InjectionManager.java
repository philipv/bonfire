package com.simulator.factory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.data.Sequenceable;
import com.simulator.data.Side;
import com.simulator.orderbook.OrderBook;
import com.simulator.processor.AsyncParallelProcessor;
import com.simulator.processor.IAsyncProcessor;
import com.simulator.processor.MarketProcessor;
import com.simulator.util.AscendingComparator;
import com.simulator.util.DescendingComparator;
import com.simulator.util.IMarketDepth;
import com.simulator.util.MarketDepthImpl;

/*
 * Utility class for creating different types of objects
 */
public class InjectionManager {
	
	public BufferedReader createBufferedReader(InputStreamReader inputStreamReader) {
		return new BufferedReader(inputStreamReader);
	}
	
	public IMarketDepth createMarketDepth(Side side){
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
				return new PriorityQueue<>(16, new DescendingComparator<Sequenceable<Quote>, Quote>());
			case S:
			default:
				return new PriorityQueue<>(16, new AscendingComparator<Sequenceable<Quote>, Quote>());
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
	
	public List<MarketProcessor> createMultiMarketProcessors(int cores) {
		return new ArrayList<MarketProcessor>(cores);
	}
	
	public List<ExecutorService> createMultiExecutors(int cores) {
		return new ArrayList<ExecutorService>(cores);
	}
	
	public IAsyncProcessor<Quote, MarketUpdate<Double, Long>> createAsyncProcessor(int cores) {
		return new AsyncParallelProcessor(cores, this);
	}
	
}
