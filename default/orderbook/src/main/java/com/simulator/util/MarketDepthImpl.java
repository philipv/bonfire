package com.simulator.util;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.simulator.data.Quote;
import com.simulator.data.Sequenceable;
import com.simulator.data.Side;
import com.simulator.data.Trade;
import com.simulator.factory.InjectionManager;

public class MarketDepthImpl implements MarketDepth{
	private static final double ALLOWED_DECIMAL_PLACES = 3;
	private PriorityQueue<Sequenceable<Quote>> depth;
	private IMatcher matcher;

	public MarketDepthImpl(InjectionManager factoryUtility, Side side) {
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
			throw new IllegalArgumentException("Price and Quantity cannot be null(" + newQuote + ")");
		}
		
		if(newQuote.getPrice()<=0 || newQuote.getQuantity()<=0){
			throw new IllegalArgumentException("Price and Quantity cannot be 0 or less(" + newQuote + ")");
		}
		
		if(!isPriceValid(newQuote.getPrice())){
			throw new IllegalArgumentException("Price cannot be more than " + ALLOWED_DECIMAL_PLACES + " decimal places(" + newQuote + ")");
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
			long unfilledQuantity = newQuote.getQuantity() - quoteOnBook.getQuantity();
			trade.setQuantity(quoteOnBook.getQuantity());
			newQuote.setQuantity(unfilledQuantity);
			depth.poll();
		}else{
			long unfilledQuantity = quoteOnBook.getQuantity() - newQuote.getQuantity();
			trade.setQuantity(newQuote.getQuantity());
			newQuote.setQuantity(0L);
			quoteOnBook.setQuantity(unfilledQuantity);
		}
		return trade;
	}
	
	@Override
	public boolean add(Quote newQuote) {
		return depth.add(new Sequenceable<Quote>(newQuote));
	}
	
	private boolean isPriceValid(double quotePrice){
		double price = quotePrice * Math.pow(10d, ALLOWED_DECIMAL_PLACES);
		double difference = price - (int)price;
		return difference<0.1d;
	}
}
