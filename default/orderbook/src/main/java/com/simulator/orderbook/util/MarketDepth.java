package com.simulator.orderbook.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.simulator.orderbook.data.Quote;
import com.simulator.orderbook.data.Trade;

public class MarketDepth {
	private TreeMap<Double, List<Quote>> depth;

	public MarketDepth(TreeMap<Double, List<Quote>> depth) {
		this.depth = depth;
	}
	
	public List<Trade> match(Quote newQuote) {
		SortedMap<Double, List<Quote>> matchablePriceLevels = depth.headMap(newQuote.getPrice(), true);
		List<Trade> trades = new LinkedList<>();
		Iterator<Entry<Double, List<Quote>>> priceLevelsIterator = matchablePriceLevels.entrySet().iterator();
		while(priceLevelsIterator.hasNext()){
			
			if(newQuote.getQuantity()>0){
				Entry<Double, List<Quote>> priceLevel = priceLevelsIterator.next();
				List<Quote> quotesOnPriceLevel = priceLevel.getValue();
				Iterator<Quote> quotesOnPriceLevelIterator = quotesOnPriceLevel.iterator();
				
				while(quotesOnPriceLevelIterator.hasNext()){
					if(newQuote.getQuantity()>0){
						Quote quoteOnPriceLevel = quotesOnPriceLevelIterator.next();
						trades.add(generateTrade(newQuote, priceLevel.getKey(),
								quotesOnPriceLevelIterator, quoteOnPriceLevel));
					}else{
						break;
					}
				}
				
				if(quotesOnPriceLevel.size()==0){
					priceLevelsIterator.remove();
				}
			}else{
				break;
			}
		}
		
		return trades;
	}
	
	private Trade generateTrade(Quote newQuote,	Double price,
			Iterator<Quote> quotesOnBookIterator, Quote quoteOnBook) {
		Trade trade = new Trade();
		trade.setPrice(price);
		
		if(newQuote.getQuantity()>quoteOnBook.getQuantity()){
			int unfilledQuantity = newQuote.getQuantity() - quoteOnBook.getQuantity();
			trade.setQuantity(quoteOnBook.getQuantity());
			newQuote.setQuantity(unfilledQuantity);
			quotesOnBookIterator.remove();
		}else{
			int unfilledQuantity = quoteOnBook.getQuantity() - newQuote.getQuantity();
			trade.setQuantity(newQuote.getQuantity());
			newQuote.setQuantity(0);
			quoteOnBook.setQuantity(unfilledQuantity);
		}
		return trade;
	}
	
	public void placeQuote(Quote newQuote) {
		List<Quote> quotesOnBook = depth.get(newQuote.getPrice());
		if(quotesOnBook==null){
			quotesOnBook = new LinkedList<>();
			depth.put(newQuote.getPrice(), quotesOnBook);
		}
		quotesOnBook.add(newQuote);
	}
}
