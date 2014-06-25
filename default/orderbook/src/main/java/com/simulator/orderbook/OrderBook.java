package com.simulator.orderbook;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.data.Side;
import com.simulator.data.Trade;
import com.simulator.factory.FactoryUtility;
import com.simulator.util.MarketDepth;

public class OrderBook {
	private MarketDepth bids;
	private MarketDepth asks;
	private Map<Double, Integer> aggregatedBids;
	private Map<Double, Integer> aggregatedAsks;
	
	public OrderBook(FactoryUtility factoryUtility) {
		this.bids = factoryUtility.createMarketDepth(Side.B);
		this.asks = factoryUtility.createMarketDepth(Side.S);
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

	public MarketUpdate<Double, Integer> placeOrder(Quote newQuote) {
		if(newQuote.getSide()!=null){
			List<Trade> trades = new LinkedList<>();
			MarketUpdate<Double, Integer> matchResult = new MarketUpdate<>(trades);
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
				matchResult.addBidUpdates(trade.getPrice(), aggregatedBids.get(trade.getPrice()));
				remove(trade, aggregatedAsks);
				matchResult.addAskUpdates(trade.getPrice(), aggregatedAsks.get(trade.getPrice()));
			}
			placeOnDepth(newQuote);
			matchResult.setTrades(trades);
			matchResult.addAskUpdates(aggregatedAsks);
			matchResult.addBidUpdates(aggregatedBids);
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
			if(aggregatedQuantity<=0){
				aggregatedView.remove(trade.getPrice());
			}else{
				aggregatedView.put(trade.getPrice(), aggregatedQuantity);
			}
		}
	}
}

