package com.simulator.processor;

import java.util.Map;
import java.util.PriorityQueue;

import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.data.Side;
import com.simulator.exception.ProcessingFailedException;
import com.simulator.orderbook.OrderBook;
import com.simulator.util.AscendingComparator;
import com.simulator.util.DescendingComparator;
import com.simulator.util.MarketDepth;
public class MarketProcessor {
	
	private Map<String, OrderBook> orderBooks;

	public MarketProcessor(Map<String, OrderBook> orderBooks) {
		this.orderBooks = orderBooks;
	}
	
	public MarketUpdate<Double, Integer> createMarketOrder(Quote newQuote) throws ProcessingFailedException{
		try{
			OrderBook orderBook = orderBooks.get(newQuote.getSymbol());
			if(orderBook==null){
				orderBook = new OrderBook(new MarketDepth(new PriorityQueue<>(16, new DescendingComparator<>()), Side.B), 
						new MarketDepth(new PriorityQueue<>(16, new AscendingComparator<>()), Side.S));
				orderBooks.put(newQuote.getSymbol(), orderBook);
			}
			
			MarketUpdate<Double, Integer> orderBookUpdate = orderBook.placeOrder(newQuote);
			return orderBookUpdate;
		}catch(RuntimeException ex){
			if(ex instanceof IllegalArgumentException){
				throw new ProcessingFailedException(ex);
			}else{
				throw new ProcessingFailedException("Internal Server Error.", ex);
			}
	
		}
	}
	
}
