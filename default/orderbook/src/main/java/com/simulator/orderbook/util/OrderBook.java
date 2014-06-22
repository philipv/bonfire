package com.simulator.orderbook.util;

import java.util.List;

import com.simulator.orderbook.data.Quote;
import com.simulator.orderbook.data.Trade;

public class OrderBook {
	private MarketDepth bids;
	private MarketDepth asks;
	
	public OrderBook(MarketDepth bids, MarketDepth asks) {
		super();
		this.bids = bids;
		this.asks = asks;
	}
	
	public void place(Quote newQuote) {
		switch (newQuote.getSide()) {
			case B:
				bids.placeQuote(newQuote);;
			case S:
				asks.placeQuote(newQuote);;
		}
	}

	public List<Trade> match(Quote newQuote) {
		switch(newQuote.getSide()){
		case B:
			return asks.match(newQuote);
		case S:
			return bids.match(newQuote);
		default:
			throw new IllegalArgumentException("Invalid side on the quote");
	}
	}
}
