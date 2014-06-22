package com.simulator.orderbook;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.simulator.orderbook.data.Quote;
import com.simulator.orderbook.data.Trade;
import com.simulator.orderbook.exception.ProcessingFailedException;
import com.simulator.orderbook.util.MarketDepth;
import com.simulator.orderbook.util.OrderBook;
import com.simulator.orderbook.util.ReverseComparator;

public class ExchangeFacade {
	
	private Map<String, OrderBook> orderBooks;

	public ExchangeFacade(Map<String, OrderBook> orderBooks) {
		this.orderBooks = orderBooks;
	}
	
	public List<Trade> create(Quote newQuote) throws ProcessingFailedException{
		List<Trade> trades = new LinkedList<>();
		try{
			if(newQuote!=null){
				OrderBook orderBook = orderBooks.get(newQuote.getSymbol());
				if(orderBook==null){
					orderBook = new OrderBook(new MarketDepth(new TreeMap<Double, List<Quote>>(new ReverseComparator<Double>())), 
							new MarketDepth(new TreeMap<Double, List<Quote>>()));
					orderBooks.put(newQuote.getSymbol(), orderBook);
				}
				
				trades = orderBook.match(newQuote);
				if(newQuote.getQuantity()>0){
					orderBook.place(newQuote);
				}
			}
		}catch(RuntimeException ex){
			if(ex instanceof IllegalArgumentException){
				throw new ProcessingFailedException(ex);
			}else{
				throw new ProcessingFailedException("Internal Server Error. Please contact support.");
			}
		}
		return trades;
	}
	
	
}
