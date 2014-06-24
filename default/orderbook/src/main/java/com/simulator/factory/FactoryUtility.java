package com.simulator.factory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.simulator.data.Side;
import com.simulator.orderbook.OrderBook;
import com.simulator.processor.MarketProcessor;
import com.simulator.util.AscendingComparator;
import com.simulator.util.DescendingComparator;
import com.simulator.util.MarketDepth;

/*
 * Utility class for creating different types of objects
 */
public class FactoryUtility {
	
	public BufferedReader createBufferedReader(InputStreamReader inputStreamReader) {
		return new BufferedReader(inputStreamReader);
	}
	
	public MarketDepth createMarketDepth(Side side){
		switch(side){
			case B: 
				return new MarketDepth(new PriorityQueue<>(16, new DescendingComparator<>()), Side.B);
			case S:
			default:
				return new MarketDepth(new PriorityQueue<>(16, new AscendingComparator<>()), Side.S);
		}
	}
	
	public OrderBook createOrderBook(){
		return new OrderBook(createMarketDepth(Side.B), createMarketDepth(Side.S));
	}
	
	public MarketProcessor createMarketProcessor() {
		return new MarketProcessor(new HashMap<String, OrderBook>(),new FactoryUtility());
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
	
}
