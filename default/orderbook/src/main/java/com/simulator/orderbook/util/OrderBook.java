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
		if(newQuote.getSide()!=null){
			switch (newQuote.getSide()) {
				case B:
					bids.placeQuote(newQuote);
					break;
				case S:
				default:
					asks.placeQuote(newQuote);
					break;
			}
		}else{
			throw new IllegalArgumentException("Wrong quote inputted (" + newQuote + ")");
		}
	}

	public List<Trade> match(Quote newQuote) {
		if(newQuote.getSide()!=null){
			switch(newQuote.getSide()){
				case B:
					return asks.match(newQuote);
				case S:
				default:
					return bids.match(newQuote);
			}
		} else {
			throw new IllegalArgumentException("Wrong quote inputted (" + newQuote + ")");
		}
	}
}
