package com.simulator.processor;

import java.util.Map;

import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.exception.ProcessingFailedException;
import com.simulator.factory.FactoryUtility;
import com.simulator.orderbook.OrderBook;
public class MarketProcessor {
	
	private Map<String, OrderBook> orderBooks;
	private FactoryUtility factoryUtility;

	public MarketProcessor(Map<String, OrderBook> orderBooks, FactoryUtility factoryUtility) {
		this.orderBooks = orderBooks;
		this.factoryUtility = factoryUtility;
	}
	
	public MarketUpdate<Double, Integer> createMarketOrder(Quote newQuote) throws ProcessingFailedException{
		try{
			OrderBook orderBook = orderBooks.get(newQuote.getSymbol());
			if(orderBook==null){
				orderBook = factoryUtility.createOrderBook();
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
