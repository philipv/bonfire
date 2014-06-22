package com.simulator.orderbook;

import java.util.List;

import com.simulator.orderbook.data.Quote;
import com.simulator.orderbook.data.Trade;
import com.simulator.orderbook.util.OrderBook;

public class ExchangeFacade {
	
	private OrderBook orderBook;

	public ExchangeFacade(OrderBook orderBook) {
		super();
		this.orderBook = orderBook;
	}
	
	public List<Trade> create(Quote newQuote){
		List<Trade> trades = orderBook.match(newQuote);
		if(newQuote.getQuantity()>0){
			orderBook.place(newQuote);
		}
		return trades;
	}
	
	
}
