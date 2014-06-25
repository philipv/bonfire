package com.simulator.util;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.simulator.data.Quote;
import com.simulator.data.Sequenceable;
import com.simulator.data.Side;
import com.simulator.data.Trade;
import com.simulator.factory.FactoryUtility;

public class MarketDepthImpl implements MarketDepth{
	private PriorityQueue<Sequenceable<Quote>> depth;
	private IMatcher matcher;

	public MarketDepthImpl(FactoryUtility factoryUtility, Side side) {
		this.depth = factoryUtility.createPriorityQueue(side);
		switch(side){
			case B:
				matcher = new IMatcher() {
					@Override
					public boolean isMatchable(Quote newAsk, Quote bidOnBook) {
						return bidOnBook.compareTo(newAsk)>=0;
					}
				};
				break;
			case S:
				matcher = new IMatcher() {
					@Override
					public boolean isMatchable(Quote newBid, Quote askOnBook) {
						return askOnBook.compareTo(newBid)<=0;
					}
				};
			default:
			
		}
	}
	
	@Override
	public List<Trade> match(Quote newQuote) {
		if(newQuote.getPrice()==null || newQuote.getQuantity()==null){
			throw new IllegalArgumentException("Wrong quote inputted (" + newQuote + ")");
		}
		
		List<Trade> trades = new LinkedList<>();
		matchLoop:
		while(!newQuote.isEmpty()){
			Sequenceable<Quote> sequenceable = depth.peek();
			if(sequenceable!=null){
				Quote quoteOnBook = sequenceable.getEntry();
				if(matcher.isMatchable(newQuote, quoteOnBook)){
					trades.add(generateTrade(newQuote, quoteOnBook));
				}else{
					break matchLoop;
				}
			}else{
				break matchLoop;
			}
		}
		return trades;
	}
	
	private Trade generateTrade(Quote newQuote,	Quote quoteOnBook) {
		Trade trade = new Trade();
		trade.setPrice(quoteOnBook.getPrice());
		
		if(newQuote.getQuantity()>=quoteOnBook.getQuantity()){
			int unfilledQuantity = newQuote.getQuantity() - quoteOnBook.getQuantity();
			trade.setQuantity(quoteOnBook.getQuantity());
			newQuote.setQuantity(unfilledQuantity);
			depth.poll();
		}else{
			int unfilledQuantity = quoteOnBook.getQuantity() - newQuote.getQuantity();
			trade.setQuantity(newQuote.getQuantity());
			newQuote.setQuantity(0);
			quoteOnBook.setQuantity(unfilledQuantity);
		}
		return trade;
	}
	
	@Override
	public boolean add(Quote newQuote) {
		return depth.add(new Sequenceable<Quote>(newQuote));
	}
}
