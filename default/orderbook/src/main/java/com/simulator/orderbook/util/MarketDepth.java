package com.simulator.orderbook.util;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.simulator.orderbook.data.Quote;
import com.simulator.orderbook.data.Sequenceable;
import com.simulator.orderbook.data.Side;
import com.simulator.orderbook.data.Trade;

public class MarketDepth {
	private PriorityQueue<Sequenceable<Quote>> depth;
	private IMatchable matchableComparator;

	public MarketDepth(PriorityQueue<Sequenceable<Quote>> depth, Side side) {
		this.depth = depth;
		switch(side){
			case B:
				matchableComparator = new IMatchable() {
					@Override
					public boolean isMatchable(Quote newAsk, Quote bidOnBook) {
						return bidOnBook.compareTo(newAsk)>=0;
					}
				};
				break;
			case S:
				matchableComparator = new IMatchable() {
					@Override
					public boolean isMatchable(Quote newBid, Quote askOnBook) {
						return askOnBook.compareTo(newBid)<=0;
					}
				};
			default:
			
		}
	}
	
	public List<Trade> match(Quote newQuote) {
		if(newQuote.getPrice()==null || newQuote.getQuantity()==null){
			throw new IllegalArgumentException("Wrong quote inputted (" + newQuote + ")");
		}
		
		List<Trade> trades = new LinkedList<>();
		matchLoop:
		while(newQuote.getQuantity()>0){
			Sequenceable<Quote> sequenceable = depth.peek();
			if(sequenceable!=null){
				Quote quoteOnBook = sequenceable.getEntry();
				if(matchableComparator.isMatchable(newQuote, quoteOnBook)){
					trades.add(generateTrade(newQuote, quoteOnBook));
				}else{
					break matchLoop;
				}
				
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
	
	public void placeQuote(Quote newQuote) {
		depth.add(new Sequenceable<Quote>(newQuote));
	}
}
