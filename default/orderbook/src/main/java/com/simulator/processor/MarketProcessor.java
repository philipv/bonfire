package com.simulator.processor;

import java.util.Map;

import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.exception.ProcessingFailedException;
import com.simulator.factory.InjectionManager;
import com.simulator.orderbook.OrderBook;

/**
 * @author vinith
 * This class represents a bucket of symbols which can be managed by a single thread of execution. This class is a way to
 * scale out the event processing of symbols. This class cannot be used concurrently but multiple instances of this class
 * can run parallely on multiple threads. There is no data sharing above this layer(for a new incoming quote) and hence 
 * could be used to process quotes(lying in different buckets) parallely.
 */
public class MarketProcessor {
	
	private Map<String, OrderBook> orderBooks;
	private InjectionManager factoryUtility;

	public MarketProcessor(InjectionManager factoryUtility) {
		this.orderBooks = factoryUtility.createOrderBookPerSymbol();
		this.factoryUtility = factoryUtility;
	}
	
	/**
	 * This method forwards the new quote to appropriate order book for the respective symbol to match or/and 
	 * place on the market depth
	 * @param newQuote
	 * @return marketUpdate - consolidated view of impact of this new quote on market
	 * @throws ProcessingFailedException : This wraps any error thrown by the layer underneath
	 */
	public MarketUpdate<Double, Long> createMarketOrder(Quote newQuote) throws ProcessingFailedException{
		try{
			OrderBook orderBook = orderBooks.get(newQuote.getSymbol());
			if(orderBook==null){
				orderBook = factoryUtility.createOrderBook();
				orderBooks.put(newQuote.getSymbol(), orderBook);
			}
			
			MarketUpdate<Double, Long> orderBookUpdate = orderBook.placeOrder(newQuote);
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
