package com.simulator.orderbook.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.simulator.orderbook.data.OrderBookUpdate;
import com.simulator.orderbook.data.Quote;
import com.simulator.orderbook.data.Trade;

public class OrderBook {
	private MarketDepth bids;
	private MarketDepth asks;
	private Map<Double, Integer> aggregatedBids;
	private Map<Double, Integer> aggregatedAsks;
	
	public OrderBook(MarketDepth bids, MarketDepth asks) {
		this.bids = bids;
		this.asks = asks;
		this.aggregatedBids = new HashMap<>();
		this.aggregatedAsks = new HashMap<>();
	}
	
	private void placeOnDepth(Quote newQuote) {
		if(!newQuote.isEmpty()){
			switch (newQuote.getSide()) {
			case B:
				if(bids.add(newQuote)){
					addOrUpdate(newQuote, aggregatedBids);
				}
				break;
			case S:
			default:
				if(asks.add(newQuote)){
					addOrUpdate(newQuote, aggregatedAsks);
				}
				break;
			}
		}
	}

	public OrderBookUpdate<Double, Integer> placeOrder(Quote newQuote) {
		if(newQuote.getSide()!=null){
			List<Trade> trades = new LinkedList<>();
			OrderBookUpdate<Double, Integer> matchResult = new OrderBookUpdate<>(trades, new HashMap<Double, 
					Integer>(), new HashMap<Double, Integer>());
			switch(newQuote.getSide()){
				case B:
					trades = asks.match(newQuote);
					break;
				case S:
				default:
					trades =  bids.match(newQuote);
					break;
			}
			for(Trade trade:trades){
				remove(trade, aggregatedBids);
				matchResult.addBidUpdate(trade.getPrice(), aggregatedBids.get(trade.getPrice()));
				remove(trade, aggregatedAsks);
				matchResult.addAskUpdate(trade.getPrice(), aggregatedBids.get(trade.getPrice()));
			}
			placeOnDepth(newQuote);
			matchResult.addBidUpdate(newQuote.getPrice(), aggregatedBids.get(newQuote.getPrice()));
			matchResult.addAskUpdate(newQuote.getPrice(), aggregatedBids.get(newQuote.getPrice()));
			return matchResult;
		} else {
			throw new IllegalArgumentException("Wrong quote inputted (" + newQuote + ")");
		}
	}
	
	private void addOrUpdate(Quote quote, Map<Double, Integer> aggregatedView){
		Integer aggregatedQuantity = aggregatedView.get(quote.getPrice());
		if(aggregatedQuantity==null){
			aggregatedQuantity = 0;
		}
		aggregatedQuantity = aggregatedQuantity + quote.getQuantity();
		aggregatedView.put(quote.getPrice(), aggregatedQuantity);
	}
	
	private void remove(Trade trade, Map<Double, Integer> aggregatedView){
		Integer aggregatedQuantity = aggregatedView.get(trade.getPrice());
		if(aggregatedQuantity!=null){
			aggregatedQuantity = aggregatedQuantity - trade.getQuantity();
		}
		aggregatedView.put(trade.getPrice(), aggregatedQuantity);
	}
}

