package com.simulator.processor;

import java.util.Map;
import java.util.PriorityQueue;

import com.simulator.data.OrderBookUpdate;
import com.simulator.data.Quote;
import com.simulator.data.Side;
import com.simulator.exception.ProcessingFailedException;
import com.simulator.orderbook.OrderBook;
import com.simulator.util.AscendingComparator;
import com.simulator.util.DescendingComparator;
import com.simulator.util.MarketDepth;
public class OrderProcessor {
	
	private Map<String, OrderBook> orderBooks;

	public OrderProcessor(Map<String, OrderBook> orderBooks) {
		this.orderBooks = orderBooks;
	}
	
	public OrderBookUpdate<Double, Integer> createMarketOrder(Quote newQuote) throws ProcessingFailedException{
		try{
			if(newQuote!=null){
				OrderBook orderBook = orderBooks.get(newQuote.getSymbol());
				if(orderBook==null){
					orderBook = new OrderBook(new MarketDepth(new PriorityQueue<>(16, new DescendingComparator<>()), Side.B), 
							new MarketDepth(new PriorityQueue<>(16, new AscendingComparator<>()), Side.S));
					orderBooks.put(newQuote.getSymbol(), orderBook);
				}
				
				OrderBookUpdate<Double, Integer> orderBookUpdate = orderBook.placeOrder(newQuote);
				return orderBookUpdate;
			}else{
				throw new IllegalArgumentException("Wrong quote inputted (" + newQuote + ")");
			}
		}catch(RuntimeException ex){
			if(ex instanceof IllegalArgumentException){
				throw new ProcessingFailedException(ex);
			}else{
				throw new ProcessingFailedException("Internal Server Error.", ex);
			}
	
		}
	}
	
}
